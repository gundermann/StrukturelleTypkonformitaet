package matching.methods;

import java.lang.reflect.Method;

/**
 * Dieser Matcher achtet darauf, dass die Typen (Return- und Argumenttypen) der beiden Methoden auch Generelisierungen
 * bzw. Spezialisierungen von einander sein können.
 */
public class GenSpecMethodMatcher implements MethodMatcher {

  @Override
  public boolean matches( Method m1, Method m2 ) {
    // TODO Auto-generated method stub
    return false;
  }

}
