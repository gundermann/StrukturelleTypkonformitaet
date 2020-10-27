package matching.modules;

import matching.methods.MethodMatcher;

public class ModuleMatcherTestSupport {

  public static <S> ModuleMatcher<S> createModuleMatcher( Class<S> queryType, MethodMatcher methodMatcher ) {
    return new ModuleMatcher<>( queryType, methodMatcher );
  }

}
