package tester.querytypes.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import tester.annotation.QueryTypeInstanceSetter;
import tester.querytypes.Interface2;

public class Interface2Test {

  private Interface2 testInterface;

  @QueryTypeInstanceSetter
  public void setComponent( Interface2 i ) {
    this.testInterface = i;
  }

  @Test
  void getOne() {
    assertThat( testInterface.getOne(), equalTo( 1 ) );
  }

  @Test
  void getTrue() {
    assertThat( testInterface.getTrue(), equalTo( true ) );
  }

  @Test
  void getFalse() {
    assertThat( testInterface.getFalse(), equalTo( false ) );
  }

}
