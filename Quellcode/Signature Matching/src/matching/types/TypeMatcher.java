package matching.types;

import java.util.Collection;

import matching.MatcherRate;
import matching.MatchingInfo;

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

  Collection<MatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType );

  MatcherRate matchesWithRating( Class<?> checkType, Class<?> queryType );

}
