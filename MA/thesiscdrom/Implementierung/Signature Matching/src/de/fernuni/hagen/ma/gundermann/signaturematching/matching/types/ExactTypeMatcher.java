package de.fernuni.hagen.ma.gundermann.signaturematching.matching.types;

import java.util.Collection;
import java.util.Collections;

import de.fernuni.hagen.ma.gundermann.signaturematching.SingleMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.glue.factories.ProxyCreatorFactories;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.Setting;

/**
 * {@link TypeMatcher} fuer identische Typen
 * 
 * @author Niels Gundermann
 *
 */
public class ExactTypeMatcher implements TypeMatcher {

	@Override
	public boolean matchesType(Class<?> checkType, Class<?> queryType) {
		return checkType.equals(queryType);
	}

	@Override
	public Collection<SingleMatchingInfo> calculateTypeMatchingInfos(Class<?> targetType, Class<?> sourceType) {
		if (matchesType(targetType, sourceType)) {
			return Collections.singletonList(new SingleMatchingInfo.Builder(sourceType, targetType,
					ProxyCreatorFactories.getIdentityFactoryCreator()).build());
		}
		return Collections.emptyList();
	}

	@Override
	public double getTypeMatcherRate() {
		return Setting.EXACT_TYPE_MATCHER_RATING;
	}

}
