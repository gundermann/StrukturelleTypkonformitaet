package matching.types.testtypes;

import java.math.BigInteger;

public interface Interface1 {

  boolean getTrue();

  boolean getFalse();

  int getOne();

  int addPartlyNativeWrapped( Integer a, int b );

  int subPartlyNativeWrapped( int a, Integer b );

  int addPartlyWrapped( BigInteger a, int b );

  int subPartlyWrapped( int a, BigInteger b );

}
