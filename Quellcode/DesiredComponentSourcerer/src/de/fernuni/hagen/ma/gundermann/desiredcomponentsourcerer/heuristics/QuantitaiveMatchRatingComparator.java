package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.util.Comparator;
import java.util.Optional;

import matching.modules.PartlyTypeMatchingInfo;

public class QuantitaiveMatchRatingComparator implements Comparator<PartlyTypeMatchingInfo> {

  // Der mit dem hoechsten Ranking soll vorne stehen
  @Override
  public int compare( PartlyTypeMatchingInfo o1, PartlyTypeMatchingInfo o2 ) {
    Double ranking1 = Optional.ofNullable( o1 ).map( PartlyTypeMatchingInfo::getQuantitaiveMatchRating ).orElse( 0d );
    Double ranking2 = Optional.ofNullable( o2 ).map( PartlyTypeMatchingInfo::getQuantitaiveMatchRating ).orElse( 0d );
    return Double.compare( ranking2, ranking1 );
  }

}