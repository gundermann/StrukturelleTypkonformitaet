package tester.querytypes.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import matching.modules.testmodules.Interface2;
import tester.annotation.QueryTypeInstanceSetter;

public class Interface2Test {

  private Interface2 testInterface;

  @QueryTypeInstanceSetter
  public void setComponent( Interface2 i ) {
    this.testInterface = i;
  }

  @Test
  void getOne() {
    assertTrue( testInterface.getOne() == 1 );
  }

  @Test
  void getTrue() {
    assertTrue( testInterface.getTrue() );
  }

  @Test
  void getFalse() {
    assertTrue( !testInterface.getTrue() );
  }

}
