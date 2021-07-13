package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.Heuristic;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.Selector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.CombinationFinderUtils;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.CombinationInfo;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.CombinationPartInfo;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.Combinator;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.AnalyzationUtils;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.CollectionUtil;
import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import matching.MatchingInfo;
import util.Logger;

/**
 * Selektor für strukturell 100%ig matchende Komponenten
 */
public class SingleSelector implements Selector {

	private List<Collection<MatchingInfo>> infos;

	private int selectedIndex = -1;

	private Collection<Collection<CombinationPartInfo>> cachedCalculatedInfos = new ArrayList<>();

	// H: blacklist by pivot test calls
	private final Collection<Collection<Integer>> methodMatchingInfoHCBlacklist = new ArrayList<>();
	

	public SingleSelector(List<MatchingInfo> infos, Collection<Heuristic> usedHeuristics) {
		Stream<List<MatchingInfo>> stream = infos.stream()
				.filter(CombinationFinderUtils::isFullMatchingComponent).map(i -> Collections.singletonList(i));
		if (usedHeuristics.contains(Heuristic.LMF)) {
			// H: respect to Matching Rate
			stream = stream.sorted(new AccumulatedMatchingRateComparator());
		}
		this.infos = stream.collect(Collectors.toList());
	}

	@Override
	public boolean hasNext() {
		return infos.size() > selectedIndex + 1 || !cachedCalculatedInfos.isEmpty();
	}

	@Override
	public Optional<CombinationInfo> getNext() {
		if (cachedCalculatedInfos.isEmpty()) {
			selectedIndex++;
			MatchingInfo info = infos.get(selectedIndex).iterator().next();
			Map<Method, Collection<MatchingInfo>> relevantTypeMatchingInfos = getMatchingInfoPerMethod(info);
			fillCachedComponent2MatchingInfo(relevantTypeMatchingInfos);
		}
		return Optional.of(new CombinationInfo(CollectionUtil.pop(cachedCalculatedInfos)));
	}

	private void fillCachedComponent2MatchingInfo(Map<Method, Collection<MatchingInfo>> typeMatchingInfos) {
		Map<Method, Collection<CombinationPartInfo>> combiPartInfos = CombinationFinderUtils
				.transformToCombinationPartInfosPerMethod(typeMatchingInfos, this.methodMatchingInfoHCBlacklist);
		this.cachedCalculatedInfos = new Combinator<Method, CombinationPartInfo>().generateCombis(combiPartInfos, this.methodMatchingInfoHCBlacklist);
		Logger.infoF("COMBIFILTER: filtered combinations > %d", AnalyzationUtils.filterCount);
	}

	private Map<Method, Collection<MatchingInfo>> getMatchingInfoPerMethod(MatchingInfo info) {
		Map<Method, Collection<MatchingInfo>> relevantTypeMatchingInfos = new HashMap<>();
		Collection<Method> methodsWithMatchingInfo = info.getMethodMatchingInfoSupplier().keySet();
		for (Method m : methodsWithMatchingInfo) {
			relevantTypeMatchingInfos.put(m, Collections.singletonList(info));
		}
		return relevantTypeMatchingInfos;
	}

	@Override
	public void addHigherPotentialType(Class<?> higherPotentialType) {
		// irrelevant fuer diesen Selector

	}

	@Override
	public void addToBlacklist(Collection<Collection<MethodMatchingInfo>> mmiCombis) {
		// H: blacklist by pivot test calls
		// H: blacklist failed single methods tested

		for (Collection<MethodMatchingInfo> mmiCombi : mmiCombis) {
			List<Integer> hcCol = mmiCombi.stream().map(mmi -> mmi.hashCode()).collect(Collectors.toList());
			this.methodMatchingInfoHCBlacklist.add(hcCol);
		}
		cachedCalculatedInfos = new MMICombiBlacklistFilter(this.methodMatchingInfoHCBlacklist, "update")
				.filterWithNestedCriteria(cachedCalculatedInfos);
	}

	@Override
	public void addToBlacklist(Class<?> componentInterface) {
		// H: blacklist if no implementation available
		// update cache
		cachedCalculatedInfos = new CheckTypeBlacklistFilter(Arrays.asList(componentInterface.hashCode()), "update")
				.filterWithNestedCriteria(cachedCalculatedInfos);
	}

}
