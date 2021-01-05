package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.CommonSelector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.QuantitaiveMatchingInfoComparator;
import matching.modules.PartlyTypeMatchingInfo;

public class BestMatchingComponentCombinationFinder {

  private final Selector commonSelector;

  private final List<PartlyTypeMatchingInfo> quantitativeSortedInfos;

  private Optional<CombinationInfo> nextCombinationInfo = Optional.empty();

  BestMatchingComponentCombinationFinder(
      Map<Class<?>, PartlyTypeMatchingInfo> componentInterface2PartlyMatchingInfos ) {
    quantitativeSortedInfos = new ArrayList<>(
        componentInterface2PartlyMatchingInfos.values() );
    Collections.sort( quantitativeSortedInfos, new QuantitaiveMatchingInfoComparator() );
    this.commonSelector = new CommonSelector( quantitativeSortedInfos );
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
    if ( commonSelector.hasNext() ) {
      return commonSelector;
    }
    return new NoneSelector();
  }

  public void optimzeForCurrentCombination() {
    if ( nextCombinationInfo.isPresent() ) {
      nextCombinationInfo.get().getComponentClasses().forEach( this.commonSelector::addHigherPotentialType );
    }

  }

}
