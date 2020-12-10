package glue;

import java.util.Collection;
import java.util.Map;

import matching.methods.MethodMatchingInfo;

public interface ProxyFactory<T> {

  T createProxy( Object component, Collection<MethodMatchingInfo> matchingInfos );

  default T createProxy( Map<Object, Collection<MethodMatchingInfo>> components2MatchingInfo ) {
    Object component = components2MatchingInfo.keySet().iterator().next();
    return createProxy( component, components2MatchingInfo.get( component ) );
  }

}
