package matching.methods;

import java.lang.reflect.Method;

public interface MethodMatcher {

  boolean matches( Method m1, Method m2 );
}
