package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.CombinationSelector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.NoneSelector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.SingleSelector;
import matching.methods.MethodMatchingInfo;
import matching.modules.PartlyTypeMatchingInfo;

public class BestMatchingComponentCombinationFinder {

  private final Selector combinationSelector;

  private final Selector singleSelector;

  private final List<PartlyTypeMatchingInfo> quantitativeSortedInfos;

  private Optional<CombinationInfo> nextCombinationInfo = Optional.empty();

  BestMatchingComponentCombinationFinder(
      Map<Class<?>, PartlyTypeMatchingInfo> componentInterface2PartlyMatchingInfos ) {
    quantitativeSortedInfos = new ArrayList<>(
        componentInterface2PartlyMatchingInfos.values() );
    this.combinationSelector = new CombinationSelector( quantitativeSortedInfos );
    this.singleSelector = new SingleSelector( quantitativeSortedInfos );
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
    if ( singleSelector.hasNext() ) {
      return singleSelector;
    }
    if ( combinationSelector.hasNext() ) {
      return combinationSelector;
    }
    return new NoneSelector();
  }

  public void optimizeForCurrentCombination() {
    if ( nextCombinationInfo.isPresent() ) {
      nextCombinationInfo.get().getComponentClasses().forEach( cc -> {
        this.combinationSelector.addHigherPotentialType( cc );
        this.singleSelector.addHigherPotentialType( cc );
      } );
    }

  }

  public void optimizeMatchingInfoBlacklist( MethodMatchingInfo methodMatchingInfo ) {
    combinationSelector.addToBlacklist( methodMatchingInfo );
    singleSelector.addToBlacklist( methodMatchingInfo );
  }

  public void optimizeCheckTypeBlacklist( Class<?> componentInterface ) {
    combinationSelector.addToBlacklist( componentInterface );
    singleSelector.addToBlacklist( componentInterface );
  }

}
