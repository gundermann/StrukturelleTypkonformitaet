package matching.methods;

import static matching.methods.testmethods.MethodPool.getMethod;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.modules.ModuleMatchingInfo;

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
    Method checkMethod = getMethod( "addOne" );
    Method queryMethod = getMethod( "subOne" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    matchingInfos.forEach( info -> {
      assertThat( info.getReturnTypeMatchingInfo(), notNullValue() );
      assertThat( info.getReturnTypeMatchingInfo().getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( info.getArgumentTypeMatchingInfos(), notNullValue() );
      assertThat( info.getArgumentTypeMatchingInfos().size(), equalTo( 1 ) );

      Iterator<Entry<ParamPosition, ModuleMatchingInfo>> iterator = info.getArgumentTypeMatchingInfos().entrySet()
          .iterator();
      Entry<ParamPosition, ModuleMatchingInfo> argInfoEntry1 = iterator.next();
      assertThat( argInfoEntry1.getValue().getSource(),
          equalTo( queryMethod.getParameterTypes()[argInfoEntry1.getKey().getSourceParamPosition()] ) );
      assertThat( argInfoEntry1.getValue().getTarget(),
          equalTo( checkMethod.getParameterTypes()[argInfoEntry1.getKey().getTargetParamPosition()] ) );
    } );
  }

  @Test
  public void test7() {
    Method checkMethod = getMethod( "add" );
    Method queryMethod = getMethod( "sub" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 2 ) );
    matchingInfos.forEach( info -> {
      ModuleMatchingInfo returnTypeMatchingInfo = info.getReturnTypeMatchingInfo();
      assertThat( returnTypeMatchingInfo, notNullValue() );
      assertThat( returnTypeMatchingInfo.getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( returnTypeMatchingInfo.getTarget(), equalTo( queryMethod.getReturnType() ) );
      Map<ParamPosition, ModuleMatchingInfo> argumentTypeMatchingInfos = info.getArgumentTypeMatchingInfos();
      assertThat( argumentTypeMatchingInfos, notNullValue() );
      assertThat( argumentTypeMatchingInfos.size(), equalTo( 2 ) );

      Iterator<Entry<ParamPosition, ModuleMatchingInfo>> iterator = info.getArgumentTypeMatchingInfos().entrySet()
          .iterator();
      while ( iterator.hasNext() ) {
        Entry<ParamPosition, ModuleMatchingInfo> argInfoEntry1 = iterator.next();
        assertThat( argInfoEntry1.getValue().getSource(),
            equalTo( queryMethod.getParameterTypes()[argInfoEntry1.getKey().getSourceParamPosition()] ) );
        assertThat( argInfoEntry1.getValue().getTarget(),
            equalTo( checkMethod.getParameterTypes()[argInfoEntry1.getKey().getTargetParamPosition()] ) );
      }

    } );
  }

  @Test
  public void test8() {
    Method checkMethod = getMethod( "addPartlyNativeWrapped" );
    Method queryMethod = getMethod( "subPartlyNativeWrapped" );

    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 2 ) );
    for ( MethodMatchingInfo info : matchingInfos ) {
      ModuleMatchingInfo returnTypeMatchingInfo = info.getReturnTypeMatchingInfo();
      assertThat( returnTypeMatchingInfo, notNullValue() );
      assertThat( returnTypeMatchingInfo.getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( returnTypeMatchingInfo.getTarget(), equalTo( queryMethod.getReturnType() ) );
      Map<ParamPosition, ModuleMatchingInfo> argumentTypeMatchingInfos = info.getArgumentTypeMatchingInfos();
      assertThat( argumentTypeMatchingInfos, notNullValue() );
      assertThat( argumentTypeMatchingInfos.size(), equalTo( 2 ) );

      Iterator<Entry<ParamPosition, ModuleMatchingInfo>> iterator = info.getArgumentTypeMatchingInfos().entrySet()
          .iterator();
      while ( iterator.hasNext() ) {
        Entry<ParamPosition, ModuleMatchingInfo> argInfoEntry = iterator.next();
        assertThat( argInfoEntry.getValue().getSource(),
            equalTo( queryMethod.getParameterTypes()[argInfoEntry.getKey().getSourceParamPosition()] ) );
        assertThat( argInfoEntry.getValue().getTarget(),
            equalTo( checkMethod.getParameterTypes()[argInfoEntry.getKey().getTargetParamPosition()] ) );
      }
    }
  }

  @Test
  public void test9() {
    Method checkMethod = getMethod( "addPartlyWrapped" );
    Method queryMethod = getMethod( "subPartlyWrapped" );

    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 2 ) );
    for ( MethodMatchingInfo info : matchingInfos ) {
      ModuleMatchingInfo returnTypeMatchingInfo = info.getReturnTypeMatchingInfo();
      assertThat( returnTypeMatchingInfo, notNullValue() );
      assertThat( returnTypeMatchingInfo.getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( returnTypeMatchingInfo.getTarget(), equalTo( queryMethod.getReturnType() ) );
      Map<ParamPosition, ModuleMatchingInfo> argumentTypeMatchingInfos = info.getArgumentTypeMatchingInfos();
      assertThat( argumentTypeMatchingInfos, notNullValue() );
      assertThat( argumentTypeMatchingInfos.size(), equalTo( 2 ) );

      Iterator<Entry<ParamPosition, ModuleMatchingInfo>> iterator = info.getArgumentTypeMatchingInfos().entrySet()
          .iterator();
      while ( iterator.hasNext() ) {
        Entry<ParamPosition, ModuleMatchingInfo> argInfoEntry = iterator.next();
        assertThat( argInfoEntry.getValue().getSource(),
            equalTo( queryMethod.getParameterTypes()[argInfoEntry.getKey().getSourceParamPosition()] ) );
        assertThat( argInfoEntry.getValue().getTarget(),
            equalTo( checkMethod.getParameterTypes()[argInfoEntry.getKey().getTargetParamPosition()] ) );
      }
    }
  }

  @Test
  public void test10() {
    Method checkMethod = getMethod( "addSpec" );
    Method queryMethod = getMethod( "addGen" );

    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 2 ) );
    for ( MethodMatchingInfo info : matchingInfos ) {
      ModuleMatchingInfo returnTypeMatchingInfo = info.getReturnTypeMatchingInfo();
      assertThat( returnTypeMatchingInfo, notNullValue() );
      assertThat( returnTypeMatchingInfo.getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( returnTypeMatchingInfo.getTarget(), equalTo( queryMethod.getReturnType() ) );
      Map<ParamPosition, ModuleMatchingInfo> argumentTypeMatchingInfos = info.getArgumentTypeMatchingInfos();
      assertThat( argumentTypeMatchingInfos, notNullValue() );
      assertThat( argumentTypeMatchingInfos.size(), equalTo( 2 ) );

      Iterator<Entry<ParamPosition, ModuleMatchingInfo>> iterator = info.getArgumentTypeMatchingInfos().entrySet()
          .iterator();
      while ( iterator.hasNext() ) {
        Entry<ParamPosition, ModuleMatchingInfo> argInfoEntry = iterator.next();
        assertThat( argInfoEntry.getValue().getSource(),
            equalTo( queryMethod.getParameterTypes()[argInfoEntry.getKey().getSourceParamPosition()] ) );
        assertThat( argInfoEntry.getValue().getTarget(),
            equalTo( checkMethod.getParameterTypes()[argInfoEntry.getKey().getTargetParamPosition()] ) );
      }
    }
  }

  @Test
  public void test11() {
    Method queryMethod = getMethod( "addSpec" );
    Method checkMethod = getMethod( "addSpecReturnSpec" );
    Set<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );

  }

}
