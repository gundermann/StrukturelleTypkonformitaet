package matching.methods;

import java.lang.reflect.Method;
import java.util.Set;

public interface MethodMatcher {

  boolean matches( Method checkMethod, Method queryMethod );

  Set<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod );

  boolean matchesType( Class<?> checkType, Class<?> queryType );
}
