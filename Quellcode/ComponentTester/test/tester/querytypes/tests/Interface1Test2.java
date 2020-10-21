package tester.querytypes.tests;

import tester.annotation.QueryTypeInstanceSetter;
import tester.querytypes.Interface1;

public class Interface1Test2 {

  private Interface1 testInterface;

  @QueryTypeInstanceSetter
  public void setComponent( Interface1 i ) {
    this.testInterface = i;
  }

}
