package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

import matching.MatcherRate;
import matching.modules.PartlyTypeMatchingInfo;

public class AccumulatedMatchingRateComparator implements Comparator<Collection<PartlyTypeMatchingInfo>> {

  @Override
  public int compare( Collection<PartlyTypeMatchingInfo> o1, Collection<PartlyTypeMatchingInfo> o2 ) {
    Double ranking1 = Optional.ofNullable( o1 ).map( Collection::stream )
        .map( s -> s.map( PartlyTypeMatchingInfo::getQualitativeMatchRating ).map( MatcherRate::getMatcherRating )
            .reduce( 0d, ( a, b ) -> a + b ) )
        .orElse( 0d );

    Double ranking2 = Optional.ofNullable( o2 ).map( Collection::stream )
        .map( s -> s.map( PartlyTypeMatchingInfo::getQualitativeMatchRating ).map( MatcherRate::getMatcherRating )
            .reduce( 0d, ( a, b ) -> a + b ) )
        .orElse( 0d );
    return Double.compare( ranking1, ranking2 );
  }

}
