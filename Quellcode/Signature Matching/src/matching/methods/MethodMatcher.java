package matching.methods;

import java.lang.reflect.Method;
import java.util.Set;

public interface MethodMatcher {

  boolean matches( Method m1, Method m2 );

  Set<MethodMatchingInfo> calculateMatchingInfos( Method m1, Method m2 );
}
