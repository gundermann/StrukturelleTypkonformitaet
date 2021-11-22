package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatcherRate;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods.UndefinedMatcherRatingException;

/**
 * Funktionen zur Ermittlung des Matcherratings. In dieser Klasse sind alle 4 Varianten der Funktion <code>rating*</code> enthalten.
 * @author ngundermann
 *
 */
public class MatcherratingFunctions {

	private MatcherratingFunctions() {
	}

	public static double rating(Collection<MatchingInfo> mis) {
		if (mis.size() == 0) {
			throw new UndefinedMatcherRatingException("found zero matcherrateings");
		}

		List<Double> ratings = mis.stream().map(MatchingInfo::getQualitativeMatchRating).flatMap(Collection::stream)
			.map(MatcherRate::getMatcherRating).collect(Collectors.toList());
		return rating1(ratings);
		//Weitere Varianten von rating
//		return rating2(ratings);
//		return rating3(ratings);
//		return rating4(ratings);
	}

	private static double rating1(Collection<Double> ratings) {
		return ratings.stream().reduce(0d, (a, b) -> a + b) / ratings.size();
	}

	@SuppressWarnings("unused")
	private static double rating2(Collection<Double> ratings) {
		return ratings.stream().max(Double::compareTo).get();
	}

	@SuppressWarnings("unused")
	private static double rating3(Collection<Double> ratings) {
		return ratings.stream().min(Double::compareTo).get();
	}

	@SuppressWarnings("unused")
	private static double rating4(Collection<Double> ratings) {
		return (ratings.stream().max(Double::compareTo).get()
				+ ratings.stream().min(Double::compareTo).get())
				/ 2;
	}
}
