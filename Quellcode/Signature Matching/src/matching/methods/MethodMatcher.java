package matching.methods;

import java.lang.reflect.Method;
import java.util.Collection;

public interface MethodMatcher {

  boolean matches( Method checkMethod, Method queryMethod );

  Collection<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod );

}
