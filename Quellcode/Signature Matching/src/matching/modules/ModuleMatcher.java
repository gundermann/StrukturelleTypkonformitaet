package matching.modules;

import matching.methods.CombinedMethodMatcher;
import matching.methods.MethodMatcher;

public class ModuleMatcher {

  MethodMatcher methodMatcher = new CombinedMethodMatcher();

  public boolean matches( Class<?> type1, Class<?> type2 ) {

  }

}
