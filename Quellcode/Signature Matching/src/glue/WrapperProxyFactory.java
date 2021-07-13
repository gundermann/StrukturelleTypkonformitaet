package glue;

import java.lang.reflect.Field;
import java.util.Collection;

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
  public T createProxy( Object component, Collection<MethodMatchingInfo> matchingInfos ) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass( targetStrcture );
    BehaviourDelegateInvocationHandler handler = new BehaviourDelegateInvocationHandler( component,
        matchingInfos );

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
      Field wrappedField = getDeclaredFieldOfClassHierachry( targetStrcture,
          targetDelegationAttribute );
      if ( wrappedField == null ) {
        logFieldError( targetDelegationAttribute, targetStrcture.getName() );
      }
      wrappedField.setAccessible( true );
      wrappedField.set( proxyInstance,
          createProxyOfWrappedComponent( wrappedField.getType(), component, matchingInfos ) );
    }
    catch ( IllegalArgumentException | IllegalAccessException e ) {
      Logger.switchOn();
      logFieldError( targetDelegationAttribute, targetStrcture.getName() );
    }
    return (T) proxyInstance;
  }

  private Object createProxyOfWrappedComponent( Class<?> fieldType, Object component,
      Collection<MethodMatchingInfo> matchingInfos ) {
    ProxyFactoryCreator proxyFactoryCreator = getRelevantProxyFactoryCreator( matchingInfos );
    return proxyFactoryCreator.createProxyFactory( fieldType ).createProxy( component, matchingInfos );
  }

  // TODO Die ProxyFactory sollte im Matcher der ModuleMatchingInfo mitgegeben werden
  private ProxyFactoryCreator getRelevantProxyFactoryCreator( Collection<MethodMatchingInfo> matchingInfos ) {
    return matchingInfos.isEmpty() ? ProxyCreatorFactories.getIdentityFactoryCreator()
        : ProxyCreatorFactories.getClassProxyFactoryCreator();
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
