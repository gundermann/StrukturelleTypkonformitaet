package glue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collection;

import matching.methods.MethodMatchingInfo;

public class InterfaceProxyFactory<T> implements ProxyFactory<T> {

  private final Class<T> targetStructure;

  public InterfaceProxyFactory( Class<T> targetStructure ) {
    this.targetStructure = targetStructure;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public T createProxy( Object component, Collection<MethodMatchingInfo> matchingInfos ) {
    InvocationHandler invocationHandler = new BehaviourDelegateInvocationHandler( component,
        matchingInfos );

    return (T) Proxy.newProxyInstance( this.getClass().getClassLoader(),

        // ClassLoader.getSystemClassLoader(),
        // Die Verwendung des SystemClassLoaders führt zum Fehler, wenn die targetStructure in dem Modul, in dem der
        // Aufruf des Proxies initiiert wird, nicht bekannt ist.
        // Exception in thread "main" java.lang.IllegalArgumentException: interface ... is not visible from class loader

        new Class<?>[] { targetStructure }, invocationHandler );
  }

  static class InterfaceProxyFactoryCreator implements ProxyFactoryCreator {

    @Override
    public <T> ProxyFactory<T> createProxyFactory( Class<T> targetType ) {
      return new InterfaceProxyFactory<>( targetType );
    }

  }

}
