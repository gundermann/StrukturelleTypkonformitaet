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
    InvocationHandler invocationHandler = new BehaviourDelegateInvocationHandler( component, matchingInfo );

    return (T) Proxy.newProxyInstance( this.getClass().getClassLoader(),

        // ClassLoader.getSystemClassLoader(),
        // Die Verwendung des SystemClassLoaders führt zum Fehler, wenn die targetStructure in dem Modul, in dem der
        // Aufruf des Proxies initiiert wird, nicht bekannt ist.
        // Exception in thread "main" java.lang.IllegalArgumentException: interface ... is not visible from class loader

        new Class<?>[] { targetStrcture }, invocationHandler );
  }

}
