package matching.methods;

import static matching.methods.testmethods.MethodPool.getMethod;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class WrappedTypeMethodMatcherMatchingInfosTest {
  MethodMatcher matcher;

  @Before
  public void setup() {
    matcher = new WrappedTypeMethodMatcher( () -> new ExactMethodMatcher() );
  }

  @Test
  public void test1() {
    // assertTrue( matcher.matches( getMethod( "getTrue" ), getMethod( "getTrue" ) ) );
  }

  @Test
  public void test2() {
    // assertTrue( matcher.matches( getMethod( "getTrue" ), getMethod( "getFalse" ) ) );
  }

  @Test
  public void test3() {
    // assertFalse( matcher.matches( getMethod( "getTrue" ), getMethod( "getOne" ) ) );
  }

  @Test
  public void test4() {
    Method sourceMethod = getMethod( "getOneNativeWrapped" );
    Method targetMethod = getMethod( "getOne" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    // TODO

  }

  @Test
  public void test5() {
    // assertTrue( matcher.matches( getMethod( "setBool" ), getMethod( "setBoolNativeWrapped" ) ) );
  }

  @Test
  public void test6() {
    // assertTrue( matcher.matches( getMethod( "addOne" ), getMethod( "subOne" ) ) );
  }

  @Test
  public void test7() {
    // assertTrue( matcher.matches( getMethod( "add" ), getMethod( "sub" ) ) );
  }

  @Test
  public void test8() {
    // assertTrue( matcher.matches( getMethod( "addPartlyNativeWrapped" ), getMethod( "subPartlyNativeWrapped" ) ) );
  }

  @Test
  public void test9() {
    // assertTrue( matcher.matches( getMethod( "addPartlyWrapped" ), getMethod( "subPartlyWrapped" ) ) );
  }

  @Test
  public void test10() {
    Method sourceMethod = getMethod( "addSpec" );
    Method targetMethod = getMethod( "addGen" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test
  public void test11() {
    Method sourceMethod = getMethod( "setBool" );
    Method targetMethod = getMethod( "setObject" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }
}
