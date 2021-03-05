package matching.modules;

import java.util.Collection;

import matching.MatcherRate;

public interface TypeMatcher {

  /**
   * Prüft, ob ein Matching über den TypeMatcher hergestellt werden kann.<br>
   * Matching über Matcher <code>M</code>: <br>
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
