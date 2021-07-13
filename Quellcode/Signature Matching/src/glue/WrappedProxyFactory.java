package glue;

import java.lang.reflect.Field;
import java.util.Collection;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.ProxyFactory;
import de.fernuni.hagen.ma.gundermann.signaturematching.ProxyFactoryCreator;
import util.Logger;

public class WrappedProxyFactory<T> implements ProxyFactory<T> {

  private final Class<T> targetStrcture;

  private final String delegationAttribute;

  public WrappedProxyFactory( Class<T> targetStrcture, String delegationAttribute ) {
    this.targetStrcture = targetStrcture;
    this.delegationAttribute = delegationAttribute;
  }

  @Override
  public T createProxy( Object component, Collection<MethodMatchingInfo> matchingInfos ) {
    try {
      Field wrappedField = getDeclaredFieldOfClassHierachry( component.getClass(),
          delegationAttribute );
      if ( wrappedField == null ) {
        logFieldError( delegationAttribute, component.getClass().getName() );
      }
      wrappedField.setAccessible( true );
      ProxyFactory<T> proxyFactory = getRelevantProxyFactoryCreator( matchingInfos )
          .createProxyFactory( targetStrcture );
      return proxyFactory.createProxy( wrappedField.get( component ), matchingInfos );
    }
    catch ( IllegalArgumentException | IllegalAccessException e ) {
      logFieldError( delegationAttribute, component.getClass().getName() );
    }
    return null;

  }

  // TODO Die ProxyFactory sollte im Matcher der MatchingInfo mitgegeben werden
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
