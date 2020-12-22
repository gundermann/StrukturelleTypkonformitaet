package matching.methods;

import java.lang.reflect.Method;
import java.util.Collection;

import matching.MatcherCombiner;
import matching.modules.ExactTypeMatcher;
import matching.modules.GenSpecTypeMatcher;
import matching.modules.WrappedTypeMatcher;

public class CombinedMethodMatcher implements MethodMatcher {

  ExactTypeMatcher exactTypeMatcher = new ExactTypeMatcher();

  GenSpecTypeMatcher genSpecTypeMatcher = new GenSpecTypeMatcher();

  MethodMatcher combination = new ParamPermMethodMatcher(
      MatcherCombiner.combine( genSpecTypeMatcher, exactTypeMatcher,
          new WrappedTypeMatcher( MatcherCombiner.combine( genSpecTypeMatcher,
              exactTypeMatcher ) ) ) );

  @Override
  public boolean matches( Method checkMethod, Method queryMethod ) {
    return combination.matches( checkMethod, queryMethod );
  }

  @Override
  public Collection<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod ) {
    return combination.calculateMatchingInfos( checkMethod, queryMethod );
  }

  @Override
  public double matchesWithRating( Method checkMethod, Method queryMethod ) {
    return combination.matchesWithRating( checkMethod, queryMethod );
  }

}
