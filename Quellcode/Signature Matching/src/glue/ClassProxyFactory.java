package glue;

import matching.modules.ModuleMatchingInfo;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class ClassProxyFactory<T> implements ProxyFactory<T> {

  private final Class<T> targetStrcture;

  public ClassProxyFactory( Class<T> targetStrcture ) {
    this.targetStrcture = targetStrcture;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public T createProxy( Object component, ModuleMatchingInfo<T> matchingInfo ) {

    // TODO Was ist mit abstrakten Klassen?
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass( targetStrcture );
    BehaviourDelegateInvocationHandler<T> handler = new BehaviourDelegateInvocationHandler<>( component,
        matchingInfo );
    MethodInterceptor methodInterceptor = ( obj, method, args, proxy ) -> handler.invoke( proxy, method, args );
    enhancer.setCallback( methodInterceptor );
    T targetObject = (T) enhancer.create();
    return targetObject;
  }

}
