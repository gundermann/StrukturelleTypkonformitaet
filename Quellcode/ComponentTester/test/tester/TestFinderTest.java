package tester;

import java.math.BigInteger;

import org.junit.Test;

import tester.finder.TestFinder;
import tester.querytypes.Interface1;

class TestFinderTest {

  @Test
  public void findTests_interface1() {
    TestFinder<Interface1> testFinder = new TestFinder<>();
    ImplementedInterface1 component = new ImplementedInterface1();
    testFinder.findTestClassesOfComponent( component );
  }

  private static class ImplementedInterface1 implements Interface1 {

    @Override
    public boolean getTrue() {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public boolean getFalse() {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public int getOne() {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public int addPartlyNativeWrapped( Integer a, int b ) {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public int subPartlyNativeWrapped( int a, Integer b ) {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public int addPartlyWrapped( BigInteger a, int b ) {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public int subPartlyWrapped( int a, BigInteger b ) {
      // TODO Auto-generated method stub
      return 0;
    }

  }
}
