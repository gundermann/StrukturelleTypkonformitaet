package de.fernuni.hagen.ma.gundermann.signaturematching.matching.types;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fernuni.hagen.ma.gundermann.signaturematching.SingleMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.SingleMatchingInfo.Builder;
import de.fernuni.hagen.ma.gundermann.signaturematching.glue.factories.ProxyCreatorFactories;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.Setting;

/**
 * {@link TypeMatcher} fuer Typen, die ein Feld enthalten, welches mit dem
 * anderen Typen gematcht werden kann.
 * 
 * @author Niels Gundermann
 *
 */
public class WrappedTypeMatcher implements TypeMatcher {

	final Map<Class<?>[], Boolean> cachedWrappedTypeChecks = new HashMap<>();

	private final Supplier<TypeMatcher> innerMethodMatcherSupplier;

	public WrappedTypeMatcher(Supplier<TypeMatcher> innerMethodMatcherSupplier) {
		this.innerMethodMatcherSupplier = innerMethodMatcherSupplier;
	}

	@Override
	public boolean matchesType(Class<?> t1, Class<?> t2) {
		return isWrappedIn(t1, t2) || isWrappedIn(t2, t1);
	}

	boolean matchesWrappedOneDirection(Class<?> t1, Class<?> t2) {
		return isWrappedIn(t1, t2);
	}

	private boolean isWrappedIn(Class<?> checkType, Class<?> queryType) {
		Class<?>[] cacheKey = new Class<?>[] { checkType, queryType };
		if (isCombinationCached(cacheKey)) {
			return getResultFromCache(cacheKey);
		}
		cachedWrappedTypeChecks.put(cacheKey, null);
		boolean result = containsFieldWithType(queryType, checkType);
		cachedWrappedTypeChecks.put(cacheKey, result);
		return result;

	}

	private boolean getResultFromCache(Class<?>[] newCacheKey) {
		for (Entry<Class<?>[], Boolean> cacheEntries : cachedWrappedTypeChecks.entrySet()) {
			Class<?>[] cachedKey = cacheEntries.getKey();
			if (Objects.equals(cachedKey[0], newCacheKey[0]) && Objects.equals(cachedKey[1], newCacheKey[1])) {
				return cacheEntries.getValue() == null ? false : cacheEntries.getValue();
			}
		}
		return false;
	}

	private boolean isCombinationCached(Class<?>[] newCacheKey) {
		for (Class<?>[] cacheKeys : cachedWrappedTypeChecks.keySet()) {
			if (Objects.equals(cacheKeys[0], newCacheKey[0]) && Objects.equals(cacheKeys[1], newCacheKey[1])) {
				return true;
			}
		}
		return false;
	}

	private boolean containsFieldWithType(Class<?> wrapperClass, Class<?> wrappedType) {
		Field[] fieldsOfWrapper = filterStaticFields(wrapperClass.getDeclaredFields());
		for (Field field : fieldsOfWrapper) {
			if (innerMethodMatcherSupplier.get().matchesType(field.getType(), wrappedType)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Collection<SingleMatchingInfo> calculateTypeMatchingInfos(Class<?> checkType, Class<?> queryType) {
		Collection<SingleMatchingInfo> allMatchingInfos = new ArrayList<>();

		TypeMatcher innerMethodMatcher = innerMethodMatcherSupplier.get();
		if (innerMethodMatcher.matchesType(checkType, queryType)) {
			Collection<SingleMatchingInfo> matchingInfos = innerMethodMatcher.calculateTypeMatchingInfos(checkType,
					queryType);
			allMatchingInfos.addAll(matchingInfos);
		}
		if (isWrappedIn(checkType, queryType)) {
			Collection<SingleMatchingInfo> matchingInfos = calculateWrappedTypeMatchingInfos(queryType, checkType,
					false);
			allMatchingInfos.addAll(matchingInfos);
		}
		if (isWrappedIn(queryType, checkType)) {
			Collection<SingleMatchingInfo> matchingInfos = calculateWrappedTypeMatchingInfos(checkType, queryType,
					true);
			allMatchingInfos.addAll(matchingInfos);
		}
		return allMatchingInfos;
	}

	private Collection<SingleMatchingInfo> calculateWrappedTypeMatchingInfos(Class<?> wrapperClass,
			Class<?> wrappedType, boolean isTargetWrapper) {
		Collection<SingleMatchingInfo> allMatchingInfos = new ArrayList<>();
		Field[] fieldsOfWrapper = filterStaticFields(wrapperClass.getDeclaredFields());
		for (Field field : fieldsOfWrapper) {
			Collection<SingleMatchingInfo> infosFromInnerMatcher = new ArrayList<>();
			SingleMatchingInfo.Builder mibuilder = null;

			if (isTargetWrapper) {
				infosFromInnerMatcher = calcInnerMatchingInfos(field.getType(), wrappedType);
				mibuilder = new Builder(wrappedType, wrapperClass,
						ProxyCreatorFactories.getWrapperFactoryCreator(field.getName()));
			} else {
				infosFromInnerMatcher = calcInnerMatchingInfos(wrappedType, field.getType());
				mibuilder = new Builder(wrapperClass, wrappedType,
						ProxyCreatorFactories.getWrappedFactoryCreator(field.getName()));
			}
			allMatchingInfos.addAll(enhanceInfosWithDelegate(infosFromInnerMatcher, mibuilder));
		}
		return allMatchingInfos;
	}

	private Collection<SingleMatchingInfo> calcInnerMatchingInfos(Class<?> checkType, Class<?> queryType) {
		TypeMatcher innerMethodMatcher = innerMethodMatcherSupplier.get();
		if (innerMethodMatcher.matchesType(checkType, queryType)) {
			return innerMethodMatcher.calculateTypeMatchingInfos(checkType, queryType);
		}
		return new ArrayList<>();
	}

	private Field[] filterStaticFields(Field[] declaredFields) {
		return Stream.of(declaredFields).filter(f -> !Modifier.isStatic(f.getModifiers())).collect(Collectors.toList())
				.toArray(new Field[] {});
	}

	private Collection<SingleMatchingInfo> enhanceInfosWithDelegate(Collection<SingleMatchingInfo> infos,
			Builder builder) {
		Collection<SingleMatchingInfo> enhancedInfos = new ArrayList<>();
		for (SingleMatchingInfo mmi : infos) {
			builder.withMethodMatchingInfos(mmi.getMethodMatchingInfos());
			enhancedInfos.add(builder.build());
		}
		return enhancedInfos;
	}

	@Override
	public double getTypeMatcherRate() {
		return Setting.WRAPPEN_TYPE_MATCHER_BASE_RATING;
	}

}
