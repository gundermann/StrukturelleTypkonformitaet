package glue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import matching.modules.ModuleMatchingInfo;

public class InterfaceProxyFactory<T> implements ProxyFactory<T> {

  private final Class<T> targetStrcture;

  public InterfaceProxyFactory( Class<T> targetStrcture ) {
    this.targetStrcture = targetStrcture;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public T createProxy( Object component, ModuleMatchingInfo matchingInfo ) {
    InvocationHandler invocationHandler = new BehaviourDelegateInvocationHandler<T>( component, matchingInfo );

    // TODO kl�ren, welcher Classloader in verteilten Systemen verwendet werden muss.
    return (T) Proxy.newProxyInstance( ClassLoader.getSystemClassLoader(),
        new Class<?>[] { targetStrcture }, invocationHandler );
  }

}
