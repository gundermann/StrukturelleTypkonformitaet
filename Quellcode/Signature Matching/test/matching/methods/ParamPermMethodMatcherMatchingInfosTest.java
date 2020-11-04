package matching.methods;

import static matching.methods.testmethods.MethodPool.getMethod;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Set;

import org.junit.Test;

public class ParamPermMethodMatcherMatchingInfosTest {

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
    Method sourceMethod = getMethod( "getTrue" );
    Method targetMethod = getMethod( "getFalse" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    matchingInfos.forEach( info -> {
      assertThat( info.getReturnTypeMatchingInfo(), notNullValue() );
      assertThat( info.getReturnTypeMatchingInfo().getSource(), equalTo( sourceMethod.getReturnType() ) );
      assertThat( info.getArgumentTypeMatchingInfos(), notNullValue() );
      assertThat( info.getArgumentTypeMatchingInfos().size(), equalTo( 0 ) );
    } );
  }

  @Test
  public void test3() {
    Method sourceMethod = getMethod( "getTrue" );
    Method targetMethod = getMethod( "getOne" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test
  public void test4() {
    Method sourceMethod = getMethod( "getOneNativeWrapped" );
    Method targetMethod = getMethod( "getOne" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test
  public void test5() {
    Method sourceMethod = getMethod( "setBool" );
    Method targetMethod = getMethod( "setBoolNativeWrapped" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test
  public void test6() {
    Method sourceMethod = getMethod( "addOne" );
    Method targetMethod = getMethod( "subOne" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 2 ) );
    matchingInfos.forEach( info -> {
      assertThat( info.getReturnTypeMatchingInfo(), notNullValue() );
      assertThat( info.getReturnTypeMatchingInfo().getSource(), equalTo( sourceMethod.getReturnType() ) );
      assertThat( info.getArgumentTypeMatchingInfos(), notNullValue() );
      assertThat( info.getArgumentTypeMatchingInfos().size(), equalTo( 1 ) );
      assertThat( info.getArgumentTypeMatchingInfos().get( 0 ).getSource(),
          equalTo( sourceMethod.getParameterTypes()[0] ) );
      assertThat( info.getArgumentTypeMatchingInfos().get( 0 ).getTarget(),
          equalTo( targetMethod.getParameterTypes()[0] ) );
    } );
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
