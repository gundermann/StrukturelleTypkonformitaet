package matching.methods;

import static matching.methods.testmethods.MethodPool.getMethod;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.modules.ModuleMatchingInfo;

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
    Method checkMethod = getMethod( "getOneNativeWrapped" );
    Method queryMethod = getMethod( "getOne" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    for ( MethodMatchingInfo mmi : matchingInfos ) {
      assertThat( mmi.getSource(), equalTo( queryMethod ) );
      assertThat( mmi.getTarget(), equalTo( checkMethod ) );
      ModuleMatchingInfo rtMatchingInfo = mmi.getReturnTypeMatchingInfo();
      assertThat( rtMatchingInfo, notNullValue() );
      assertThat( rtMatchingInfo.getSource(), notNullValue() );
      assertThat( rtMatchingInfo.getSource(), equalTo( Integer.class ) );
      assertThat( rtMatchingInfo.getSourceDelegateAttribute(), notNullValue() );
      assertThat( rtMatchingInfo.getSourceDelegateAttribute(), equalTo( "value" ) );

      assertThat( rtMatchingInfo.getTarget(), notNullValue() );
      assertThat( rtMatchingInfo.getTarget(), equalTo( int.class ) );
      assertThat( rtMatchingInfo.getTargetDelegateAttribute(), nullValue() );

      Set<MethodMatchingInfo> rtMethodMI = rtMatchingInfo.getMethodMatchingInfos();
      assertThat( rtMethodMI, notNullValue() );
      assertThat( rtMethodMI.size(), equalTo( 0 ) ); // Denn die Typen sind identisch

      assertThat( mmi.getArgumentTypeMatchingInfos(), notNullValue() );
      assertThat( mmi.getArgumentTypeMatchingInfos().size(), equalTo( 0 ) );
    }
  }

  @Test
  public void test4_turned() {
    Method queryMethod = getMethod( "getOneNativeWrapped" );
    Method checkMethod = getMethod( "getOne" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    for ( MethodMatchingInfo mmi : matchingInfos ) {
      assertThat( mmi.getSource(), equalTo( queryMethod ) );
      assertThat( mmi.getTarget(), equalTo( checkMethod ) );
      ModuleMatchingInfo rtMatchingInfo = mmi.getReturnTypeMatchingInfo();
      assertThat( rtMatchingInfo, notNullValue() );
      assertThat( rtMatchingInfo.getTarget(), notNullValue() );
      assertThat( rtMatchingInfo.getTarget(), equalTo( Integer.class ) );
      assertThat( rtMatchingInfo.getTargetDelegateAttribute(), notNullValue() );
      assertThat( rtMatchingInfo.getTargetDelegateAttribute(), equalTo( "value" ) );

      assertThat( rtMatchingInfo.getSource(), notNullValue() );
      assertThat( rtMatchingInfo.getSource(), equalTo( int.class ) );
      assertThat( rtMatchingInfo.getSourceDelegateAttribute(), nullValue() );

      Set<MethodMatchingInfo> rtMethodMI = rtMatchingInfo.getMethodMatchingInfos();
      assertThat( rtMethodMI, notNullValue() );
      assertThat( rtMethodMI.size(), equalTo( 0 ) ); // Denn die Typen sind identisch

      assertThat( mmi.getArgumentTypeMatchingInfos(), notNullValue() );
      assertThat( mmi.getArgumentTypeMatchingInfos().size(), equalTo( 0 ) );
    }
  }

  @Test
  public void test5() {
    Method checkMethod = getMethod( "setBoolNativeWrapped" );
    Method queryMethod = getMethod( "setBool" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    for ( MethodMatchingInfo mmi : matchingInfos ) {
      assertThat( mmi.getSource(), equalTo( queryMethod ) );
      assertThat( mmi.getTarget(), equalTo( checkMethod ) );
      ModuleMatchingInfo rtMatchingInfo = mmi.getReturnTypeMatchingInfo();
      assertThat( rtMatchingInfo, notNullValue() );
      assertThat( rtMatchingInfo.getSource(), notNullValue() );
      assertThat( rtMatchingInfo.getSource(), equalTo( void.class ) );
      assertThat( rtMatchingInfo.getSourceDelegateAttribute(), nullValue() );

      assertThat( rtMatchingInfo.getTarget(), notNullValue() );
      assertThat( rtMatchingInfo.getTarget(), equalTo( void.class ) );
      assertThat( rtMatchingInfo.getTargetDelegateAttribute(), nullValue() );

      Set<MethodMatchingInfo> rtMethodMI = rtMatchingInfo.getMethodMatchingInfos();
      assertThat( rtMethodMI, notNullValue() );
      assertThat( rtMethodMI.size(), equalTo( 0 ) ); // Denn die Typen sind identisch

      Map<ParamPosition, ModuleMatchingInfo> argumentTypeMatchingInfos = mmi.getArgumentTypeMatchingInfos();
      assertThat( argumentTypeMatchingInfos, notNullValue() );
      assertThat( argumentTypeMatchingInfos.size(), equalTo( 1 ) );
      for ( ModuleMatchingInfo argMMI : argumentTypeMatchingInfos.values() ) {
        assertThat( argMMI, notNullValue() );
        assertThat( argMMI.getSource(), notNullValue() );
        assertThat( argMMI.getSourceDelegateAttribute(), nullValue() );
        assertThat( argMMI.getTarget(), notNullValue() );
        assertThat( argMMI.getTargetDelegateAttribute(), notNullValue() );
        assertThat( argMMI.getTargetDelegateAttribute(), equalTo( "value" ) );
        Set<MethodMatchingInfo> argMethodMI = argMMI.getMethodMatchingInfos();
        assertThat( argMethodMI, notNullValue() );
        assertThat( argMethodMI.size(), equalTo( 0 ) ); // Denn die Typen sind identisch
      }
    }
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
