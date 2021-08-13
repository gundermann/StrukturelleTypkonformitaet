package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.AnalyzationUtils;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.CollectionUtil;

public class Combinator<K, V> {

	public static <T> Collection<Collection<T>> generateCombis(Collection<T> base, int combinatedElems) {
		if (combinatedElems == 1) {
			return base.stream().map(Collections::singletonList).map(ArrayList::new).collect(Collectors.toList());
		}

		Collection<T> restBase = new ArrayList<>(base);
		Collection<Collection<T>> result = new ArrayList<>();
		for (int i1 = 0; i1 < base.size(); i1++) {
			T elem = CollectionUtil.get(base, i1).get();
			restBase.remove(elem);
			Collection<Collection<T>> generateCombis = new ArrayList<>();
			for (int combiIteration = combinatedElems - 1; combiIteration > 0; combiIteration--) {
				generateCombis.addAll(generateCombis(restBase, combiIteration));
			}
			for (Collection<T> combi : generateCombis) {
				combi.add(elem);
			}
			result.addAll(generateCombis);
		}
		return result;

	}

	public Combinator() {
		AnalyzationUtils.filterCount = 0;
	}

	public Collection<Collection<CombinationPartInfo>> generateCombis(
			Map<K, Collection<CombinationPartInfo>> possibleMethodMatches,
			Collection<Collection<Integer>> blacklistedMMICombis) {
		Collection<Integer> singleBlacklistedHC = blacklistedMMICombis.stream().filter(l -> l.size() == 1)
				.flatMap(Collection::stream).collect(Collectors.toList());
		Collection<Collection<CombinationPartInfo>> combinations = new ArrayList<>();
		// erste Methode holen
		Iterator<K> iterator = possibleMethodMatches.keySet().iterator();
		if (!iterator.hasNext()) {
			// keine Methode mehr übrig
			return combinations;
		}
		K selectedMethod = iterator.next();
		// possibleMethodMatches abbauen
		// Kopie der ursprünglichen Map erstellen
		Map<K, Collection<CombinationPartInfo>> localMethodMatches = new HashMap<>(possibleMethodMatches);
		Collection<CombinationPartInfo> selectedValues = localMethodMatches.remove(selectedMethod);
		if (selectedValues.isEmpty()) {
			return generateCombis(localMethodMatches, blacklistedMMICombis);
		}
		for (CombinationPartInfo value : selectedValues) {
			if (singleBlacklistedHC.contains(value.getMatchingInfo().hashCode())) {
				AnalyzationUtils.filterCount++;
				continue;
			}
			Collection<Collection<Integer>> relevantBlacklist = blacklistedMMICombis.stream()
					.filter(hcList -> hcList.contains(value.getMatchingInfo().hashCode()))
					.map(hcList -> removeSelectedHC(hcList, value.getMatchingInfo().hashCode()))
					.collect(Collectors.toList());
			Collection<Collection<CombinationPartInfo>> otherCombinations = generateCombis(localMethodMatches,
					relevantBlacklist);
			if (otherCombinations.isEmpty()) {
				Collection<CombinationPartInfo> singleInfos = new ArrayList<>();
				singleInfos.add(value);
				combinations.add(singleInfos);
				continue;
			}
			for (Collection<CombinationPartInfo> otherInfos : otherCombinations) {
				otherInfos.add(value);
			}
			combinations.addAll(otherCombinations);
		}
		return combinations;
	}

	private Collection<Integer> removeSelectedHC(Collection<Integer> hcList, Integer selectedHC) {
		Collection<Integer> newList = new ArrayList<Integer>(hcList);
		newList.remove(selectedHC);
		return newList;
	}

}
