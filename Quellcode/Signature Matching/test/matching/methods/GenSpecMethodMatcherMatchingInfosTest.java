package matching.methods;

import static matching.methods.testmethods.MethodPool.getMethod;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.modules.GenSpecTypeMatcher;
import matching.modules.ModuleMatchingInfo;

public class GenSpecMethodMatcherMatchingInfosTest {

  MethodMatcher matcher = new CommonMethodMatcher( () -> new GenSpecTypeMatcher() );

  @Test
  public void test1() {
    Method checkMethod = getMethod( "getTrue" );
    Method queryMethod = getMethod( "getTrue" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod,
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
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
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
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test
  public void test4() {
    Method checkMethod = getMethod( "getOneNativeWrapped" );
    Method queryMethod = getMethod( "getOne" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test
  public void test5() {
    Method checkMethod = getMethod( "setBool" );
    Method queryMethod = getMethod( "setBoolNativeWrapped" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test
  public void test6() {
    Method checkMethod = getMethod( "addOne" );
    Method queryMethod = getMethod( "subOne" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    matchingInfos.forEach( info -> {
      assertThat( info.getReturnTypeMatchingInfo(), notNullValue() );
      assertThat( info.getReturnTypeMatchingInfo().getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( info.getReturnTypeMatchingInfo().getTarget(), equalTo( queryMethod.getReturnType() ) );
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
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
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
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test
  public void test9() {
    Method checkMethod = getMethod( "addPartlyWrapped" );
    Method queryMethod = getMethod( "subPartlyWrapped" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test
  public void test10() {
    Method checkMethod = getMethod( "addSpec" );
    Method queryMethod = getMethod( "addGen" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size() > 0, equalTo( true ) );
    for ( MethodMatchingInfo info : matchingInfos ) {
      ModuleMatchingInfo returnTypeMatchingInfo = info.getReturnTypeMatchingInfo();
      assertThat( returnTypeMatchingInfo, notNullValue() );
      assertThat( returnTypeMatchingInfo.getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( returnTypeMatchingInfo.getTarget(), equalTo( queryMethod.getReturnType() ) );
      Map<ParamPosition, ModuleMatchingInfo> argumentTypeMatchingInfos = info.getArgumentTypeMatchingInfos();
      assertThat( argumentTypeMatchingInfos, notNullValue() );
      assertThat( argumentTypeMatchingInfos.size(), equalTo( 2 ) );

      // Parameter prüfen
      Iterator<Entry<ParamPosition, ModuleMatchingInfo>> iterator = argumentTypeMatchingInfos.entrySet().iterator();
      Entry<ParamPosition, ModuleMatchingInfo> argInfoEntry1 = iterator.next();
      assertThat( argInfoEntry1.getValue().getSource(),
          equalTo( queryMethod.getParameterTypes()[argInfoEntry1.getKey().getSourceParamPosition()] ) );
      assertThat( argInfoEntry1.getValue().getTarget(),
          equalTo( checkMethod.getParameterTypes()[argInfoEntry1.getKey().getTargetParamPosition()] ) );

      Entry<ParamPosition, ModuleMatchingInfo> argInfoEntry2 = iterator.next();
      assertThat( argInfoEntry2.getValue().getSource(),
          equalTo( queryMethod.getParameterTypes()[argInfoEntry2.getKey().getSourceParamPosition()] ) );
      assertThat( argInfoEntry2.getValue().getTarget(),
          equalTo( checkMethod.getParameterTypes()[argInfoEntry2.getKey().getTargetParamPosition()] ) );

      assertThat( argInfoEntry1.getValue().getMethodMatchingInfos().isEmpty()
          ^ argInfoEntry2.getValue().getMethodMatchingInfos().isEmpty(), equalTo( true ) );
    }
  }

  @Test
  public void test11() {
    Method checkMethod = getMethod( "setBoolNativeWrapped" );
    Method queryMethod = getMethod( "setObject" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size() > 0, equalTo( true ) );
    for ( MethodMatchingInfo info : matchingInfos ) {
      assertThat( info.getReturnTypeMatchingInfo(), notNullValue() );
      assertThat( info.getReturnTypeMatchingInfo().getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( info.getReturnTypeMatchingInfo().getTarget(), equalTo( queryMethod.getReturnType() ) );
      assertThat( info.getArgumentTypeMatchingInfos(), notNullValue() );
      assertThat( info.getArgumentTypeMatchingInfos().size(), equalTo( 1 ) );

      Iterator<Entry<ParamPosition, ModuleMatchingInfo>> iterator = info.getArgumentTypeMatchingInfos().entrySet()
          .iterator();
      Entry<ParamPosition, ModuleMatchingInfo> argInfoEntry1 = iterator.next();
      assertThat( argInfoEntry1.getValue().getSource(),
          equalTo( queryMethod.getParameterTypes()[argInfoEntry1.getKey().getSourceParamPosition()] ) );
      assertThat( argInfoEntry1.getValue().getTarget(),
          equalTo( checkMethod.getParameterTypes()[argInfoEntry1.getKey().getTargetParamPosition()] ) );
    }
  }

  @Test
  public void test12() {
    Method queryMethod = getMethod( "addSpec" );
    Method checkMethod = getMethod( "addSpecReturnSpec" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size() > 0, equalTo( true ) );
    for ( MethodMatchingInfo info : matchingInfos ) {
      // Returntype prüfen
      ModuleMatchingInfo returnTypeMatchingInfo = info.getReturnTypeMatchingInfo();
      assertThat( returnTypeMatchingInfo, notNullValue() );
      assertThat( returnTypeMatchingInfo.getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( returnTypeMatchingInfo.getTarget(), equalTo( queryMethod.getReturnType() ) );
      Collection<MethodMatchingInfo> methodMatchingInfosOfReturnType = returnTypeMatchingInfo.getMethodMatchingInfos();
      assertThat( methodMatchingInfosOfReturnType.isEmpty(), equalTo( true ) );

      // Parameter prüfen
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

    }
  }

  @Test
  public void test13() {
    Method checkMethod = getMethod( "addSpec" );
    Method queryMethod = getMethod( "addSpecReturnSpec" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size() > 0, equalTo( true ) );
    for ( MethodMatchingInfo info : matchingInfos ) {
      // Returntype prüfen
      ModuleMatchingInfo returnTypeMatchingInfo = info.getReturnTypeMatchingInfo();
      assertThat( returnTypeMatchingInfo, notNullValue() );
      assertThat( returnTypeMatchingInfo.getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( returnTypeMatchingInfo.getTarget(), equalTo( queryMethod.getReturnType() ) );
      Collection<MethodMatchingInfo> methodMatchingInfosOfReturnType = returnTypeMatchingInfo.getMethodMatchingInfos();
      assertThat( methodMatchingInfosOfReturnType.isEmpty(), equalTo( false ) );

      // Parameter prüfen
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

    }
  }

}
