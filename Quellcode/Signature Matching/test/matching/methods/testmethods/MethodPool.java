package matching.methods.testmethods;

import java.lang.reflect.Method;
import java.math.BigInteger;

public interface MethodPool {

  boolean getTrue();

  boolean getFalse();

  int getOne();

  Integer getOneNativeWrapped();

  void setBool( boolean b );

  void setBoolNativeWrapped( Boolean b );

  void setObject( Object o );

  int addOne( int a );

  int subOne( int a );

  int add( int a, int b );

  int sub( int a, int b );

  int addPartlyNativeWrapped( Integer a, int b );

  int subPartlyNativeWrapped( int a, Integer b );

  int addPartlyWrapped( BigInteger a, int b );

  int subPartlyWrapped( int a, BigInteger b );

  Number addGen( BigInteger a, Number b );

  Number addSpec( Number a, BigInteger b );

  BigInteger addSpecReturnSpec( Number a, BigInteger b );

  public static Method getMethod( String methodName ) {
    try {
      for ( Method m : MethodPool.class.getMethods() ) {
        if ( m.getName().equals( methodName ) ) {
          return m;
        }
      }
      throw new NoSuchMethodException();
    }
    catch ( NoSuchMethodException | SecurityException e ) {
      throw new RuntimeException( e );
    }
  }
}
