/*   $HeadURL$
 * ----------------------------------------------------------------------------
 *     (c) by data experts gmbh
 *            Woldegker Str. 12
 *            17033 Neubrandenburg
 * ----------------------------------------------------------------------------
 *     Dieses Dokument und die hierin enthaltenen Informationen unterliegen
 *     dem Urheberrecht und duerfen ohne die schriftliche Genehmigung des
 *     Herausgebers weder als ganzes noch in Teilen dupliziert, reproduziert
 *     oder manipuliert werden.
 * ----------------------------------------------------------------------------
 *     $Id$
 * ----------------------------------------------------------------------------
 */
package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

import matching.modules.PartlyTypeMatchingInfo;

public class AccumulatedMatchingRateComparator implements Comparator<Collection<PartlyTypeMatchingInfo>> {

  @Override
  public int compare( Collection<PartlyTypeMatchingInfo> o1, Collection<PartlyTypeMatchingInfo> o2 ) {
    int matchRatingCompare = compareQuantitaiveMatchRating( o1, o2 );
    if ( matchRatingCompare == 0 ) {
      return compareOriginalOfferedRating( o1, o2 );
    }
    return matchRatingCompare;
  }

  private int compareQuantitaiveMatchRating( Collection<PartlyTypeMatchingInfo> o1,
      Collection<PartlyTypeMatchingInfo> o2 ) {
    Double ranking1 = Optional.ofNullable( o1 ).map( Collection::stream )
        .map( s -> s.map( PartlyTypeMatchingInfo::getQuantitaiveMatchRating ).reduce( 0d, ( a, b ) -> a + b ) )
        .orElse( 0d );
    Double ranking2 = Optional.ofNullable( o2 ).map( Collection::stream )
        .map( s -> s.map( PartlyTypeMatchingInfo::getQuantitaiveMatchRating ).reduce( 0d, ( a, b ) -> a + b ) )
        .orElse( 0d );
    return Double.compare( ranking2, ranking1 );
  }

  // TODO ich glaube das hier bringt nicht wirklich viel.
  // Fuer die pcs-Tests bringt das sehr viel. Je spezieller die erwarteten Komponenten werden, desto mehr bringt diese
  // Heuristik
  private int compareOriginalOfferedRating( Collection<PartlyTypeMatchingInfo> o1,
      Collection<PartlyTypeMatchingInfo> o2 ) {
    Double ranking1 = Optional.ofNullable( o1 ).map( Collection::stream )
        .map( s -> s.map( PartlyTypeMatchingInfo::getQuantitativeMatchedToPotentialMethodsRating ).reduce( 0d,
            ( a, b ) -> a + b ) )
        .orElse( 0d );
    Double ranking2 = Optional.ofNullable( o2 ).map( Collection::stream )
        .map( s -> s.map( PartlyTypeMatchingInfo::getQuantitativeMatchedToPotentialMethodsRating ).reduce( 0d,
            ( a, b ) -> a + b ) )
        .orElse( 0d );
    return Double.compare( ranking2, ranking1 );
  }
}
