package tester.components;

import tester.querytypes.Interface2;

public class Interface2ConfirmTestsImpl implements Interface2 {

  @Override
  public Boolean getTrue() {
    return true;
  }

  @Override
  public Boolean getFalse() {
    return false;
  }

  @Override
  public Integer getOne() {
    return 1;
  }

}
