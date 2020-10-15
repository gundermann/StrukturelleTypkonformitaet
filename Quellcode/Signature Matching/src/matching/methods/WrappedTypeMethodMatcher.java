package matching.methods;

import java.lang.reflect.Method;

/**
 * Dieser Matcher beachtet, dass die Typen (Return- und Argumenttypen) einer der beiden Methoden in einem Typ der
 * anderen Methode enthalten sein können (Wrapper).
 */
public class WrappedTypeMethodMatcher implements MethodMatcher {

  @Override
  public boolean matches( Method m1, Method m2 ) {
    // TODO Auto-generated method stub
    return false;
  }

}
