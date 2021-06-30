package tester.querytypes.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import api.RequiredTypeInstanceSetter;
import api.RequiredTypeTest;
import tester.querytypes.Interface1;

public class Interface1Test1 {

  private Interface1 testInterface;

  @RequiredTypeInstanceSetter
  public void setComponent( Interface1 i ) {
    this.testInterface = i;
  }

  @RequiredTypeTest
  void getOne() {
    assertThat( testInterface.getOne(), equalTo( 1 ) );
  }

  @RequiredTypeTest
  void getTrue() {
    assertThat( testInterface.getTrue(), equalTo( true ) );
  }

  @RequiredTypeTest
  void getFalse() {
    assertThat( testInterface.getFalse(), equalTo( false ) );
  }

}
