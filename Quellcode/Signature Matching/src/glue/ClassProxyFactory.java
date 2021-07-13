package glue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.ProxyFactory;
import de.fernuni.hagen.ma.gundermann.signaturematching.ProxyFactoryCreator;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;

public class ClassProxyFactory<T> implements ProxyFactory<T> {

  private final Class<T> sourceType;

  public ClassProxyFactory( Class<T> sourceType ) {
    this.sourceType = sourceType;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public T createProxy( Map<Object, Collection<MethodMatchingInfo>> targets2MatchingInfo ) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass( sourceType );
    BehaviourDelegateInvocationHandler handler = new BehaviourDelegateInvocationHandler( targets2MatchingInfo );

    MethodInterceptor methodInterceptor = ( obj, method, args, proxyMethod ) -> {
      return handler.intercept( obj, method, args, proxyMethod );
    };
    enhancer.setCallbackType( methodInterceptor.getClass() );
    Class<T> enhancedClass = enhancer.createClass();

    Objenesis objenesis = new ObjenesisStd();
    ObjectInstantiator<?> instantiator = objenesis.getInstantiatorOf( enhancedClass );
    Object proxyInstance = instantiator.newInstance();
    ( (Factory) proxyInstance ).setCallbacks( new Callback[] { methodInterceptor } );
    return (T) proxyInstance;
  }

  @Override
  public T createProxy( Object target, Collection<MethodMatchingInfo> matchingInfos ) {
    Map<Object, Collection<MethodMatchingInfo>> components2MatchingInfo = new HashMap<>();
    components2MatchingInfo.put( target, matchingInfos );
    return createProxy( components2MatchingInfo );
  }

  static class ClassProxyFactoryCreator implements ProxyFactoryCreator {

    @Override
    public <T> ProxyFactory<T> createProxyFactory( Class<T> targetType ) {
      return new ClassProxyFactory<>( targetType );
    }

  }

}
