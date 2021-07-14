package de.fernuni.hagen.ma.gundermann.signaturematching.matching.types;

import java.util.Collection;

import de.fernuni.hagen.ma.gundermann.signaturematching.SingleMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatcherRate;

public interface TypeMatcher {

  /**
   * Prüft, ob ein Matching über den TypeMatcher hergestellt werden kann.<br>
   * Matching über Matcher <code>M</code>: <br>
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

  MatcherRate matchesWithRating( Class<?> checkType, Class<?> queryType );

}
