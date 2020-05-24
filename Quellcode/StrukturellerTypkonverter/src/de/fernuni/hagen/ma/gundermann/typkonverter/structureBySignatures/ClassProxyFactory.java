package de.fernuni.hagen.ma.gundermann.typkonverter.structureBySignatures;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class ClassProxyFactory<T> implements ProxyFactory<T> {

  private final Class<T> targetStrcture;

  public ClassProxyFactory( Class<T> targetStrcture ) {
    this.targetStrcture = targetStrcture;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public T createProxy( Object convertationObject ) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass( targetStrcture );
    BehaviourDelegateInvocationHandler handler = new BehaviourDelegateInvocationHandler( convertationObject );
    MethodInterceptor methodInterceptor = ( obj, method, args, proxy ) -> handler.invoke( proxy, method, args );
    enhancer.setCallback( methodInterceptor );
    T targetObject = (T) enhancer.create();
    return targetObject;
  }

}
