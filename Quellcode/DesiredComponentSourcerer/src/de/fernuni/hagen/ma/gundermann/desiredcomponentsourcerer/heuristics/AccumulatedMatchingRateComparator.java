package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import matching.MatcherRate;
import matching.Setting;
import matching.modules.PartlyTypeMatchingInfo;

/**
 * This Comparator is responsable of the following heuristic:</br>
 * H: combinate low matcher rating first
 */
public class AccumulatedMatchingRateComparator implements Comparator<Collection<PartlyTypeMatchingInfo>> {

  @Override
  public int compare( Collection<PartlyTypeMatchingInfo> o1, Collection<PartlyTypeMatchingInfo> o2 ) {
    Optional<Stream<MatcherRate>> mr1 = Optional.ofNullable( o1 ).map( Collection::stream )
        .map( s -> s.map( PartlyTypeMatchingInfo::getQualitativeMatchRating ) );

    Optional<Stream<MatcherRate>> mr2 = Optional.ofNullable( o2 ).map( Collection::stream )
        .map( s -> s.map( PartlyTypeMatchingInfo::getQualitativeMatchRating ) );

    if ( !mr1.isPresent() && !mr2.isPresent() ) {
      return 0;
    }
    if ( mr1.isPresent() && !mr2.isPresent() ) {
      return 1;
    }
    if ( !mr1.isPresent() && mr2.isPresent() ) {
      return -1;
    }

    // Vorher
     Double ranking1 = mr1.get().map( MatcherRate::getMatcherRating ).reduce( 0d, ( a, b ) -> a + b );
    // TODO
//    Double ranking1 = Setting.QUALITATIVE_COMPONENT_MATCH_RATE_CUMULATION.apply( mr1.get() ).getMatcherRating();

    // Vorher
     Double ranking2 = mr2.get().map( MatcherRate::getMatcherRating ).reduce( 0d, ( a, b ) -> a + b );
    // TODO
//    Double ranking2 = Setting.QUALITATIVE_COMPONENT_MATCH_RATE_CUMULATION.apply( mr2.get() ).getMatcherRating();

    return Double.compare( ranking1, ranking2 );
  }

}
