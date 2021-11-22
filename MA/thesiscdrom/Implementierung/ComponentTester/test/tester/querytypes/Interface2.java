package tester.querytypes;

import api.RequiredTypeTestReference;
import tester.querytypes.tests.Interface2Test;

@RequiredTypeTestReference( testClasses = Interface2Test.class )
public interface Interface2 {

  Boolean getTrue();

  Boolean getFalse();

  Integer getOne();

}
