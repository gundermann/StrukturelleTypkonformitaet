package tester.querytypes.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;
import tester.querytypes.Interface1;

public class Interface1Test1 {

  private Interface1 testInterface;

  @QueryTypeInstanceSetter
  public void setComponent( Interface1 i ) {
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
