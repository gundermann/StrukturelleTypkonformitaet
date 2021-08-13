package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.CombinationInfo;

public interface Selector {

	boolean hasNext();

	Optional<CombinationInfo> getNext();

	void addHigherPotentialType(Class<?> higherPotentialType);

	void addToBlacklist(Class<?> componentInterface);

	void updateByBlacklist(Collection<Integer> combiParts, Collection<Collection<Integer>> methodMatchingInfoHCBlacklist);

	void setBlacklist(Map<Integer,Collection<Collection<Integer>>> component2methodMatchingInfoHCBlacklist);

}
