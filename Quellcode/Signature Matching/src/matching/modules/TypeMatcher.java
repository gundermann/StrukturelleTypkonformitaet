package matching.modules;

import java.util.Collection;

import matching.MatcherRate;

public interface TypeMatcher {

  boolean matchesType( Class<?> checkType, Class<?> queryType );

  Collection<ModuleMatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType );

  MatcherRate matchesWithRating( Class<?> checkType, Class<?> queryType );

}
