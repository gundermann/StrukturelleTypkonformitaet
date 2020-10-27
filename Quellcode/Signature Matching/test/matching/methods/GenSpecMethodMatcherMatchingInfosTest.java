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
    Method sourceMethod = getMethod( "getTrue" );
    Method targetMethod = getMethod( "getTrue" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod,
        targetMethod );
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
    // Da die MethodMatchingInfoFactory eine Info erzeugt, sobald eine TypeMatchingInfo für den Returntype existiert,
    // wird hier auch ein Element erzeugt. Denn void== void
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
      ModuleMatchingInfo<?> returnTypeMatchingInfo = info.getReturnTypeMatchingInfo();
      assertThat( returnTypeMatchingInfo, notNullValue() );
      assertThat( returnTypeMatchingInfo.getSource(), equalTo( sourceMethod.getReturnType() ) );
      Map<Integer, ModuleMatchingInfo<?>> argumentTypeMatchingInfos = info.getArgumentTypeMatchingInfos();
      assertThat( argumentTypeMatchingInfos, notNullValue() );
      assertThat( argumentTypeMatchingInfos.size(), equalTo( 2 ) );
      int index = 0;
      assertThat( argumentTypeMatchingInfos.get( index ).getSource(),
          equalTo( sourceMethod.getParameterTypes()[index] ) );
      assertThat( argumentTypeMatchingInfos.get( index ).getTarget(),
          equalTo( targetMethod.getParameterTypes()[index] ) );
      index = 1;
      assertThat( argumentTypeMatchingInfos.get( index ).getSource(),
          equalTo( sourceMethod.getParameterTypes()[index] ) );
      assertThat( argumentTypeMatchingInfos.get( index ).getTarget(),
          equalTo( targetMethod.getParameterTypes()[index] ) );

    } );
  }

  @Test
  public void test8() {
    // Da die MethodMatchingInfoFactory eine Info erzeugt, sobald eine TypeMatchingInfo für den Returntype existiert,
    // wird hier auch ein Element erzeugt. Denn int == int
    Method sourceMethod = getMethod( "addPartlyNativeWrapped" );
    Method targetMethod = getMethod( "subPartlyNativeWrapped" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
  }

  @Test
  public void test9() {
    // Da die MethodMatchingInfoFactory eine Info erzeugt, sobald eine TypeMatchingInfo für den Returntype existiert,
    // wird hier auch ein Element erzeugt. Denn int == int
    Method sourceMethod = getMethod( "addPartlyWrapped" );
    Method targetMethod = getMethod( "subPartlyWrapped" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
  }

  @Test
  public void test10() {
    Method sourceMethod = getMethod( "addSpec" );
    Method targetMethod = getMethod( "addGen" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    matchingInfos.forEach( info -> {
      ModuleMatchingInfo<?> returnTypeMatchingInfo = info.getReturnTypeMatchingInfo();
      assertThat( returnTypeMatchingInfo, notNullValue() );
      assertThat( returnTypeMatchingInfo.getSource(), equalTo( sourceMethod.getReturnType() ) );
      Map<Integer, ModuleMatchingInfo<?>> argumentTypeMatchingInfos = info.getArgumentTypeMatchingInfos();
      assertThat( argumentTypeMatchingInfos, notNullValue() );
      assertThat( argumentTypeMatchingInfos.size(), equalTo( 2 ) );
      int index = 0;
      assertThat( argumentTypeMatchingInfos.get( index ).getSource(),
          equalTo( sourceMethod.getParameterTypes()[index] ) );
      assertThat( argumentTypeMatchingInfos.get( index ).getTarget(),
          equalTo( targetMethod.getParameterTypes()[index] ) );
      index = 1;
      assertThat( argumentTypeMatchingInfos.get( index ).getSource(),
          equalTo( sourceMethod.getParameterTypes()[index] ) );
      assertThat( argumentTypeMatchingInfos.get( index ).getTarget(),
          equalTo( targetMethod.getParameterTypes()[index] ) );

    } );
  }

  @Test
  public void test11() {
    Method sourceMethod = getMethod( "setBool" );
    Method targetMethod = getMethod( "setObject" );
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
  public void test12() {
    Method checkMethod = getMethod( "addSpec" );
    Method queryMethod = getMethod( "addSpecReturnSpec" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    matchingInfos.forEach( info -> {
      ModuleMatchingInfo<?> returnTypeMatchingInfo = info.getReturnTypeMatchingInfo();
      assertThat( returnTypeMatchingInfo, notNullValue() );
      assertThat( returnTypeMatchingInfo.getSource(), equalTo( queryMethod.getReturnType() ) );
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
  public void test13() {
    Method queryMethod = getMethod( "addSpec" );
    Method checkMethod = getMethod( "addSpecReturnSpec" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    matchingInfos.forEach( info -> {
      ModuleMatchingInfo<?> returnTypeMatchingInfo = info.getReturnTypeMatchingInfo();
      assertThat( returnTypeMatchingInfo, notNullValue() );
      assertThat( returnTypeMatchingInfo.getSource(), equalTo( queryMethod.getReturnType() ) );
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
}
