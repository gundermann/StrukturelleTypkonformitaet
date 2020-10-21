package tester.querytypes;

import tester.annotation.QueryTypeTestReference;
import tester.querytypes.tests.Interface2Test;

@QueryTypeTestReference( testClasses = Interface2Test.class )
public interface Interface2 {

  Boolean getTrue();

  Boolean getFalse();

  Integer getOne();

}
