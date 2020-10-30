package glue;

import org.objenesis.ObjenesisHelper;

import matching.modules.ModuleMatchingInfo;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
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
    // addInterceptor( handler );

    MethodInterceptor methodInterceptor = ( obj, method, args, proxy ) -> handler.invoke( proxy, method, args );
    enhancer.setCallbackType( methodInterceptor.getClass() );
    // enhancer.setCallback( methodInterceptor );

    Class<T> enhancedClass = enhancer.createClass();

    // Das Verändern des Method-Interceptors (bzw. das anmelden eines je Objekt) führt dazu, dass alle Objekte mit der
    // Sematik des zuletzt konvertiern Objektes arbeit. Der registrierte Interceptor wird immer wieder überschrieben.
    // TODO Lösung: Eine Map mit Target- und Source-Objekten und
    Enhancer.registerCallbacks( enhancedClass, new Callback[] { methodInterceptor } );
    return ObjenesisHelper.newInstance( enhancedClass );
  }

}
