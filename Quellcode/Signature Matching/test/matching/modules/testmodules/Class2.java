package matching.modules.testmodules;

import java.math.BigInteger;

public class Class2 {

  public int addPartlyNativeWrapped( Integer a, int b ) {
    return a + b;
  }

  public int subPartlyNativeWrapped( int a, Integer b ) {
    return a - b;
  }

  public int addPartlyWrapped( BigInteger a, int b ) {
    return a.intValue() + b;
  }

  public int subPartlyWrapped( int a, BigInteger b ) {
    return a - b.intValue();
  }

  public Number addGen( BigInteger a, Number b ) {
    return a.doubleValue() + b.doubleValue();
  }

  public Number addSpec( Number a, BigInteger b ) {
    return a.intValue() + b.doubleValue();
  }

}
