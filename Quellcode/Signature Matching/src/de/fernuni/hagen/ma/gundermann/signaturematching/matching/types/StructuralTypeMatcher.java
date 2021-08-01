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
import de.fernuni.hagen.ma.gundermann.signaturematching.glue.ProxyCreatorFactories;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatcherRate;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatchingSupplier;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.Setting;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatchingInfo.Builder;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods.MatchingMethod;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods.MethodMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods.ParamPermMethodMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.util.Logger;

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
//		  PartlyTypeMatchingInfoFactory factory = new PartlyTypeMatchingInfoFactory( targetType );
		if (sourceType.equals(Object.class)) {
			// Dieser Spezialfall fuehrt ohne diese Sonderregelung in einen Stackoverflow,
			// da Object als Typ immer wieder
			// auftaucht. Es ist also eine Abbruchbedingung.
			return miBuilder.build();
		}

		Method[] queryMethods = getQueryMethods(sourceType);
		Logger.infoF("QueryMethods: %s",
				Stream.of(queryMethods).map(m -> m.getName()).collect(Collectors.joining(", ")));

		// gleicht nur die nicht statischen public-Methods ab
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

	// /**
	// * @param checkType
	// * @param queryType
	// * must be an interface
	// * @return matchende Methoden
	// */
	// public Map<Method, Collection<Method>> getMatchingMethods( Class<?> checkType
	// ) {
	// if ( partlyMatches( checkType ) ) {
	// throw new RuntimeException( "Check-Type does not match Query-Type" );
	// }
	// Method[] queryMethods = getQueryMethods();
	// Map<Method, Collection<Method>> possibleMatches = collectPossibleMatches(
	// queryMethods, checkType.getMethods() );
	// return possibleMatches;
	// }

	// Hier muessen alle Methoden der Klasse Object herausgefiltert werden, weil:
	// 1. ohnehin alle Objekte mit diesen Methoden umgehen koenne
	// 2. ein Interface die dazu passenden Methoden-Signaturen nicht ausweist

	// Einsicht: Das Matching mit Klassen oder Enums als Query-Typ ist etwas
	// kompliziert. Ich beschraenke mich erst einmal
	// nur auf Interfaces als Query-Typ. Das ist auch hinsichtlich meines
	// Anwendungsfalls eher relevant.

	// Weiteres Problem: primitive Typen haben keine Methoden!!!

	// Weiteres Problem: was ist mit package-Sichtbarkeit?
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
		return new MatchingSupplier(supplier,matchingMethods.stream().map(MatchingMethod::getRate).collect(Collectors.toList()));
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
