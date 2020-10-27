package matching.modules.testmodules;

import java.math.BigInteger;

public enum Enum2 {
  CONSTANT_1;

  public Boolean getTrue() {
    return true;
  }

  public Boolean getFalse() {
    return false;
  }

  public Integer getOne() {
    return 1;
  }

  public int subPartlyNativeWrapped( int a, Integer b ) {
    return a - b;
  }

  public int addPartlyWrapped( BigInteger a, int b ) {
    return a.intValue() + b;
  }

}
