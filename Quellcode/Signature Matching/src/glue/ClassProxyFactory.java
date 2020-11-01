package glue;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisHelper;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import matching.modules.ModuleMatchingInfo;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;

public class ClassProxyFactory<T> implements ProxyFactory<T> {

  private final Class<T> targetStrcture;

  public ClassProxyFactory( Class<T> targetStrcture ) {
    this.targetStrcture = targetStrcture;
  }

  @Override
  public T createProxy( Object component, ModuleMatchingInfo<T> matchingInfo ) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass( targetStrcture );
    BehaviourDelegateInvocationHandler<T> handler = new BehaviourDelegateInvocationHandler<>( component,
        matchingInfo );

    MethodInterceptor methodInterceptor = ( obj, method, args, proxy ) -> handler.invoke( proxy, method, args );
    enhancer.setCallbackType( methodInterceptor.getClass() );
    Class<T> enhancedClass = enhancer.createClass();
    
    Objenesis objenesis = new ObjenesisStd();
    ObjectInstantiator<?> instantiator = objenesis.getInstantiatorOf(enhancedClass);
    Object proxyInstance = instantiator.newInstance();
    ((Factory) proxyInstance).setCallbacks(new Callback[]{methodInterceptor});
    return (T) proxyInstance;
  }

}
