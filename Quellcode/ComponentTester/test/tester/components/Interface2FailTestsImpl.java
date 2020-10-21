package tester.components;

import tester.querytypes.Interface2;

public class Interface2FailTestsImpl implements Interface2 {

  @Override
  public Boolean getTrue() {
    return false;
  }

  @Override
  public Boolean getFalse() {
    return true;
  }

  @Override
  public Integer getOne() {
    return 0;
  }

}
