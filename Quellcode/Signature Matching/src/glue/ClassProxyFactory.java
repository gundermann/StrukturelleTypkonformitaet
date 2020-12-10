package glue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import matching.methods.MethodMatchingInfo;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;

public class ClassProxyFactory<T> implements ProxyFactory<T> {

  private final Class<T> targetStrcture;

  public ClassProxyFactory( Class<T> targetStrcture ) {
    this.targetStrcture = targetStrcture;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public T createProxy( Map<Object, Collection<MethodMatchingInfo>> components2MatchingInfo ) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass( targetStrcture );
    BehaviourDelegateInvocationHandler handler = new BehaviourDelegateInvocationHandler( components2MatchingInfo );

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
  public T createProxy( Object component, Collection<MethodMatchingInfo> matchingInfos ) {
    Map<Object, Collection<MethodMatchingInfo>> components2MatchingInfo = new HashMap<>();
    components2MatchingInfo.put( component, matchingInfos );
    return createProxy( components2MatchingInfo );
  }

  // private void logFieldError( String fieldname, String classname ) {
  // Logger.err( String.format( "field %s not found in class hierarchy of %s",
  // fieldname, classname ) );
  // }
  //
  // private Field getDeclaredFieldOfClassHierachry( Class<? extends Object> startClass,
  // String fieldName ) {
  // Field declaredField = null;
  // try {
  // declaredField = startClass.getDeclaredField( fieldName );
  // }
  // catch ( NoSuchFieldException | SecurityException e ) {
  // if ( startClass.getSuperclass() != null ) {
  // return getDeclaredFieldOfClassHierachry( startClass.getSuperclass(), fieldName );
  // }
  // }
  // return declaredField;
  //
  // }

  static class ClassProxyFactoryCreator implements ProxyFactoryCreator {

    @Override
    public <T> ProxyFactory<T> createProxyFactory( Class<T> targetType ) {
      return new ClassProxyFactory<>( targetType );
    }

  }

}
