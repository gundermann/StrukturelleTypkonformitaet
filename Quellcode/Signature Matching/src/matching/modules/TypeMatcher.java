package matching.modules;

import java.util.Collection;

import matching.MatcherRate;

public interface TypeMatcher {

  /**
   * Pr�ft, ob ein Matching �ber den TypeMatcher hergestellt werden kann.<br>
   * Matching �ber Matcher <code>M</code>: <br>
   * <b> checkType &equiv;<sub>M</sub> queryType</b>
   *
   * @param checkType
   * @param queryType
   * @return
   */
  boolean matchesType( Class<?> checkType, Class<?> queryType );

  Collection<ModuleMatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType );

  MatcherRate matchesWithRating( Class<?> checkType, Class<?> queryType );

}
