package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.CombinationSelector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.CommonSelector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.SingleSelector;
import matching.modules.PartlyTypeMatchingInfo;

// Auskommentierte Teile gehoeren zu Heuristiken, die ich begonnen hatte umzusetzten, aber nicht zuende gedacht hatte.
public class BestMatchingComponentCombinationFinder {

  // Selektor f�r strukturell 100%ig matchende Komponenten
  private final Selector singleSelector;

  // Selektor f�r die Kombination von strukturell 100%ig matchenden Komponenten
  private final Selector fullMatchingCombinationSelector;

  // Selektor f�r die Kombination von Komponenten, sodass alle erwarteten Methoden bedient werden.
  private final Selector commonSelector;

  private final List<PartlyTypeMatchingInfo> quantitativeSortedInfos;

  private Optional<CombinationInfo> nextCombinationInfo = Optional.empty();

  BestMatchingComponentCombinationFinder(
      Map<Class<?>, PartlyTypeMatchingInfo> componentInterface2PartlyMatchingInfos ) {
    quantitativeSortedInfos = new ArrayList<>(
        componentInterface2PartlyMatchingInfos.values() );
    Collections.sort( quantitativeSortedInfos, new QuantitaiveMatchRankingComparator() );
    this.singleSelector = new SingleSelector( quantitativeSortedInfos.stream()
        .filter( CombinationFinderUtils::isFullMatchingComponent ).collect( Collectors.toList() ) );
    this.fullMatchingCombinationSelector = new CombinationSelector( quantitativeSortedInfos.stream()
        .filter( CombinationFinderUtils::isFullMatchingComponent ).collect( Collectors.toList() ) );
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
    // if ( singleSelector.hasNext() ) {
    // return singleSelector;
    // }
    // if ( fullMatchingCombinationSelector.hasNext() ) {
    // return fullMatchingCombinationSelector;
    // }

    if ( commonSelector.hasNext() ) {
      return commonSelector;
    }
    return new Selector() {
      @Override
      public boolean hasNext() {
        return false;
      }

      @Override
      public Optional<CombinationInfo> getNext() {
        return Optional.empty();
      }
    };
  }

  private static class QuantitaiveMatchRankingComparator implements Comparator<PartlyTypeMatchingInfo> {

    // Der mit dem hoechsten Ranking soll vorne stehen
    @Override
    public int compare( PartlyTypeMatchingInfo o1, PartlyTypeMatchingInfo o2 ) {
      Double ranking1 = Optional.ofNullable( o1 ).map( PartlyTypeMatchingInfo::getQuantitaiveMatchRating ).orElse( 0d );
      Double ranking2 = Optional.ofNullable( o2 ).map( PartlyTypeMatchingInfo::getQuantitaiveMatchRating ).orElse( 0d );
      return Double.compare( ranking2, ranking1 );
    }

  }

}
