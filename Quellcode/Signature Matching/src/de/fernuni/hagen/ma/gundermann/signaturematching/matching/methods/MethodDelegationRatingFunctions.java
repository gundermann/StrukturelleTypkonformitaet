package de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatcherRate;

public final class MethodDelegationRatingFunctions {

	private MethodDelegationRatingFunctions() {
	}

	
	public static double mdRating(Collection<MatcherRate> rates) {
		if (rates.size() == 0) {
			throw new UndefinedMatcherRatingException("found zero matcherrateings");
		}
		List<Double> ratings = rates.stream().map(MatcherRate::getMatcherRating).collect(Collectors.toList());
		return mdRating1(ratings);
//		return mdRating2(ratings);
//		return mdRating3(ratings);
//		return mdRating4(ratings);
	}

	
	private static double mdRating1(Collection<Double> ratings) {
		return ratings.stream().reduce(0d, (a, b) -> a + b) / ratings.size();
	}

	private static double mdRating2(Collection<Double> ratings) {
		return ratings.stream().max(Double::compareTo).get();
	}

	private static double mdRating3(Collection<Double> ratings) {
		return ratings.stream().min(Double::compareTo).get();
	}

	private static double mdRating4(Collection<Double> ratings) {
		return (ratings.stream().max(Double::compareTo).get()
				+ ratings.stream().min(Double::compareTo).get()) / 2;
	}

}
