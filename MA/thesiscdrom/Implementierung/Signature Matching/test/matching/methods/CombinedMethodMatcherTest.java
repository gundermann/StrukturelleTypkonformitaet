package matching.methods;

import static matching.methods.testmethods.MethodPool.getMethod;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods.CombinedMethodMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods.MethodMatcher;

public class CombinedMethodMatcherTest {

  MethodMatcher matcher = new CombinedMethodMatcher();

  @Test
  public void test1() {
    assertTrue( matcher.matches( getMethod( "getTrue" ), getMethod( "getTrue" ) ) );
  }

  @Test
  public void test2() {
    assertTrue( matcher.matches( getMethod( "getTrue" ), getMethod( "getFalse" ) ) );
  }

  @Test
  public void test3() {
    assertFalse( matcher.matches( getMethod( "getTrue" ), getMethod( "getOne" ) ) );
  }

  @Test
  public void test4() {
    assertTrue( matcher.matches( getMethod( "getOneNativeWrapped" ), getMethod( "getOne" ) ) );
  }

  @Test
  public void test5() {
    assertTrue( matcher.matches( getMethod( "setBool" ), getMethod( "setBoolNativeWrapped" ) ) );
  }

  @Test
  public void test6() {
    assertTrue( matcher.matches( getMethod( "addOne" ), getMethod( "subOne" ) ) );
  }

  @Test
  public void test7() {
    assertTrue( matcher.matches( getMethod( "add" ), getMethod( "sub" ) ) );
  }

  @Test
  public void test8() {
    assertTrue( matcher.matches( getMethod( "addPartlyNativeWrapped" ), getMethod( "subPartlyNativeWrapped" ) ) );
  }

  @Test
  public void test9() {
    assertTrue( matcher.matches( getMethod( "addPartlyWrapped" ), getMethod( "subPartlyWrapped" ) ) );
  }

  @Test
  public void test10() {
    assertTrue( matcher.matches( getMethod( "addSpec" ), getMethod( "addGen" ) ) );
  }

  @Test
  public void test11() {
    assertTrue( matcher.matches( getMethod( "setBool" ), getMethod( "setObject" ) ) );
  }
}