package matching.types;

import java.util.Collection;

import matching.MatcherRate;
import matching.MatchingInfo;

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

  Collection<MatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType );

  MatcherRate matchesWithRating( Class<?> checkType, Class<?> queryType );

}
