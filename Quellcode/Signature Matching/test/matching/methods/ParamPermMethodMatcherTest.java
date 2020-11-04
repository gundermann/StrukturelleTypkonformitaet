package matching.methods;

import static matching.methods.testmethods.MethodPool.getMethod;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

public class ParamPermMethodMatcherTest {

  MethodMatcher matcher = new ParamPermMethodMatcher( () -> new ExactMethodMatcher() );

  @Test
  public void test1() {
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( getMethod( "getTrue" ),
        getMethod( "getTrue" ) );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    matchingInfos.forEach( info -> {
      assertThat( info.getReturnTypeMatchingInfo(), notNullValue() );
      assertThat( info.getReturnTypeMatchingInfo().getSource(), equalTo( getMethod( "getTrue" ).getReturnType() ) );
      assertThat( info.getArgumentTypeMatchingInfos(), notNullValue() );
      assertThat( info.getArgumentTypeMatchingInfos().size(), equalTo( 0 ) );
    } );
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
    // assertFalse( matcher.matches( getMethod( "getOneNativeWrapped" ), getMethod( "getOne" ) ) );
  }

  @Test
  public void test5() {
    // assertFalse( matcher.matches( getMethod( "setBool" ), getMethod( "setBoolNativeWrapped" ) ) );
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
    // Das dieser Test richtig ist, war eigentlich nicht meine Intension.
    // Aber es ist nachvollziehbar, dass das Ergebnis durch eine Parameter-Permutation positiv ausfällt.
    assertTrue( matcher.matches( getMethod( "addSpec" ), getMethod( "addGen" ) ) );
  }
}
