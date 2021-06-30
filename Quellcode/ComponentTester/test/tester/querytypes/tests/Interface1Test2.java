package tester.querytypes.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;

import api.RequiredTypeInstanceSetter;
import api.RequiredTypeTest;
import tester.querytypes.Interface1;

public class Interface1Test2 {

  private Interface1 testInterface;

  @RequiredTypeInstanceSetter
  public void setComponent( Interface1 i ) {
    this.testInterface = i;
  }

  @RequiredTypeTest
  public void addPartlyNativeWrapped() {
    assertThat( testInterface.addPartlyNativeWrapped( 1, 1 ), equalTo( 2 ) );
  }

  @RequiredTypeTest
  public void subPartlyNativeWrapped() {
    assertThat( testInterface.subPartlyNativeWrapped( 3, 1 ), equalTo( 2 ) );
  }

  @RequiredTypeTest
  public void addPartlyWrapped() {
    assertThat( testInterface.addPartlyWrapped( BigInteger.ONE, 1 ), equalTo( 2 ) );
  }

  @RequiredTypeTest
  public void subPartlyWrapped() {
    assertThat( testInterface.subPartlyWrapped( 3, BigInteger.ONE ), equalTo( 2 ) );
  }

}
