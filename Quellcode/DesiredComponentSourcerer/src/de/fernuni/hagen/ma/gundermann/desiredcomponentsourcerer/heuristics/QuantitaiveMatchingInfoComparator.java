package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.util.Comparator;
import java.util.Optional;

import matching.modules.PartlyTypeMatchingInfo;

@Deprecated
public class QuantitaiveMatchingInfoComparator implements Comparator<PartlyTypeMatchingInfo> {

  // Der mit dem hoechsten Ranking soll vorne stehen
  @Override
  public int compare( PartlyTypeMatchingInfo o1, PartlyTypeMatchingInfo o2 ) {
    int matchRatingCompare = compareQuantitaiveMatchRating( o1, o2 );
    if ( matchRatingCompare == 0 ) {
      return compareOriginalOfferedRating( o1, o2 );
    }
    return matchRatingCompare;
  }

  private int compareQuantitaiveMatchRating( PartlyTypeMatchingInfo o1, PartlyTypeMatchingInfo o2 ) {
    Double ranking1 = Optional.ofNullable( o1 ).map( PartlyTypeMatchingInfo::getQuantitaiveMatchRating ).orElse( 0d );
    Double ranking2 = Optional.ofNullable( o2 ).map( PartlyTypeMatchingInfo::getQuantitaiveMatchRating ).orElse( 0d );
    return Double.compare( ranking2, ranking1 );
  }

  // TODO ich glaube das hier bringt nicht wirklich viel.
  // Fuer die pcs-Tests bringt das sehr viel. Je spezieller die erwarteten Komponenten werden, desto mehr bringt diese
  // Heuristik
  private int compareOriginalOfferedRating( PartlyTypeMatchingInfo o1, PartlyTypeMatchingInfo o2 ) {
    Double ranking1 = Optional.ofNullable( o1 )
        .map( PartlyTypeMatchingInfo::getQuantitativeMatchedToPotentialMethodsRating )
        .orElse( 0d );
    Double ranking2 = Optional.ofNullable( o2 )
        .map( PartlyTypeMatchingInfo::getQuantitativeMatchedToPotentialMethodsRating )
        .orElse( 0d );
    return Double.compare( ranking2, ranking1 );
  }

}