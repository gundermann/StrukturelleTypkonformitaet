package matching.methods;

import java.lang.reflect.Method;

/**
 * Dieser Matcher beachtet, dass die Argumenttypen der beiden Methoden in unterschiedlicher Reihenfolge angegeben sein
 * k�nnen.
 */
public class ParamPermMethodMatcher implements MethodMatcher {

  @Override
  public boolean matches( Method m1, Method m2 ) {
    // TODO Auto-generated method stub
    return false;
  }

}
