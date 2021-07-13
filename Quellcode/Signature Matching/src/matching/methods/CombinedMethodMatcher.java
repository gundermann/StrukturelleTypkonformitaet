package matching.methods;

import java.lang.reflect.Method;
import java.util.Collection;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import matching.MatcherCombiner;
import matching.MatcherRate;
import matching.types.ExactTypeMatcher;
import matching.types.GenSpecTypeMatcher;
import matching.types.WrappedTypeMatcher;

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
  public MatcherRate matchesWithRating( Method checkMethod, Method queryMethod ) {
    return combination.matchesWithRating( checkMethod, queryMethod );
  }

}
