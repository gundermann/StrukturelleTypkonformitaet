package matching.methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import matching.modules.ModuleMatchingInfo;

public interface MethodMatcher {

  boolean matches( Method checkMethod, Method queryMethod );

  Collection<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod );

  @Deprecated
  default boolean matchesType( Class<?> checkType, Class<?> queryType ) {
    return false;
  }

  @Deprecated
  default Collection<ModuleMatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType ) {
    return new ArrayList<>();
  }
}
