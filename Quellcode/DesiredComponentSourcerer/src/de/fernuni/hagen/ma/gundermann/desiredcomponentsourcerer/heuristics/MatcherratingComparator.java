package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.util.Collection;
import java.util.Comparator;

import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatchingInfo;

public class MatcherratingComparator implements Comparator<Collection<MatchingInfo>> {

	@Override
	public int compare(Collection<MatchingInfo> mi1, Collection<MatchingInfo> mi2) {

		if (mi1 == null && mi2 == null) {
			return 0;
		}
		if (mi2 == null) {
			return 1;
		}
		if (mi1 == null) {
			return -1;
		}

		Double ranking1 = MatcherratingFunctions.rating(mi1);
		Double ranking2 = MatcherratingFunctions.rating(mi2);

		return Double.compare(ranking1, ranking2);
	}

}
