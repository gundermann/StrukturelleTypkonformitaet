package matching.methods;

import static matching.methods.testmethods.MethodPool.getMethod;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import matching.modules.ModuleMatchingInfo;

public class GenSpecMethodMatcherMatchingInfosTest {

  MethodMatcher matcher = new GenSpecMethodMatcher();

  @Test
  public void test1() {
    Method checkMethod = getMethod( "getTrue" );
    Method queryMethod = getMethod( "getTrue" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod,
        queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    matchingInfos.forEach( info -> {
      assertThat( info.getReturnTypeMatchingInfo(), notNullValue() );
      assertThat( info.getReturnTypeMatchingInfo().getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( info.getArgumentTypeMatchingInfos(), notNullValue() );
      assertThat( info.getArgumentTypeMatchingInfos().size(), equalTo( 0 ) );
    } );
  }

  @Test
  public void test2() {
    Method checkMethod = getMethod( "getTrue" );
    Method queryMethod = getMethod( "getFalse" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    matchingInfos.forEach( info -> {
      assertThat( info.getReturnTypeMatchingInfo(), notNullValue() );
      assertThat( info.getReturnTypeMatchingInfo().getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( info.getArgumentTypeMatchingInfos(), notNullValue() );
      assertThat( info.getArgumentTypeMatchingInfos().size(), equalTo( 0 ) );
    } );
  }

  @Test
  public void test3() {
    Method checkMethod = getMethod( "getTrue" );
    Method queryMethod = getMethod( "getOne" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test
  public void test4() {
    Method checkMethod = getMethod( "getOneNativeWrapped" );
    Method queryMethod = getMethod( "getOne" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test
  public void test5() {
    // Da die MethodMatchingInfoFactory eine Info erzeugt, sobald eine TypeMatchingInfo für den Returntype existiert,
    // wird hier auch ein Element erzeugt. Denn void== void
    Method checkMethod = getMethod( "setBool" );
    Method queryMethod = getMethod( "setBoolNativeWrapped" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
  }

  @Test
  public void test6() {
    Method checkMethod = getMethod( "addOne" );
    Method queryMethod = getMethod( "subOne" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    matchingInfos.forEach( info -> {
      assertThat( info.getReturnTypeMatchingInfo(), notNullValue() );
      assertThat( info.getReturnTypeMatchingInfo().getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( info.getReturnTypeMatchingInfo().getTarget(), equalTo( queryMethod.getReturnType() ) );
      assertThat( info.getArgumentTypeMatchingInfos(), notNullValue() );
      assertThat( info.getArgumentTypeMatchingInfos().size(), equalTo( 1 ) );
      assertThat( info.getArgumentTypeMatchingInfos().get( 0 ).getSource(),
          equalTo( queryMethod.getParameterTypes()[0] ) );
      assertThat( info.getArgumentTypeMatchingInfos().get( 0 ).getTarget(),
          equalTo( checkMethod.getParameterTypes()[0] ) );
    } );
  }

  @Test
  public void test7() {
    Method checkMethod = getMethod( "add" );
    Method queryMethod = getMethod( "sub" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    matchingInfos.forEach( info -> {
      ModuleMatchingInfo<?> returnTypeMatchingInfo = info.getReturnTypeMatchingInfo();
      assertThat( returnTypeMatchingInfo, notNullValue() );
      assertThat( returnTypeMatchingInfo.getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( returnTypeMatchingInfo.getTarget(), equalTo( queryMethod.getReturnType() ) );
      Map<Integer, ModuleMatchingInfo<?>> argumentTypeMatchingInfos = info.getArgumentTypeMatchingInfos();
      assertThat( argumentTypeMatchingInfos, notNullValue() );
      assertThat( argumentTypeMatchingInfos.size(), equalTo( 2 ) );
      int index = 0;
      assertThat( argumentTypeMatchingInfos.get( index ).getSource(),
          equalTo( queryMethod.getParameterTypes()[index] ) );
      assertThat( argumentTypeMatchingInfos.get( index ).getTarget(),
          equalTo( checkMethod.getParameterTypes()[index] ) );
      index = 1;
      assertThat( argumentTypeMatchingInfos.get( index ).getSource(),
          equalTo( queryMethod.getParameterTypes()[index] ) );
      assertThat( argumentTypeMatchingInfos.get( index ).getTarget(),
          equalTo( checkMethod.getParameterTypes()[index] ) );

    } );
  }

  @Test
  public void test8() {
    // Da die MethodMatchingInfoFactory eine Info erzeugt, sobald eine TypeMatchingInfo für den Returntype existiert,
    // wird hier auch ein Element erzeugt. Denn int == int
    Method checkMethod = getMethod( "addPartlyNativeWrapped" );
    Method queryMethod = getMethod( "subPartlyNativeWrapped" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
  }

  @Test
  public void test9() {
    // Da die MethodMatchingInfoFactory eine Info erzeugt, sobald eine TypeMatchingInfo für den Returntype existiert,
    // wird hier auch ein Element erzeugt. Denn int == int
    Method checkMethod = getMethod( "addPartlyWrapped" );
    Method queryMethod = getMethod( "subPartlyWrapped" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
  }

  @Test
  public void test10() {
    Method checkMethod = getMethod( "addSpec" );
    Method queryMethod = getMethod( "addGen" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size() > 0, equalTo( true ) );
    for ( MethodMatchingInfo info : matchingInfos ) {
      ModuleMatchingInfo<?> returnTypeMatchingInfo = info.getReturnTypeMatchingInfo();
      assertThat( returnTypeMatchingInfo, notNullValue() );
      assertThat( returnTypeMatchingInfo.getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( returnTypeMatchingInfo.getTarget(), equalTo( queryMethod.getReturnType() ) );
      Map<Integer, ModuleMatchingInfo<?>> argumentTypeMatchingInfos = info.getArgumentTypeMatchingInfos();
      assertThat( argumentTypeMatchingInfos, notNullValue() );
      assertThat( argumentTypeMatchingInfos.size(), equalTo( 2 ) );

      // Parameter prüfen
      assertThat( argumentTypeMatchingInfos.get( 0 ).getSource(),
          equalTo( queryMethod.getParameterTypes()[0] ) );
      assertThat( argumentTypeMatchingInfos.get( 0 ).getTarget(),
          equalTo( checkMethod.getParameterTypes()[0] ) );
      Set<MethodMatchingInfo> methodMatchingInfos1 = argumentTypeMatchingInfos.get( 0 ).getMethodMatchingInfos();

      assertThat( argumentTypeMatchingInfos.get( 1 ).getSource(),
          equalTo( queryMethod.getParameterTypes()[1] ) );
      assertThat( argumentTypeMatchingInfos.get( 1 ).getTarget(),
          equalTo( checkMethod.getParameterTypes()[1] ) );
      Set<MethodMatchingInfo> methodMatchingInfos2 = argumentTypeMatchingInfos.get( 1 ).getMethodMatchingInfos();

      assertThat( methodMatchingInfos1.isEmpty() ^ methodMatchingInfos2.isEmpty(), equalTo( true ) );
    }
  }

  @Test
  public void test11() {
    Method checkMethod = getMethod( "setBoolNativeWrapped" );
    Method queryMethod = getMethod( "setObject" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size() > 0, equalTo( true ) );
    for ( MethodMatchingInfo info : matchingInfos ) {
      assertThat( info.getReturnTypeMatchingInfo(), notNullValue() );
      assertThat( info.getReturnTypeMatchingInfo().getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( info.getReturnTypeMatchingInfo().getTarget(), equalTo( queryMethod.getReturnType() ) );
      assertThat( info.getArgumentTypeMatchingInfos(), notNullValue() );
      assertThat( info.getArgumentTypeMatchingInfos().size(), equalTo( 1 ) );
      assertThat( info.getArgumentTypeMatchingInfos().get( 0 ).getSource(),
          equalTo( queryMethod.getParameterTypes()[0] ) );
      assertThat( info.getArgumentTypeMatchingInfos().get( 0 ).getTarget(),
          equalTo( checkMethod.getParameterTypes()[0] ) );
    }
  }

  @Test
  public void test12() {
    Method checkMethod = getMethod( "addSpec" );
    Method queryMethod = getMethod( "addSpecReturnSpec" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size() > 0, equalTo( true ) );
    for ( MethodMatchingInfo info : matchingInfos ) {
      // Returntype prüfen
      ModuleMatchingInfo<?> returnTypeMatchingInfo = info.getReturnTypeMatchingInfo();
      assertThat( returnTypeMatchingInfo, notNullValue() );
      assertThat( returnTypeMatchingInfo.getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( returnTypeMatchingInfo.getTarget(), equalTo( queryMethod.getReturnType() ) );
      Set<MethodMatchingInfo> methodMatchingInfosOfReturnType = returnTypeMatchingInfo.getMethodMatchingInfos();
      assertThat( methodMatchingInfosOfReturnType.isEmpty(), equalTo( true ) );

      // Parameter prüfen
      Map<Integer, ModuleMatchingInfo<?>> argumentTypeMatchingInfos = info.getArgumentTypeMatchingInfos();
      assertThat( argumentTypeMatchingInfos, notNullValue() );
      assertThat( argumentTypeMatchingInfos.size(), equalTo( 2 ) );
      int index = 0;
      assertThat( argumentTypeMatchingInfos.get( index ).getSource(),
          equalTo( queryMethod.getParameterTypes()[index] ) );
      assertThat( argumentTypeMatchingInfos.get( index ).getTarget(),
          equalTo( checkMethod.getParameterTypes()[index] ) );
      index = 1;
      assertThat( argumentTypeMatchingInfos.get( index ).getSource(),
          equalTo( queryMethod.getParameterTypes()[index] ) );
      assertThat( argumentTypeMatchingInfos.get( index ).getTarget(),
          equalTo( checkMethod.getParameterTypes()[index] ) );

    }
  }

}
