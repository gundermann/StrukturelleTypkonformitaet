package matching.methods;

import static matching.methods.testmethods.MethodPool.getMethod;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import matching.modules.ExactTypeMatcher;
import matching.modules.WrappedTypeMatcher;

public class WrappedTypeMethodMatcherTest {
  MethodMatcher matcher;

  @Before
  public void setup() {
    matcher = new CommonMethodMatcher( () -> new WrappedTypeMatcher( () -> new ExactTypeMatcher() ) );
  }

  @Test
  public void test1() {
    assertFalse( matcher.matches( getMethod( "getTrue" ), getMethod( "getTrue" ) ) );
  }

  @Test
  public void test2() {
    assertFalse( matcher.matches( getMethod( "getTrue" ), getMethod( "getFalse" ) ) );
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
    assertFalse( matcher.matches( getMethod( "setBool" ), getMethod( "setBoolNativeWrapped" ) ) );
  }

  @Test
  public void test6() {
    assertFalse( matcher.matches( getMethod( "addOne" ), getMethod( "subOne" ) ) );
  }

  @Test
  public void test7() {
    assertFalse( matcher.matches( getMethod( "add" ), getMethod( "sub" ) ) );
  }

  @Test
  public void test8() {
    assertFalse( matcher.matches( getMethod( "addPartlyNativeWrapped" ), getMethod( "subPartlyNativeWrapped" ) ) );
  }

  @Test
  public void test9() {
    assertFalse( matcher.matches( getMethod( "addPartlyWrapped" ), getMethod( "subPartlyWrapped" ) ) );
  }

  @Test
  public void test10() {
    assertFalse( matcher.matches( getMethod( "addSpec" ), getMethod( "addGen" ) ) );
  }

  @Test
  public void test11() {
    assertFalse( matcher.matches( getMethod( "setBool" ), getMethod( "setObject" ) ) );
  }
}
