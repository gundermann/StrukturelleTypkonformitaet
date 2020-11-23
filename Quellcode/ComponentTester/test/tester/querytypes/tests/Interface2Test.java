package tester.querytypes.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;
import tester.querytypes.Interface2;

public class Interface2Test {

  private Interface2 testInterface;

  @QueryTypeInstanceSetter
  public void setComponent( Interface2 i ) {
    this.testInterface = i;
  }

  @QueryTypeTest
  void getOne() {
    assertThat( testInterface.getOne(), equalTo( 1 ) );
  }

  @QueryTypeTest
  void getTrue() {
    assertThat( testInterface.getTrue(), equalTo( true ) );
  }

  @QueryTypeTest
  void getFalse() {
    assertThat( testInterface.getFalse(), equalTo( false ) );
  }

}
