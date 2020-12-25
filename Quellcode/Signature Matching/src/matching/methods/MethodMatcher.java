package matching.methods;

import java.lang.reflect.Method;
import java.util.Collection;

import matching.MatcherRate;

public interface MethodMatcher {

  boolean matches( Method checkMethod, Method queryMethod );

  Collection<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod );

  MatcherRate matchesWithRating( Method checkMethod, Method queryMethod );
}
