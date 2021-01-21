package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.Selector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.CombinationSelector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.NoneSelector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.SingleSelector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
import matching.methods.MethodMatchingInfo;
import matching.modules.PartlyTypeMatchingInfo;

public class BestMatchingComponentCombinationFinder {

  private final Selector[] selectors = new Selector[2];

  private int currentSelectorIndex = 0;

  private final List<PartlyTypeMatchingInfo> quantitativeSortedInfos;

  private Optional<CombinationInfo> nextCombinationInfo = Optional.empty();

  public BestMatchingComponentCombinationFinder(
      Map<Class<?>, PartlyTypeMatchingInfo> componentInterface2PartlyMatchingInfos ) {
    quantitativeSortedInfos = new ArrayList<>(
        componentInterface2PartlyMatchingInfos.values() );
    this.selectors[0] = new SingleSelector( quantitativeSortedInfos );
    this.selectors[1] = new CombinationSelector( quantitativeSortedInfos );
    Logger.infoF( "use selector: %s", selectors[0].getClass().getSimpleName() );
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
    if ( selectors[currentSelectorIndex].hasNext() ) {
      return selectors[currentSelectorIndex];
    }
    else if ( currentSelectorIndex < selectors.length - 1 ) {
      currentSelectorIndex++;
      Logger.infoF( "use selector: %s", selectors[currentSelectorIndex].getClass().getSimpleName() );
      return getSelectorForNextCombination();
    }
    return new NoneSelector();
  }

  public void optimizeForCurrentCombination() {
    nextCombinationInfo.ifPresent( ci -> ci.getComponentClasses()
        .forEach( cc -> {
          for ( int i = currentSelectorIndex; i < selectors.length; i++ ) {
            selectors[i].addHigherPotentialType( cc );
          }
        } ) );

  }

  public void optimizeMatchingInfoBlacklist( Collection<MethodMatchingInfo> collection ) {
    for ( int i = currentSelectorIndex; i < selectors.length; i++ ) {
      selectors[i].addToBlacklist( collection );
    }
  }

  public void optimizeCheckTypeBlacklist( Class<?> componentInterface ) {
    for ( int i = currentSelectorIndex; i < selectors.length; i++ ) {
      selectors[i].addToBlacklist( componentInterface );
    }
  }

}
