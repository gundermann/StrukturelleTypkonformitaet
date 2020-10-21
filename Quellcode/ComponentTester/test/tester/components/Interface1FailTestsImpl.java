package tester.components;

import java.math.BigInteger;

import tester.querytypes.Interface1;

public class Interface1FailTestsImpl implements Interface1 {

  @Override
  public int addPartlyNativeWrapped( Integer a, int b ) {
    return a + b;
  }

  @Override
  public int subPartlyNativeWrapped( int a, Integer b ) {
    return a - b;
  }

  @Override
  public int addPartlyWrapped( BigInteger a, int b ) {
    return a.intValue() + b;
  }

  @Override
  public int subPartlyWrapped( int a, BigInteger b ) {
    return a + b.intValue();
  }

  @Override
  public boolean getTrue() {
    return true;
  }

  @Override
  public boolean getFalse() {
    return false;
  }

  @Override
  public int getOne() {
    return 1;
  }

}
