package de.fernuni.hagen.ma.gundermann.signaturematching.matching.types;

import java.util.Collection;

import de.fernuni.hagen.ma.gundermann.signaturematching.SingleMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatcherRate;

public interface TypeMatcher {

  /**
   * Pr�ft, ob ein Matching �ber den TypeMatcher hergestellt werden kann.<br>
   * Matching �ber Matcher <code>M</code>: <br>
   * <b> checkType &equiv;<sub>M</sub> queryType</b>
   *
   * @param checkType
   *          - source
   * @param queryType
   *          - target
   * @return
   */
  boolean matchesType( Class<?> checkType, Class<?> queryType );

  Collection<SingleMatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType );

  double getTypeMatcherRate();

  default MatcherRate matchesWithRating( Class<?> checkType, Class<?> queryType ) {
    if ( matchesType( checkType, queryType ) ) {
      MatcherRate rate = new MatcherRate();
      rate.add( this.getClass().getSimpleName(), getTypeMatcherRate() );
      return rate;
    }
    return null;
  }

}
