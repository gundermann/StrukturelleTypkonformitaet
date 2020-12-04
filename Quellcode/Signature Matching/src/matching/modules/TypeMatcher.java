package matching.modules;

import java.util.Collection;

public interface TypeMatcher {

  boolean matchesType( Class<?> checkType, Class<?> queryType );

  /**
   * @param checkType
   * @param queryType
   *          must be an interface
   * @return Exists m1 checkType, m2 in queryType: m1 matches m2
   */
  default boolean matchesTypePartly( Class<?> checkType, Class<?> queryType ) {
    return matchesType( checkType, queryType );
  }

  Collection<ModuleMatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType );
}
