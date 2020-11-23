package tester.querytypes.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;

import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;
import tester.querytypes.Interface1;

public class Interface1Test2 {

  private Interface1 testInterface;

  @QueryTypeInstanceSetter
  public void setComponent( Interface1 i ) {
    this.testInterface = i;
  }

  @QueryTypeTest
  void addPartlyNativeWrapped() {
    assertThat( testInterface.addPartlyNativeWrapped( 1, 1 ), equalTo( 2 ) );
  }

  @QueryTypeTest
  void subPartlyNativeWrapped() {
    assertThat( testInterface.subPartlyNativeWrapped( 3, 1 ), equalTo( 2 ) );
  }

  @QueryTypeTest
  void addPartlyWrapped() {
    assertThat( testInterface.addPartlyWrapped( BigInteger.ONE, 1 ), equalTo( 2 ) );
  }

  @QueryTypeTest
  void subPartlyWrapped() {
    assertThat( testInterface.subPartlyWrapped( 3, BigInteger.ONE ), equalTo( 2 ) );
  }

}
