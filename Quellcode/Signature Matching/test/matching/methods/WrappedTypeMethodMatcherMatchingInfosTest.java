package matching.methods;

import static matching.methods.testmethods.MethodPool.getMethod;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Map;
import java.util.Map.Entry;
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
    Method checkMethod = getMethod( "getTrue" );
    Method queryMethod = getMethod( "getTrue" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    for ( MethodMatchingInfo info : matchingInfos ) {
      ModuleMatchingInfo rtInfo = info.getReturnTypeMatchingInfo();
      assertThat( rtInfo, notNullValue() );
      assertThat( rtInfo.getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( rtInfo.getSourceDelegateAttribute(), nullValue() );
      assertThat( rtInfo.getTarget(), equalTo( queryMethod.getReturnType() ) );
      assertThat( rtInfo.getTargetDelegateAttribute(), nullValue() );

      assertThat( info.getArgumentTypeMatchingInfos(), notNullValue() );
      assertThat( info.getArgumentTypeMatchingInfos().size(), equalTo( 0 ) );
    }
  }

  @Test
  public void test2() {
    Method checkMethod = getMethod( "getTrue" );
    Method queryMethod = getMethod( "getFalse" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    for ( MethodMatchingInfo info : matchingInfos ) {
      ModuleMatchingInfo rtInfo = info.getReturnTypeMatchingInfo();
      assertThat( rtInfo, notNullValue() );
      assertThat( rtInfo.getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( rtInfo.getSourceDelegateAttribute(), nullValue() );
      assertThat( rtInfo.getTarget(), equalTo( queryMethod.getReturnType() ) );
      assertThat( rtInfo.getTargetDelegateAttribute(), nullValue() );

      assertThat( info.getArgumentTypeMatchingInfos(), notNullValue() );
      assertThat( info.getArgumentTypeMatchingInfos().size(), equalTo( 0 ) );
    }
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
    Method checkMethod = getMethod( "addOne" );
    Method queryMethod = getMethod( "subOne" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    for ( MethodMatchingInfo info : matchingInfos ) {
      ModuleMatchingInfo rtInfo = info.getReturnTypeMatchingInfo();
      assertThat( rtInfo, notNullValue() );
      assertThat( rtInfo.getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( rtInfo.getSourceDelegateAttribute(), nullValue() );
      assertThat( rtInfo.getTarget(), equalTo( queryMethod.getReturnType() ) );
      assertThat( rtInfo.getTargetDelegateAttribute(), nullValue() );

      Map<ParamPosition, ModuleMatchingInfo> argInfos = info.getArgumentTypeMatchingInfos();
      assertThat( argInfos, notNullValue() );
      assertThat( argInfos.size(), equalTo( 1 ) );

      for ( Entry<ParamPosition, ModuleMatchingInfo> argEntry : argInfos.entrySet() ) {
        assertThat( argEntry.getValue().getSource(),
            equalTo( queryMethod.getParameterTypes()[argEntry.getKey().getSourceParamPosition()] ) );
        assertThat( argEntry.getValue().getTarget(),
            equalTo( checkMethod.getParameterTypes()[argEntry.getKey().getTargetParamPosition()] ) );
      }
    }
  }

  @Test
  public void test7() {
    Method checkMethod = getMethod( "add" );
    Method queryMethod = getMethod( "sub" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    for ( MethodMatchingInfo info : matchingInfos ) {
      ModuleMatchingInfo rtInfo = info.getReturnTypeMatchingInfo();
      assertThat( rtInfo, notNullValue() );
      assertThat( rtInfo.getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( rtInfo.getSourceDelegateAttribute(), nullValue() );
      assertThat( rtInfo.getTarget(), equalTo( queryMethod.getReturnType() ) );
      assertThat( rtInfo.getTargetDelegateAttribute(), nullValue() );

      Map<ParamPosition, ModuleMatchingInfo> argInfos = info.getArgumentTypeMatchingInfos();
      assertThat( argInfos, notNullValue() );
      assertThat( argInfos.size(), equalTo( 2 ) );

      for ( Entry<ParamPosition, ModuleMatchingInfo> argEntry : argInfos.entrySet() ) {
        assertThat( argEntry.getValue().getSource(),
            equalTo( queryMethod.getParameterTypes()[argEntry.getKey().getSourceParamPosition()] ) );
        assertThat( argEntry.getValue().getTarget(),
            equalTo( checkMethod.getParameterTypes()[argEntry.getKey().getTargetParamPosition()] ) );
      }
    }
  }

  @Test
  public void test8() {
    Method checkMethod = getMethod( "addPartlyNativeWrapped" );
    Method queryMethod = getMethod( "subPartlyNativeWrapped" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    for ( MethodMatchingInfo mmi : matchingInfos ) {
      assertThat( mmi.getSource(), equalTo( queryMethod ) );
      assertThat( mmi.getTarget(), equalTo( checkMethod ) );
      ModuleMatchingInfo rtMatchingInfo = mmi.getReturnTypeMatchingInfo();
      assertThat( rtMatchingInfo, notNullValue() );
      assertThat( rtMatchingInfo.getSource(), notNullValue() );
      assertThat( rtMatchingInfo.getSource(), equalTo( int.class ) );
      assertThat( rtMatchingInfo.getSourceDelegateAttribute(), nullValue() );

      assertThat( rtMatchingInfo.getTarget(), notNullValue() );
      assertThat( rtMatchingInfo.getTarget(), equalTo( int.class ) );
      assertThat( rtMatchingInfo.getTargetDelegateAttribute(), nullValue() );

      Set<MethodMatchingInfo> rtMethodMI = rtMatchingInfo.getMethodMatchingInfos();
      assertThat( rtMethodMI, notNullValue() );
      assertThat( rtMethodMI.size(), equalTo( 0 ) ); // Denn die Typen sind identisch

      Map<ParamPosition, ModuleMatchingInfo> argumentTypeMatchingInfos = mmi.getArgumentTypeMatchingInfos();
      assertThat( argumentTypeMatchingInfos, notNullValue() );
      assertThat( argumentTypeMatchingInfos.size(), equalTo( 2 ) );
      for ( ModuleMatchingInfo argMMI : argumentTypeMatchingInfos.values() ) {
        assertThat( argMMI, notNullValue() );
        assertThat( argMMI.getSource(), notNullValue() );
        assertThat( argMMI.getTarget(), notNullValue() );
        if ( argMMI.getSource().equals( int.class ) ) {
          // wrapped
          assertThat( argMMI.getSourceDelegateAttribute(), nullValue() );
          assertThat( argMMI.getTargetDelegateAttribute(), notNullValue() );
          assertThat( argMMI.getTargetDelegateAttribute(), equalTo( "value" ) );
        }
        else if ( argMMI.getSource().equals( Integer.class ) ) {
          // wrapper
          assertThat( argMMI.getSourceDelegateAttribute(), notNullValue() );
          assertThat( argMMI.getSourceDelegateAttribute(), equalTo( "value" ) );
          assertThat( argMMI.getTargetDelegateAttribute(), nullValue() );
        }
        else {
          fail( "arg source must be int or Integer" );
        }
        Set<MethodMatchingInfo> argMethodMI = argMMI.getMethodMatchingInfos();
        assertThat( argMethodMI, notNullValue() );
        assertThat( argMethodMI.size(), equalTo( 0 ) ); // Denn die Typen sind identisch
      }
    }
  }

  // TODO Dieser Test zeigt eine Schwäche auf. Eigentlich sollte man annehmen, dass int in einen BigInteger überführbar
  // ist. (BigInteger::intValue)
  // Mit dem WrapperMatcher kommt man aber nicht darauf. Das bedeutet, es muss noch eine andere Kombinationsmöglichkeit
  // geben, die nicht auf innere Felder sondern auf Methoden und deren Rückgabewerte basiert. (BoxedMatcher?)
  // TODO und es geht sogar noch weiter: Wenn ich bspw. Integer als Source habe und BigInteger als Target, können diese
  // beiden Typen auchgemachted werden, indem in Betracht gezogen wird, dass sich beide auf int zurückführen lassen.
  // (UnifikationMatcher???)
  @Test
  public void test9() {
    Method checkMethod = getMethod( "addPartlyWrapped" );
    Method queryMethod = getMethod( "subPartlyWrapped" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 25 ) ); // BigInteger enthaelt 5 int Felder (also 25 Kombinationen)
    for ( MethodMatchingInfo mmi : matchingInfos ) {
      assertThat( mmi.getSource(), equalTo( queryMethod ) );
      assertThat( mmi.getTarget(), equalTo( checkMethod ) );
      ModuleMatchingInfo rtMatchingInfo = mmi.getReturnTypeMatchingInfo();
      assertThat( rtMatchingInfo, notNullValue() );
      assertThat( rtMatchingInfo.getSource(), notNullValue() );
      assertThat( rtMatchingInfo.getSource(), equalTo( int.class ) );
      assertThat( rtMatchingInfo.getSourceDelegateAttribute(), nullValue() );

      assertThat( rtMatchingInfo.getTarget(), notNullValue() );
      assertThat( rtMatchingInfo.getTarget(), equalTo( int.class ) );
      assertThat( rtMatchingInfo.getTargetDelegateAttribute(), nullValue() );

      Set<MethodMatchingInfo> rtMethodMI = rtMatchingInfo.getMethodMatchingInfos();
      assertThat( rtMethodMI, notNullValue() );
      assertThat( rtMethodMI.size(), equalTo( 0 ) ); // Denn die Typen sind identisch

      Map<ParamPosition, ModuleMatchingInfo> argumentTypeMatchingInfos = mmi.getArgumentTypeMatchingInfos();
      assertThat( argumentTypeMatchingInfos, notNullValue() );
      assertThat( argumentTypeMatchingInfos.size(), equalTo( 2 ) );
      for ( ModuleMatchingInfo argMMI : argumentTypeMatchingInfos.values() ) {
        assertThat( argMMI, notNullValue() );
        assertThat( argMMI.getSource(), notNullValue() );
        assertThat( argMMI.getTarget(), notNullValue() );
        if ( argMMI.getSource().equals( int.class ) ) {
          // wrapped
          assertThat( argMMI.getSourceDelegateAttribute(), nullValue() );
          assertThat( argMMI.getTargetDelegateAttribute(), notNullValue() );
        }
        else if ( argMMI.getSource().equals( BigInteger.class ) ) {
          // wrapper
          assertThat( argMMI.getSourceDelegateAttribute(), notNullValue() );
          assertThat( argMMI.getTargetDelegateAttribute(), nullValue() );
        }
        else {
          fail( "arg source must be int or Integer" );
        }
        Set<MethodMatchingInfo> argMethodMI = argMMI.getMethodMatchingInfos();
        assertThat( argMethodMI, notNullValue() );
        assertThat( argMethodMI.size(), equalTo( 0 ) ); // Denn die Typen sind identisch
      }
    }
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
