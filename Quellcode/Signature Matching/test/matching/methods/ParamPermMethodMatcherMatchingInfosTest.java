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
import matching.types.ExactTypeMatcher;
import matching.types.TypeMatchingInfo;

public class ParamPermMethodMatcherMatchingInfosTest {

  MethodMatcher matcher = new ParamPermMethodMatcher( () -> new ExactTypeMatcher() );

  @Test
  public void test1() {
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( getMethod( "getTrue" ),
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
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
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
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test
  public void test4() {
    Method sourceMethod = getMethod( "getOneNativeWrapped" );
    Method targetMethod = getMethod( "getOne" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test
  public void test5() {
    Method sourceMethod = getMethod( "setBool" );
    Method targetMethod = getMethod( "setBoolNativeWrapped" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
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
      assertThat( info.getArgumentTypeMatchingInfos(), notNullValue() );
      assertThat( info.getArgumentTypeMatchingInfos().size(), equalTo( 1 ) );

      Iterator<Entry<ParamPosition, TypeMatchingInfo>> iterator = info.getArgumentTypeMatchingInfos().entrySet()
          .iterator();
      Entry<ParamPosition, TypeMatchingInfo> argInfoEntry1 = iterator.next();
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
    assertThat( matchingInfos.size(), equalTo( 2 ) );
    matchingInfos.forEach( info -> {
      TypeMatchingInfo returnTypeMatchingInfo = info.getReturnTypeMatchingInfo();
      assertThat( returnTypeMatchingInfo, notNullValue() );
      assertThat( returnTypeMatchingInfo.getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( returnTypeMatchingInfo.getTarget(), equalTo( queryMethod.getReturnType() ) );
      Map<ParamPosition, TypeMatchingInfo> argumentTypeMatchingInfos = info.getArgumentTypeMatchingInfos();
      assertThat( argumentTypeMatchingInfos, notNullValue() );
      assertThat( argumentTypeMatchingInfos.size(), equalTo( 2 ) );

      Iterator<Entry<ParamPosition, TypeMatchingInfo>> iterator = info.getArgumentTypeMatchingInfos().entrySet()
          .iterator();
      while ( iterator.hasNext() ) {
        Entry<ParamPosition, TypeMatchingInfo> argInfoEntry1 = iterator.next();
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
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    for ( MethodMatchingInfo info : matchingInfos ) {
      TypeMatchingInfo returnTypeMatchingInfo = info.getReturnTypeMatchingInfo();
      assertThat( returnTypeMatchingInfo, notNullValue() );
      assertThat( returnTypeMatchingInfo.getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( returnTypeMatchingInfo.getTarget(), equalTo( queryMethod.getReturnType() ) );
      Map<ParamPosition, TypeMatchingInfo> argumentTypeMatchingInfos = info.getArgumentTypeMatchingInfos();
      assertThat( argumentTypeMatchingInfos, notNullValue() );
      assertThat( argumentTypeMatchingInfos.size(), equalTo( 2 ) );

      Iterator<Entry<ParamPosition, TypeMatchingInfo>> iterator = info.getArgumentTypeMatchingInfos().entrySet()
          .iterator();
      while ( iterator.hasNext() ) {
        Entry<ParamPosition, TypeMatchingInfo> argInfoEntry = iterator.next();
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

    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    for ( MethodMatchingInfo info : matchingInfos ) {
      TypeMatchingInfo returnTypeMatchingInfo = info.getReturnTypeMatchingInfo();
      assertThat( returnTypeMatchingInfo, notNullValue() );
      assertThat( returnTypeMatchingInfo.getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( returnTypeMatchingInfo.getTarget(), equalTo( queryMethod.getReturnType() ) );
      Map<ParamPosition, TypeMatchingInfo> argumentTypeMatchingInfos = info.getArgumentTypeMatchingInfos();
      assertThat( argumentTypeMatchingInfos, notNullValue() );
      assertThat( argumentTypeMatchingInfos.size(), equalTo( 2 ) );

      Iterator<Entry<ParamPosition, TypeMatchingInfo>> iterator = info.getArgumentTypeMatchingInfos().entrySet()
          .iterator();
      while ( iterator.hasNext() ) {
        Entry<ParamPosition, TypeMatchingInfo> argInfoEntry = iterator.next();
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

    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    for ( MethodMatchingInfo info : matchingInfos ) {
      TypeMatchingInfo returnTypeMatchingInfo = info.getReturnTypeMatchingInfo();
      assertThat( returnTypeMatchingInfo, notNullValue() );
      assertThat( returnTypeMatchingInfo.getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( returnTypeMatchingInfo.getTarget(), equalTo( queryMethod.getReturnType() ) );
      Map<ParamPosition, TypeMatchingInfo> argumentTypeMatchingInfos = info.getArgumentTypeMatchingInfos();
      assertThat( argumentTypeMatchingInfos, notNullValue() );
      assertThat( argumentTypeMatchingInfos.size(), equalTo( 2 ) );

      Iterator<Entry<ParamPosition, TypeMatchingInfo>> iterator = info.getArgumentTypeMatchingInfos().entrySet()
          .iterator();
      while ( iterator.hasNext() ) {
        Entry<ParamPosition, TypeMatchingInfo> argInfoEntry = iterator.next();
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
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );

  }

}
