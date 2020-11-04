package matching.methods;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

import matching.modules.ModuleMatchingInfo;

public interface MethodMatcher {

  boolean matches( Method checkMethod, Method queryMethod );

  Set<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod );

  boolean matchesType( Class<?> checkType, Class<?> queryType );

  Collection<ModuleMatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType );
}
