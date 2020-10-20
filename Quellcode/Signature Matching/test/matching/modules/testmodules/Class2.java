package matching.modules.testmodules;

import java.math.BigInteger;

public class Class2 {

  int addPartlyNativeWrapped( Integer a, int b ) {
    return a + b;
  }

  int subPartlyNativeWrapped( int a, Integer b ) {
    return a - b;
  }

  int addPartlyWrapped( BigInteger a, int b ) {
    return a.intValue() + b;
  }

  int subPartlyWrapped( int a, BigInteger b ) {
    return a - b.intValue();
  }

  Number addGen( BigInteger a, Number b ) {
    return a.doubleValue() + b.doubleValue();
  }

  Number addSpec( Number a, BigInteger b ) {
    return a.intValue() + b.doubleValue();
  }

}
