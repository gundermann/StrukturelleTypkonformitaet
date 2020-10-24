package matching.methods;

import static matching.methods.testmethods.MethodPool.getMethod;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.Set;

import org.junit.Test;

public class ExactMethodMatcherMatchingInfosTest {

  MethodMatcher matcher = new ExactMethodMatcher();

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
    // Auch wenn die beiden hinsichtlich des Matchers nicht übereinstimmen, kommt es hier zu einem Ergebnis.
    // Es wird nämlich nicht nocheinmal geprüft, ob die Typen matchen
    Method sourceMethod = getMethod( "getTrue" );
    Method targetMethod = getMethod( "getOne" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
  }

  @Test
  public void test4() {
    // Auch wenn die beiden hinsichtlich des Matchers nicht übereinstimmen, kommt es hier zu einem Ergebnis.
    // Es wird nämlich nicht nocheinmal geprüft, ob die Typen matchen
    Method sourceMethod = getMethod( "getOneNativeWrapped" );
    Method targetMethod = getMethod( "getOne" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
  }

  @Test
  public void test5() {
    // Auch wenn die beiden hinsichtlich des Matchers nicht übereinstimmen, kommt es hier zu einem Ergebnis.
    // Es wird nämlich nicht nocheinmal geprüft, ob die Typen matchen
    Method sourceMethod = getMethod( "setBool" );
    Method targetMethod = getMethod( "setBoolNativeWrapped" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
  }

  @Test
  public void test6() {
    Method sourceMethod = getMethod( "addOne" );
    Method targetMethod = getMethod( "subOne" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
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
    Method sourceMethod = getMethod( "add" );
    Method targetMethod = getMethod( "sub" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    matchingInfos.forEach( info -> {
      assertThat( info.getReturnTypeMatchingInfo(), notNullValue() );
      assertThat( info.getReturnTypeMatchingInfo().getSource(), equalTo( sourceMethod.getReturnType() ) );
      assertThat( info.getArgumentTypeMatchingInfos(), notNullValue() );
      assertThat( info.getArgumentTypeMatchingInfos().size(), equalTo( 2 ) );
      assertThat( info.getArgumentTypeMatchingInfos().get( 0 ).getSource(),
          equalTo( sourceMethod.getParameterTypes()[0] ) );
      assertThat( info.getArgumentTypeMatchingInfos().get( 0 ).getTarget(),
          equalTo( targetMethod.getParameterTypes()[0] ) );
      assertThat( info.getArgumentTypeMatchingInfos().get( 1 ).getSource(),
          equalTo( sourceMethod.getParameterTypes()[1] ) );
      assertThat( info.getArgumentTypeMatchingInfos().get( 1 ).getTarget(),
          equalTo( targetMethod.getParameterTypes()[1] ) );
    } );
  }

  @Test
  public void test8() {
    // Auch wenn die beiden hinsichtlich des Matchers nicht übereinstimmen, kommt es hier zu einem Ergebnis.
    // Es wird nämlich nicht nocheinmal geprüft, ob die Typen matchen
    Method sourceMethod = getMethod( "addPartlyNativeWrapped" );
    Method targetMethod = getMethod( "subPartlyNativeWrapped" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
  }

  @Test
  public void test9() {
    // Auch wenn die beiden hinsichtlich des Matchers nicht übereinstimmen, kommt es hier zu einem Ergebnis.
    // Es wird nämlich nicht nocheinmal geprüft, ob die Typen matchen
    Method sourceMethod = getMethod( "addPartlyWrapped" );
    Method targetMethod = getMethod( "subPartlyWrapped" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
  }

  @Test
  public void test10() {
    // Auch wenn die beiden hinsichtlich des Matchers nicht übereinstimmen, kommt es hier zu einem Ergebnis.
    // Es wird nämlich nicht nocheinmal geprüft, ob die Typen matchen
    Method sourceMethod = getMethod( "addSpec" );
    Method targetMethod = getMethod( "addGen" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
  }

  @Test( expected = ArrayIndexOutOfBoundsException.class )
  public void test11() {
    // Dieser Test ist interessant, weil hier unterschiedliche Anzahlen von Parametern verwendet werden.
    // Das Ergebnis sollte hier wirklich eine Exception sein
    Method sourceMethod = getMethod( "addSpec" );
    Method targetMethod = getMethod( "getTrue" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

}
