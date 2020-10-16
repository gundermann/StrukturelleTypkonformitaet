package matching.methods;

import java.lang.reflect.Method;

/**
 * Dieser Matcher achtet darauf, dass die Typen (Return- und Argumenttypen) der beiden Methoden auch Generelisierungen
 * bzw. Spezialisierungen von einander sein können.
 */
public class GenSpecMethodMatcher implements MethodMatcher {

  @Override
  public boolean matches( Method m1, Method m2 ) {
    MethodStructure ms1 = MethodStructure.createFromDeclaredMethod( m1 );
    MethodStructure ms2 = MethodStructure.createFromDeclaredMethod( m2 );
    return matches( ms1, ms2 );
  }

  private boolean matches( MethodStructure ms1, MethodStructure ms2 ) {
    if ( ms1.getSortedArgumentTypes().length != ms2.getSortedArgumentTypes().length ) {
      return false;
    }
    if ( !machtesGenSpecType( ms1.getReturnType(), ms2.getReturnType() ) ) {
      return false;
    }
    for ( int i = 0; i < ms1.getSortedArgumentTypes().length; i++ ) {
      if ( !machtesGenSpecType( ms1.getSortedArgumentTypes()[i], ms2.getSortedArgumentTypes()[i] ) ) {
        return false;
      }
    }
    return true;
  }

  static boolean machtesGenSpecType( Class<?> t1, Class<?> t2 ) {
    return t1.isAssignableFrom( t2 ) || t2.isAssignableFrom( t1 );
  }

}
