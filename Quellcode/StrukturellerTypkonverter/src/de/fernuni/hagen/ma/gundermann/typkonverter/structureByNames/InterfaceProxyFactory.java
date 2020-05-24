package de.fernuni.hagen.ma.gundermann.typkonverter.structureByNames;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class InterfaceProxyFactory<T> implements ProxyFactory<T> {

  private final Class<T> targetStrcture;

  public InterfaceProxyFactory( Class<T> targetStrcture ) {
    this.targetStrcture = targetStrcture;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public T createProxy( Object convertationObject ) {
    InvocationHandler invocationHandler = new BehaviourDelegateInvocationHandler( convertationObject );

    // TODO kl�ren, welcher Classloader in verteilten Systemen verwendet werden muss.
    return (T) Proxy.newProxyInstance( ClassLoader.getSystemClassLoader(),
        new Class<?>[] { targetStrcture }, invocationHandler );
  }

}
