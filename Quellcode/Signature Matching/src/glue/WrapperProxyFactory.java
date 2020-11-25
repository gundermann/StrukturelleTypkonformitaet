package glue;

import java.lang.reflect.Field;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import matching.modules.ModuleMatchingInfo;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import util.Logger;

public class WrapperProxyFactory<T> implements ProxyFactory<T> {

  private final Class<T> targetStrcture;

  private final String targetDelegationAttribute;

  public WrapperProxyFactory( Class<T> targetStrcture, String targetDelegationAttribute ) {
    this.targetStrcture = targetStrcture;
    this.targetDelegationAttribute = targetDelegationAttribute;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public T createProxy( Object component, ModuleMatchingInfo matchingInfo ) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass( targetStrcture );
    BehaviourDelegateInvocationHandler handler = new BehaviourDelegateInvocationHandler( component,
        matchingInfo );

    MethodInterceptor methodInterceptor = ( obj, method, args, proxyMethod ) -> {
      return handler.intercept( obj, method, args, proxyMethod );
    };
    enhancer.setCallbackType( methodInterceptor.getClass() );
    Class<T> enhancedClass = enhancer.createClass();

    Objenesis objenesis = new ObjenesisStd();
    ObjectInstantiator<?> instantiator = objenesis.getInstantiatorOf( enhancedClass );
    Object proxyInstance = instantiator.newInstance();
    ( (Factory) proxyInstance ).setCallbacks( new Callback[] { methodInterceptor } );

    // es handelt sich um einen Wrapper, der die übergebene component enthalten muss
    // da cglib keine Felder in Klassen erzeugen kann, ist das entsprechende Feld für die componente in den
    // Oberklassen zu suchen.
    //
    try {
      Field wrappedField = getDeclaredFieldOfClassHierachry( proxyInstance.getClass(),
          targetDelegationAttribute );
      if ( wrappedField == null ) {
        logFieldError( targetDelegationAttribute, targetStrcture.getName() );
      }
      wrappedField.setAccessible( true );
      wrappedField.set( proxyInstance, component );
    }
    catch ( IllegalArgumentException | IllegalAccessException e ) {
      logFieldError( targetDelegationAttribute, targetStrcture.getName() );
    }
    return (T) proxyInstance;
  }

  private void logFieldError( String fieldname, String classname ) {
    Logger.err( String.format( "field %s not found in class hierarchy of %s",
        fieldname, classname ) );
  }

  private Field getDeclaredFieldOfClassHierachry( Class<? extends Object> startClass,
      String fieldName ) {
    Field declaredField = null;
    try {
      declaredField = startClass.getDeclaredField( fieldName );
    }
    catch ( NoSuchFieldException | SecurityException e ) {
      if ( startClass.getSuperclass() != null ) {
        return getDeclaredFieldOfClassHierachry( startClass.getSuperclass(), fieldName );
      }
    }
    return declaredField;

  }

}
