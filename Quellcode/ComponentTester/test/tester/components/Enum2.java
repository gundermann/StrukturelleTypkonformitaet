package tester.components;

import java.math.BigInteger;

public enum Enum2 {
  CONSTANT_1;

  Boolean getTrue() {
    return true;
  }

  Boolean getFalse() {
    return false;
  }

  Integer getOne() {
    return 1;
  }

  int subPartlyNativeWrapped( int a, Integer b ) {
    return a - b;
  }

  int addPartlyWrapped( BigInteger a, int b ) {
    return a.intValue() + b;
  }

}
