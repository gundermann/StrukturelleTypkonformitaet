package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.Selector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.CombinationInfo;

/**
 * Selektor, der gar keine Komponente selektiert
 */
public class NoneSelector implements Selector {

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public Optional<CombinationInfo> getNext() {
		return Optional.empty();
	}

	@Override
	public void addHigherPotentialType(Class<?> higherPotentialType) {
		// do nothing
	}

	@Override
	public void addToBlacklist(Class<?> componentInterface) {
		// do nothing
	}

	@Override
	public void updateByBlacklist(Collection<Integer> combiParts,
			Collection<Collection<Integer>> methodMatchingInfoHCBlacklist) {
		// do nothing
	}

	@Override
	public void setBlacklist(Map<Integer, Collection<Collection<Integer>>> component2methodMatchingInfoHCBlacklist) {
		// do nothing
	}


}
