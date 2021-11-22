package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.Heuristic;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.Selector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.CombinationSelector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.NoneSelector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.SingleSelector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatchingInfo;

public class BestMatchingComponentCombinationFinder {

	private final Selector[] selectors = new Selector[2];

	private int currentSelectorIndex = 0;

	private final List<MatchingInfo> quantitativeSortedInfos;

	private Optional<CombinationInfo> nextCombinationInfo = Optional.empty();

	private Map<Integer, Collection<Collection<Integer>>> methodMatchingInfoHCBlacklist = new HashMap<>();

	public BestMatchingComponentCombinationFinder(Map<Class<?>, MatchingInfo> componentInterface2PartlyMatchingInfos,
			Collection<Heuristic> usedHeuristics) {
		quantitativeSortedInfos = new ArrayList<>(componentInterface2PartlyMatchingInfos.values());
		this.selectors[0] = new SingleSelector(quantitativeSortedInfos, usedHeuristics);
		this.selectors[1] = new CombinationSelector(quantitativeSortedInfos, usedHeuristics);
		Logger.infoF("use selector: %s", selectors[0].getClass().getSimpleName());
	}

	public boolean hasNextCombination() {
		return getSelectorForNextCombination().hasNext();
	}

	public CombinationInfo getNextCombination() {
		generateNextCombination();
		return nextCombinationInfo.get();
	}

	private void generateNextCombination() {
		Selector selector = getSelectorForNextCombination();
		nextCombinationInfo = selector.getNext();
	}

	private Selector getSelectorForNextCombination() {
		if (selectors[currentSelectorIndex].hasNext()) {
			return selectors[currentSelectorIndex];
		} else if (currentSelectorIndex < selectors.length - 1) {
			currentSelectorIndex++;
			Logger.infoF("use selector: %s", selectors[currentSelectorIndex].getClass().getSimpleName());
			Logger.infoF("Setup blacklist for selector with %d blacklistings", methodMatchingInfoHCBlacklist.size());
			selectors[currentSelectorIndex].setBlacklist(methodMatchingInfoHCBlacklist);
			return getSelectorForNextCombination();
		}
		return new NoneSelector();
	}

	public void optimizeForCurrentCombination() {
		nextCombinationInfo.ifPresent(ci -> ci.getComponentClasses().forEach(cc -> {
			for (int i = currentSelectorIndex; i < selectors.length; i++) {
				selectors[i].addHigherPotentialType(cc);
			}
		}));

	}

	public void optimizeMatchingInfoBlacklist(Collection<Class<?>> combiParts,
			Collection<Collection<MethodMatchingInfo>> collection) {
		Collection<Collection<Integer>> hcCombis = new ArrayList<Collection<Integer>>();
		for (Collection<MethodMatchingInfo> mmiCombi : collection) {
			List<Integer> hcList = mmiCombi.stream().map(mmi -> mmi.hashCode()).collect(Collectors.toList());
			hcCombis.add(hcList);
		}
		for (Class<?> combiPart : combiParts) {
			Collection<Collection<Integer>> hcs = this.methodMatchingInfoHCBlacklist.getOrDefault(combiPart.hashCode(),
					new ArrayList<Collection<Integer>>());
			hcs.addAll(hcCombis);
			this.methodMatchingInfoHCBlacklist.put(combiPart.hashCode(), hcs);
		}
		selectors[currentSelectorIndex]
				.updateByBlacklist(combiParts.stream().map(Object::hashCode).collect(Collectors.toList()), hcCombis);
	}

	public void optimizeCheckTypeBlacklist(Class<?> componentInterface) {
		for (int i = currentSelectorIndex; i < selectors.length; i++) {
			selectors[i].addToBlacklist(componentInterface);
		}
	}

}
