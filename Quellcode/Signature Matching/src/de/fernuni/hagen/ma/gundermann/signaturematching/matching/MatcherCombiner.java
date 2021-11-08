package de.fernuni.hagen.ma.gundermann.signaturematching.matching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import de.fernuni.hagen.ma.gundermann.signaturematching.SingleMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.TypeMatcher;

public final class MatcherCombiner {
	private MatcherCombiner() {
	}

	public static Supplier<TypeMatcher> combine(TypeMatcher... matcher) {
		return () -> new TypeMatcher() {

			@Override
			public boolean matchesType(Class<?> checkType, Class<?> queryType) {
				for (TypeMatcher m : getSortedMatcher()) {
					if (m.matchesType(checkType, queryType)) {
						return true;
					}
				}
				return false;
			}

			@Override
			public Collection<SingleMatchingInfo> calculateTypeMatchingInfos(Class<?> checkType, Class<?> queryType) {
				for (TypeMatcher m : getSortedMatcher()) {
					if (m.matchesType(checkType, queryType)) {
						return m.calculateTypeMatchingInfos(checkType, queryType);
					}
				}
				return new ArrayList<>();
			}

			@Override
			public MatcherRate matchesWithRating(Class<?> checkType, Class<?> queryType) {
				for (TypeMatcher m : getSortedMatcher()) {
					MatcherRate rating = m.matchesWithRating(checkType, queryType);
					if (rating != null) {
						return rating;
					}
				}
				return null;
			}

			@Override
			public double getTypeMatcherRate() {
				// irrelevant, weil matchesWithRating ueberschrieben wurde.
				return -0;
			}

			private Collection<TypeMatcher> getSortedMatcher() {
				List<TypeMatcher> matcherList = Arrays.asList(matcher);
				Collections.sort(matcherList,
						(l1, l2) -> Double.compare(l1.getTypeMatcherRate(), l2.getTypeMatcherRate()));
				return matcherList;
			}

		};
	}
}
