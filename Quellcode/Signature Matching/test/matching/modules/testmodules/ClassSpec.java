package matching.modules.testmodules;

import java.math.BigInteger;

public class ClassSpec {

  public Number add( BigInteger a, BigInteger b ) {
    return a.add( b );
  }

  public BigInteger sub( Number a, Number b ) {
    return BigInteger.valueOf( a.longValue() - b.longValue() );
  }

  public Number div( BigInteger a, Number b ) {
    return a.divide( BigInteger.valueOf( b.longValue() ) );
  }

  public BigInteger mult( Number a, BigInteger b ) {
    return b.multiply( BigInteger.valueOf( a.longValue() ) );
  }
}
