package matching.modules;

import java.util.Collection;

public interface TypeMatcher {

  boolean matchesType( Class<?> checkType, Class<?> queryType );

  Collection<ModuleMatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType );
}
