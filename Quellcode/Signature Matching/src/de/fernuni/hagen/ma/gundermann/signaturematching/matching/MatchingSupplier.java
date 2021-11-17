package de.fernuni.hagen.ma.gundermann.signaturematching.matching;

import java.util.Collection;
import java.util.function.Supplier;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;

public class MatchingSupplier {

	private Supplier<Collection<MethodMatchingInfo>> methodMatchInfosSupplier;

	private Collection<MatcherRate> matcherRatings;

	public MatchingSupplier(Supplier<Collection<MethodMatchingInfo>> supplier, Collection<MatcherRate> matcherRatings) {
		methodMatchInfosSupplier = supplier;
		this.matcherRatings = matcherRatings;
	}

	public Supplier<Collection<MethodMatchingInfo>> getMethodMatchingInfosSupplier() {
		return methodMatchInfosSupplier;
	}

	public Collection<MatcherRate> getMatcherRating() {
		return matcherRatings;
	}

}
