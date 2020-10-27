package glue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;

import matching.methods.MethodMatchingInfo;
import matching.modules.ModuleMatchingInfo;

public class BehaviourDelegateInvocationHandler<T> implements InvocationHandler {

  private Object component;

  private ModuleMatchingInfo<T> matchingInfos;

  public BehaviourDelegateInvocationHandler( Object component, ModuleMatchingInfo<T> matchingInfos ) {
    this.component = component;
    this.matchingInfos = matchingInfos;
  }

  @Override
  public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable {
    Optional<MethodMatchingInfo> matchingInfo = getMethodMatchingInfo( method );
    if ( matchingInfo.isPresent() ) {
      Method targetMethod = matchingInfo.get().getTarget();
      return targetMethod.invoke( component, args );
    }
    throw new NoSuchMethodException();
  }

  private Optional<MethodMatchingInfo> getMethodMatchingInfo( Method method ) {
    return matchingInfos.getMethodMatchingInfos().stream().filter( info -> info.getSource().equals( method ) )
        .findFirst();
  }

}
