package de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatcherRate;

/**
 * Pool fuer die verschiedenen Varianten vom mRating
 * 
 * @author Niels Gundermann
 *
 */
public final class MethodDelegationRatingFunctions {

	private MethodDelegationRatingFunctions() {
	}

	public static double mRating(Collection<MatcherRate> rates) {
		if (rates.size() == 0) {
			throw new UndefinedMatcherRatingException("found zero matcherrateings");
		}
		List<Double> ratings = rates.stream().map(MatcherRate::getMatcherRating).collect(Collectors.toList());
		return mRating1(ratings);
//		return mRating2(ratings);
//		return mRating3(ratings);
//		return mRating4(ratings);
	}

	private static double mRating1(Collection<Double> ratings) {
		return ratings.stream().reduce(0d, (a, b) -> a + b) / ratings.size();
	}

	@SuppressWarnings("unused")
	private static double mRating2(Collection<Double> ratings) {
		return ratings.stream().max(Double::compareTo).get();
	}

	@SuppressWarnings("unused")
	private static double mRating3(Collection<Double> ratings) {
		return ratings.stream().min(Double::compareTo).get();
	}

	@SuppressWarnings("unused")
	private static double mRating4(Collection<Double> ratings) {
		return (ratings.stream().max(Double::compareTo).get() + ratings.stream().min(Double::compareTo).get()) / 2;
	}

}
