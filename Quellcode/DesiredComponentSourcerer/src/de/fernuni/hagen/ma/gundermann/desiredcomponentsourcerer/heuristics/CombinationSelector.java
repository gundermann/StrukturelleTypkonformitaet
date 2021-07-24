package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.Heuristic;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.InfoCollector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.Selector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.CombinationFinderUtils;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.CombinationInfo;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.CombinationPartInfo;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.Combinator;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.AnalyzationUtils;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.CollectionUtil;
import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.util.Logger;

/**
 * Selektor für die Kombination von Komponenten, sodass alle erwarteten Methoden
 * bedient werden.
 */
public class CombinationSelector implements Selector {

	private final List<MatchingInfo> infos;

	private int combinatiedComponentCount = 1;

	private Collection<Collection<CombinationPartInfo>> cachedCalculatedInfos = new ArrayList<>();

	private List<Collection<MatchingInfo>> cachedMatchingInfoCombinations = new ArrayList<>();

	private final Collection<Method> originalMethods;

	private final Collection<Class<?>> higherPotentialTypes = new ArrayList<>();

	// H: blacklist by pivot test calls
	private final Collection<Collection<Integer>> methodMatchingInfoHCBlacklist = new ArrayList<>();

	// H: blacklist if no implementation available
	private final Collection<Integer> checkTypeHCBlacklist = new ArrayList<>();

	private final Collection<Heuristic> usedHeuristics;

	public CombinationSelector(List<MatchingInfo> infos, Collection<Heuristic> usedHeuristics) {
		this.infos = infos;
		this.originalMethods = infos.stream().findFirst().map(MatchingInfo::getMatchedSourceMethods)
				.orElse(Collections.emptyList());
		this.usedHeuristics = usedHeuristics;
	}

	@Override
	public boolean hasNext() {
		return !cachedMatchingInfoCombinations.isEmpty() || !cachedCalculatedInfos.isEmpty()
				|| combinatiedComponentCount < infos.size();
	}

	@Override
	public Optional<CombinationInfo> getNext() {
		while (cachedCalculatedInfos.isEmpty() && hasNext()) {
			if (cachedMatchingInfoCombinations.isEmpty()) {
				combinatiedComponentCount++;
				collectRelevantMatchingInfoCombinations();
				return getNext();
			}

			Map<Method, Collection<MatchingInfo>> relevantTypeMatchingInfos = collectRelevantInfosPerMethod();
			if (!relevantTypeMatchingInfos.isEmpty()) {
				fillCachedComponent2MatchingInfo(relevantTypeMatchingInfos);
				return getNext();
			}
		}
		InfoCollector.incrementCreatedProxiesInIterationStep(combinatiedComponentCount);
		return Optional.of(new CombinationInfo(CollectionUtil.pop(cachedCalculatedInfos)));
	}

	private void collectRelevantMatchingInfoCombinations() {
		// H: blacklist if no implementation available + BL_NMC (muss nicht abgefragt
		// werden, weil die fehlende Implementierungen ohnehin heruasgefiltert werden.
		Collection<MatchingInfo> relevantInfos = new CheckTypeBlacklistFilter(this.checkTypeHCBlacklist)
				.filter(infos);

		cachedMatchingInfoCombinations = new ArrayList<>(
				Combinator.generateCombis(relevantInfos, combinatiedComponentCount));

		// H: combinate low matcher rating first
		// sort by matcher rate
		if (usedHeuristics.contains(Heuristic.LMF)) {
			Collections.sort(cachedMatchingInfoCombinations, new AccumulatedMatchingRateComparator());
		}

		// H: combinate passed tests components first
		// re-organize cache with respect to search optimization
		if (usedHeuristics.contains(Heuristic.PTTF)) {
			Collections.sort(cachedMatchingInfoCombinations,
					new HigherPotentialTypesFirstComparator(higherPotentialTypes));
		}
		InfoCollector.addCominationCountInIteration(this.cachedMatchingInfoCombinations.size(), combinatiedComponentCount );
	}

	private Map<Method, Collection<MatchingInfo>> collectRelevantInfosPerMethod() {
		Collection<MatchingInfo> combi = CollectionUtil.pop(cachedMatchingInfoCombinations);
		Map<Method, Collection<MatchingInfo>> matchingInfoPerMethod = getMatchingInfoPerMethod(combi);
		// Pruefen, ob auch alle erwarteten Methoden erfuellt wurden.
		if (!matchingInfoPerMethod.keySet().containsAll(originalMethods)) {
			return new HashMap<>();
		}
		return matchingInfoPerMethod;
	}

	private void fillCachedComponent2MatchingInfo(Map<Method, Collection<MatchingInfo>> typeMatchingInfos) {
		Map<Method, Collection<CombinationPartInfo>> combiPartInfos = CombinationFinderUtils
				.transformToCombinationPartInfosPerMethod(typeMatchingInfos, this.methodMatchingInfoHCBlacklist);
		this.cachedCalculatedInfos = new Combinator<Method, CombinationPartInfo>().generateCombis(combiPartInfos,
				this.methodMatchingInfoHCBlacklist);
		Logger.infoF("COMBIFILTER: filtered combinations > %d", AnalyzationUtils.filterCount);

		this.cachedCalculatedInfos = this.cachedCalculatedInfos.stream().filter(new SelfCombinatedPartFilter())
				.collect(Collectors.toList());
	}

	private Map<Method, Collection<MatchingInfo>> getMatchingInfoPerMethod(
			Collection<MatchingInfo> relevantInfos) {
		Map<Method, Collection<MatchingInfo>> relevantTypeMatchingInfos = new HashMap<>();
		for (MatchingInfo info : relevantInfos) {
			Collection<Method> methodsWithMatchingInfo = info.getMethodMatchingInfoSupplier().keySet();
			for (Method m : methodsWithMatchingInfo) {
				relevantTypeMatchingInfos.compute(m, CollectionUtil.remapping_addToValueCollection(info));
			}
		}
		return relevantTypeMatchingInfos;
	}

	@Override
	public void addHigherPotentialType(Class<?> higherPotentialType) {
		this.higherPotentialTypes.add(higherPotentialType);
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
		this.checkTypeHCBlacklist.add(componentInterface.hashCode());

		// update cache
		cachedCalculatedInfos = new CheckTypeBlacklistFilter(this.checkTypeHCBlacklist)
				.filterWithNestedCriteria(cachedCalculatedInfos);
	}
}
