package de.fernuni.hagen.ma.gundermann.signaturematching.matching.types;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.glue.factories.ProxyCreatorFactories;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatcherRate;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatchingInfo.Builder;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatchingSupplier;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods.MatchingMethod;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods.MethodMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods.ParamPermMethodMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.util.Logger;

/**
 * {@link TypeMatcher} fuer Typen, deren Methoden miteinander gematcht werden.
 * 
 * @author Niels Gundermann
 *
 */
public class StructuralTypeMatcher {

	private final MethodMatcher methodMatcher;

	public StructuralTypeMatcher(final Supplier<TypeMatcher> innerMethodMatcherSupplier) {
		this.methodMatcher = new ParamPermMethodMatcher(innerMethodMatcherSupplier);
	}

	public boolean matchesType(Class<?> checkType, Class<?> queryType) {
		Logger.info(String.format("%s MATCH? %s", checkType.getSimpleName(), queryType.getSimpleName()));
		Method[] queryMethods = getQueryMethods(queryType);
		Map<Method, Collection<Method>> possibleMatches = convertMethod2MethodCollection(
				collectPossibleMatches(queryMethods, checkType.getMethods()));
		printPossibleMatches(possibleMatches);
		return possibleMatches.values().stream().anyMatch(l -> !l.isEmpty());
	}

	public MatchingInfo calculateTypeMatchingInfos(Class<?> targetType, Class<?> sourceType) {
		Builder miBuilder = new MatchingInfo.Builder(sourceType, targetType,
				ProxyCreatorFactories.getInterfaceProxyFactoryCreator());
		if (sourceType.equals(Object.class)) {
			return miBuilder.build();
		}

		Method[] queryMethods = getQueryMethods(sourceType);
		Logger.infoF("QueryMethods: %s",
				Stream.of(queryMethods).map(m -> m.getName()).collect(Collectors.joining(", ")));

		Method[] potentialMethods = getPotentialDelegateMethods(targetType.getMethods());
		Map<Method, Collection<MatchingMethod>> possibleMatches = collectPossibleMatches(queryMethods,
				potentialMethods);
		Map<Method, MatchingSupplier> matchingInfoSupplier = new HashMap<>();
		for (Entry<Method, Collection<MatchingMethod>> qM2tM : possibleMatches.entrySet()) {
			MatchingSupplier supplier = getSupplierOfMultipleMatchingMethods(qM2tM.getKey(), qM2tM.getValue());
			matchingInfoSupplier.put(qM2tM.getKey(), supplier);
		}
		matchingInfoSupplier.entrySet().forEach(
				e -> Logger.infoF("Supplier for MethodMatchingInfos collected - Method: %s", e.getKey().getName()));
		miBuilder.withMatchedSourceMethods(Arrays.asList(queryMethods));
		miBuilder.withMethodMatchingInfoSupplier(matchingInfoSupplier);
		return miBuilder.build();
	}

	private void printPossibleMatches(Map<Method, Collection<Method>> possibleMatches) {
		for (Entry<Method, Collection<Method>> entry : possibleMatches.entrySet()) {
			Logger.info(String.format("QUERYM: %s", entry.getKey().getName()));
			for (Method match : entry.getValue()) {
				Logger.info(String.format("    MATCHM: %s", match.getName()));
			}
		}
	}

	private Map<Method, Collection<MatchingMethod>> collectPossibleMatches(Method[] queryMethods,
			Method[] checkMethods) {
		Map<Method, Collection<MatchingMethod>> matches = new HashMap<>();
		for (Method queryMethod : queryMethods) {
			Collection<MatchingMethod> queryMethodMatches = new ArrayList<>();
			for (Method checkMethod : checkMethods) {
				MatcherRate rate = methodMatcher.matchesWithRating(checkMethod, queryMethod);
				if (rate != null) {
					queryMethodMatches.add(new MatchingMethod(checkMethod, rate));
				}
			}
			if (!queryMethodMatches.isEmpty()) {
				matches.put(queryMethod, queryMethodMatches);
			}
		}
		return matches;
	}

	private Method[] getQueryMethods(Class<?> queryType) {
		Method[] queryMethods = queryType.getMethods();
		return queryMethods;
	}

	private Method[] getPotentialDelegateMethods(Method[] methods) {
		return Stream.of(methods).filter(m -> !Modifier.isStatic(m.getModifiers())).collect(Collectors.toList())
				.toArray(new Method[] {});
	}

	private MatchingSupplier getSupplierOfMultipleMatchingMethods(Method queryMethod,
			Collection<MatchingMethod> matchingMethods) {
		Supplier<Collection<MethodMatchingInfo>> supplier = () -> {
			Collection<MethodMatchingInfo> metMIs = new ArrayList<>();
			for (MatchingMethod matching : matchingMethods) {
				metMIs.addAll(methodMatcher.calculateMatchingInfos(matching.getMethod(), queryMethod));
			}
			return metMIs;
		};
		return new MatchingSupplier(supplier,
				matchingMethods.stream().map(MatchingMethod::getRate).collect(Collectors.toList()));
	}

	private static Map<Method, Collection<Method>> convertMethod2MethodCollection(
			Map<Method, Collection<MatchingMethod>> collectPossibleMatches) {
		Map<Method, Collection<Method>> method2MethodCollection = new HashMap<>();
		for (Entry<Method, Collection<MatchingMethod>> entry : collectPossibleMatches.entrySet()) {
			method2MethodCollection.put(entry.getKey(),
					entry.getValue().stream().map(MatchingMethod::getMethod).collect(Collectors.toList()));
		}

		return method2MethodCollection;
	}

}
