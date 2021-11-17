package de.fernuni.hagen.ma.gundermann.signaturematching.matching.types;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo.ParamPosition;
import de.fernuni.hagen.ma.gundermann.signaturematching.SingleMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.SingleMatchingInfo.Builder;
import de.fernuni.hagen.ma.gundermann.signaturematching.glue.factories.ProxyCreatorFactories;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MethodMatchingInfoCombinator;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.Setting;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods.MethodMatchingInfoFactory;
import de.fernuni.hagen.ma.gundermann.signaturematching.util.Logger;

/**
 * {@link TypeMatcher} fuer Typen, die in einer Vererbungsbeziehung zueinander
 * stehen.
 * 
 * @author Niels Gundermann
 */
public class GenSpecTypeMatcher implements TypeMatcher {

	static int counter = 0;

	Map<Class<?>[], Boolean> cachedGenSpecTypesChecks = new HashMap<>();

	@Override
	public boolean matchesType(Class<?> checkType, Class<?> queryType) {
		Class<?>[] cacheKey = new Class<?>[] { checkType, queryType };
		if (isCombinationCached(cacheKey)) {
			return getResultFromCache(cacheKey);
		}
		cachedGenSpecTypesChecks.put(cacheKey, null);
		boolean result = checkType.equals(Object.class) || queryType.equals(Object.class)
				|| checkType.isAssignableFrom(queryType) || queryType.isAssignableFrom(checkType);
		cachedGenSpecTypesChecks.put(cacheKey, result);
		return result;
	}

	private boolean isCombinationCached(Class<?>[] newCacheKey) {
		for (Class<?>[] cacheKey : cachedGenSpecTypesChecks.keySet()) {
			if (Objects.equals(cacheKey[0], newCacheKey[0]) && Objects.equals(cacheKey[1], newCacheKey[1])
					|| Objects.equals(cacheKey[0], newCacheKey[1]) && Objects.equals(cacheKey[1], newCacheKey[0])) {
				return true;
			}
		}
		return false;
	}

	private boolean getResultFromCache(Class<?>[] newCacheKey) {
		for (Entry<Class<?>[], Boolean> cacheEntries : cachedGenSpecTypesChecks.entrySet()) {
			Class<?>[] cachedKey = cacheEntries.getKey();
			if (Objects.equals(cachedKey[0], newCacheKey[0]) && Objects.equals(cachedKey[1], newCacheKey[1])
					|| Objects.equals(cachedKey[0], newCacheKey[1]) && Objects.equals(cachedKey[1], newCacheKey[0])) {
				return cacheEntries.getValue() == null ? false : cacheEntries.getValue();
			}
		}
		return false;
	}

	@Override
	public Collection<SingleMatchingInfo> calculateTypeMatchingInfos(Class<?> targetType, Class<?> sourceType) {
		Logger.infoF("calculate TypeMatchingInfos: %s -> %s", sourceType, targetType);
		int c = ++counter;
		Logger.infoF("start calculation: %d", c);
		Collection<SingleMatchingInfo> result = new ArrayList<>();
		if (targetType.equals(sourceType)) {
			Logger.infoF("assumtion: %s == %s", sourceType, targetType);
			Logger.infoF("finish calculation: %d", c);
			Builder mibuilder = new SingleMatchingInfo.Builder(sourceType, targetType,
					ProxyCreatorFactories.getIdentityFactoryCreator());
			result = Collections.singletonList(mibuilder.build());
		} else if ((targetType.isPrimitive() && sourceType.equals(Object.class))
				|| (sourceType.isPrimitive() && targetType.equals(Object.class))) {
			Logger.infoF("assumtion: Object > primitiv");
			Logger.infoF("finish calculation: %d", c);

			Builder mibuilder = new SingleMatchingInfo.Builder(sourceType, targetType,
					ProxyCreatorFactories.getIdentityFactoryCreator());
			result = Collections.singletonList(mibuilder.build());
		} else if (sourceType.isAssignableFrom(targetType)) {
			Logger.infoF("assumtion: %s > %s", sourceType, targetType);
			Map<Method, Collection<MethodMatchingInfo>> methodMatchingInfos = createMethodMatchingInfoForGen2SpecMapping(
					sourceType, targetType);

			Builder mibuilder = new SingleMatchingInfo.Builder(sourceType, targetType,
					ProxyCreatorFactories.getClassProxyFactoryCreator());
			if (methodMatchingInfos.isEmpty()) {
				result.add(mibuilder.build());
				return result;
			}
			Logger.infoF("start permutation of MethodMatchingInfos: %d", methodMatchingInfos.values().stream()
					.filter(v -> !v.isEmpty()).map(v -> v.size()).reduce((a, b) -> a * b).orElse(0));
			Collection<Collection<MethodMatchingInfo>> permutedMethodMatches = new MethodMatchingInfoCombinator()
					.generateMethodMatchingCombinations(methodMatchingInfos);
			Logger.infoF("MethodMatches permuted: %d", permutedMethodMatches.size());

			for (Collection<MethodMatchingInfo> combi : permutedMethodMatches) {
				Map<Method, MethodMatchingInfo> m = combi.stream()
						.collect(Collectors.toMap(MethodMatchingInfo::getSource, mmi -> mmi));
				mibuilder.withMethodMatchingInfos(m);
				result.add(mibuilder.build());
			}

		} else if (targetType.isAssignableFrom(sourceType)) {
			Logger.infoF("assumtion: %s < %s", sourceType, targetType);
			Builder mibuilder = new SingleMatchingInfo.Builder(sourceType, targetType,
					ProxyCreatorFactories.getIdentityFactoryCreator());
			result = Collections.singletonList(mibuilder.build());
		}
		Logger.infoF("finish calculation: %d", c);
		return result;
	}

	private Map<Method, Collection<MethodMatchingInfo>> createMethodMatchingInfoForGen2SpecMapping(Class<?> genType,
			Class<?> specType) {
		Map<Method, Collection<MethodMatchingInfo>> matchingInfos = new HashMap<>();
		Collection<Method> genMethods = getOverrideableMethods(genType);
		Method[] specMethods = specType.getDeclaredMethods();
		for (Method specM : specMethods) {
			Method genM = getOriginalMethod(specM, genType);
			if (genM != null) {
				genMethods.remove(genM);
				Logger.infoF("matching methods found: %s === %s", specM, genM);
				MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory(specM, genM);
				// (Kovarianz)
				SingleMatchingInfo returnTypeMatchingInfo = calculateTypeMatchingInfos(genM.getReturnType(),
						specM.getReturnType()).iterator().next();
				// (Kontravarianz)
				Map<ParamPosition, Collection<SingleMatchingInfo>> argumentTypeMatchingInfos = calculateArgumentTypesMatchingInfos(
						specM.getParameterTypes(), genM.getParameterTypes());
				matchingInfos.put(genM,

						factory.createFromTypeMatchingInfos(Collections.singletonList(returnTypeMatchingInfo),
								Collections.singletonList(argumentTypeMatchingInfos)));
			}
		}

		for (Method genM : genMethods) {
			MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory(genM, genM);
			matchingInfos.put(genM, Collections.singletonList(factory.create(null, new HashMap<>())));
		}

		return matchingInfos;
	}

	private Map<ParamPosition, Collection<SingleMatchingInfo>> calculateArgumentTypesMatchingInfos(Class<?>[] checkATs,
			Class<?>[] queryATs) {
		Map<ParamPosition, Collection<SingleMatchingInfo>> matchingMap = new HashMap<>();
		for (int i = 0; i < checkATs.length; i++) {
			Class<?> checkAT = checkATs[i];
			Class<?> queryAT = queryATs[i];
			Collection<SingleMatchingInfo> infos = calculateTypeMatchingInfos(checkAT, queryAT);
			matchingMap.put(new ParamPosition(i, i), infos);
		}

		return matchingMap;
	}

	private Collection<Method> getOverrideableMethods(Class<?> genType) {
		Method[] methods = genType.getDeclaredMethods();
		return Stream.of(methods).filter(m -> !Modifier.isPrivate(m.getModifiers())).collect(Collectors.toList());
	}

	public static Method getOriginalMethod(final Method myMethod, Class<?> superType) {
		if (superType.equals(Object.class)) {
			return null;
		}
		try {
			Method superMethod = superType.getDeclaredMethod(myMethod.getName(), myMethod.getParameterTypes());
			return !Modifier.isPrivate(superMethod.getModifiers()) ? superMethod : null;
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	@Override
	public double getTypeMatcherRate() {
		return Setting.GEN_SPEC_TYPE_MATCHER_BASE_RATING;
	}

}
