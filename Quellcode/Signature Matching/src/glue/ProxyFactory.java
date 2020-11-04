package glue;

import matching.modules.ModuleMatchingInfo;

public interface ProxyFactory<T> {

  T createProxy( Object component, ModuleMatchingInfo matchingInfo );

}
