package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.util.Collection;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.CombinationPartInfo;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.AnalyzationUtils;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;

/**
 * H: blacklist by pivot test calls
 */

public final class MMICombiBlacklistFilter {

	private final Collection<Collection<Integer>> hashCodeBlacklists;

	private final boolean handleAnalyzationUtils;

	private final String info;

	public MMICombiBlacklistFilter(final Collection<Collection<Integer>> checkTypeHCBlacklist, String info) {
		this.hashCodeBlacklists = checkTypeHCBlacklist;
		this.info = " " + info;
		this.handleAnalyzationUtils = true;
	}

	public MMICombiBlacklistFilter(Collection<Collection<Integer>> mmiHCCombiBlacklists,
			boolean handleAnalyzationUtils) {
		this.hashCodeBlacklists = mmiHCCombiBlacklists;
		this.handleAnalyzationUtils = handleAnalyzationUtils;
		this.info = "";
	}


	/**
	 * Filtert nur die {@link CombinationPartInfo} heraus, fuer die in der hashCodeBlacklists HC-Liste mit genau einem Element enthalten sind.
	 * @param infos
	 * @return
	 */
	public Collection<CombinationPartInfo> filterBlacklistedSingleMMI(final Collection<CombinationPartInfo> infos) {
		Collection<Integer> singleBlacklistedHCs = this.hashCodeBlacklists.stream().filter(hcList -> hcList.size() == 1)
				.flatMap(Collection::stream).collect(Collectors.toList());
		if (handleAnalyzationUtils) {
			AnalyzationUtils.filterCount = 0;
		}
		Collection<CombinationPartInfo> filtered = infos.stream()
				.filter(ptmi -> AnalyzationUtils.filterWithAnalyticalCount(
						!singleBlacklistedHCs.contains(ptmi.getMatchingInfo().hashCode())))
				.collect(Collectors.toList());
		if (handleAnalyzationUtils) {
			Logger.infoF("filtered by %s%s: %d", getClass().getSimpleName(), info, AnalyzationUtils.filterCount);
		}
		return filtered;
	}

	public Collection<Collection<CombinationPartInfo>> filterWithNestedCriteria(
			Collection<Collection<CombinationPartInfo>> infos) {
		if (handleAnalyzationUtils) {
			AnalyzationUtils.filterCount = 0;
		}

		Collection<Collection<CombinationPartInfo>> filtered = infos.stream()
				.filter(cpis -> AnalyzationUtils.filterWithAnalyticalCount(!isBlacklistedCombi(cpis)))
				.collect(Collectors.toList());

		if (handleAnalyzationUtils) {
			Logger.infoF("filtered by %s%s: %d", getClass().getSimpleName(), info, AnalyzationUtils.filterCount);
		}
		return filtered;
	}

	private boolean isBlacklistedCombi(Collection<CombinationPartInfo> cpis) {
		Collection<Integer> hcOfCPIs = cpis.stream().map(cpi -> cpi.getMatchingInfo().hashCode())
				.collect(Collectors.toList());
		for (Collection<Integer> hcCombi : this.hashCodeBlacklists) {
			if (hcCombi.stream().allMatch(hcOfCPIs::contains)) {
				return true;
			}
		}
		return false;
	}

}
