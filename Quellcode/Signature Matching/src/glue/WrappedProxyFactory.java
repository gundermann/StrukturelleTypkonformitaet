package glue;

import java.lang.reflect.Field;

import matching.modules.ModuleMatchingInfo;
import util.Logger;

public class WrappedProxyFactory<T> implements ProxyFactory<T> {

  private final String sourceDelegationAttribute;

  public WrappedProxyFactory( String sourceDelegationAttribute ) {
    this.sourceDelegationAttribute = sourceDelegationAttribute;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public T createProxy( Object component, ModuleMatchingInfo matchingInfo ) {
    try {
      Field wrappedField = getDeclaredFieldOfClassHierachry( component.getClass(),
          sourceDelegationAttribute );
      if ( wrappedField == null ) {
        logFieldError( sourceDelegationAttribute, component.getClass().getName() );
      }
      wrappedField.setAccessible( true );
      return (T) wrappedField.get( component );
    }
    catch ( IllegalArgumentException | IllegalAccessException e ) {
      logFieldError( sourceDelegationAttribute, component.getClass().getName() );
    }
    return null;
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
