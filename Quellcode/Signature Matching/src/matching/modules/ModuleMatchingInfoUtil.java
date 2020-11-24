package matching.modules;

import java.lang.reflect.Field;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class ModuleMatchingInfoUtil {

  private ModuleMatchingInfoUtil() {
  }

  public static Function<Object, Object> getFieldFunction( Class<?> classWithField, String fieldName ) {
    return ( o ) -> getField( o, classWithField, fieldName );
  }

  public static BiFunction<Object, Object, Void> setFieldFunction( Class<?> classWithField, String fieldName ) {
    return ( fieldValue, o ) -> setField( fieldValue, o, classWithField, fieldName );
  }

  private static Void setField( Object value, Object obj, Class<?> classWithField, String fieldName ) {
    try {
      Field wrappedField = getDeclaredFieldOfClassHierachry( classWithField, fieldName );
      wrappedField.setAccessible( true );
      wrappedField.set( value, obj );
    }
    catch ( IllegalArgumentException | IllegalAccessException e ) {
      e.printStackTrace();
    }
    return null;
  }

  private static Object getField( Object obj, Class<?> classWithField, String fieldName ) {
    try {
      Field sourceField = classWithField.getDeclaredField( fieldName );
      sourceField.setAccessible( true );
      return sourceField.get( obj );
    }
    catch ( NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e ) {
      e.printStackTrace();
      return null;
    }
  }

  private static Field getDeclaredFieldOfClassHierachry( Class<? extends Object> startClass,
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
