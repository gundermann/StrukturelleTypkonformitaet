package glue;

import java.util.Collection;

import matching.methods.MethodMatchingInfo;

public interface ProxyFactory<T> {

  T createProxy( Object component, Collection<MethodMatchingInfo> matchingInfos );

}
