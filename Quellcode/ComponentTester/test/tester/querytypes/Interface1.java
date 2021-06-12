package tester.querytypes;

import java.math.BigInteger;

import tester.annotation.RequiredTypeTestReference;
import tester.querytypes.tests.Interface1Test1;
import tester.querytypes.tests.Interface1Test2;

@RequiredTypeTestReference( testClasses = { Interface1Test1.class, Interface1Test2.class } )
public interface Interface1 {

  boolean getTrue();

  boolean getFalse();

  int getOne();

  int addPartlyNativeWrapped( Integer a, int b );

  int subPartlyNativeWrapped( int a, Integer b );

  int addPartlyWrapped( BigInteger a, int b );

  int subPartlyWrapped( int a, BigInteger b );

}
