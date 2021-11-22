package de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods;

import java.lang.reflect.Method;
import java.util.Collection;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatcherCombiner;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatcherRate;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.ExactTypeMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.GenSpecTypeMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.ContainerTypeMatcher;

public class CombinedMethodMatcher implements MethodMatcher {

	ExactTypeMatcher exactTypeMatcher = new ExactTypeMatcher();

	GenSpecTypeMatcher genSpecTypeMatcher = new GenSpecTypeMatcher();

	MethodMatcher combination = new ParamPermMethodMatcher(MatcherCombiner.combine(genSpecTypeMatcher, exactTypeMatcher,
			new ContainerTypeMatcher(MatcherCombiner.combine(genSpecTypeMatcher, exactTypeMatcher))));

	@Override
	public boolean matches(Method checkMethod, Method queryMethod) {
		return combination.matches(checkMethod, queryMethod);
	}

	@Override
	public Collection<MethodMatchingInfo> calculateMatchingInfos(Method checkMethod, Method queryMethod) {
		return combination.calculateMatchingInfos(checkMethod, queryMethod);
	}

	@Override
	public MatcherRate matchesWithRating(Method checkMethod, Method queryMethod) {
		return combination.matchesWithRating(checkMethod, queryMethod);
	}

}
