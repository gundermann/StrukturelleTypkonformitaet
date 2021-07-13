package matching.methods;

import static matching.methods.testmethods.MethodPool.getMethod;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.signaturematching.SingleMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo.ParamPosition;
import matching.types.ExactTypeMatcher;
import matching.types.TypeMatchingInfo;

public class ExactMethodMatcherMatchingInfosTest {

  MethodMatcher matcher = new CommonMethodMatcher( () -> new ExactTypeMatcher() );

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

      Iterator<Entry<ParamPosition, SingleMatchingInfo>> iterator = info.getArgumentTypeMatchingInfos().entrySet()
          .iterator();
      while ( iterator.hasNext() ) {
        Entry<ParamPosition, SingleMatchingInfo> argInfoEntry1 = iterator.next();
        assertThat( argInfoEntry1.getValue().getSource(),
            equalTo( queryMethod.getParameterTypes()[argInfoEntry1.getKey().getSourceParamPosition()] ) );
        assertThat( argInfoEntry1.getValue().getTarget(),
            equalTo( checkMethod.getParameterTypes()[argInfoEntry1.getKey().getTargetParamPosition()] ) );
      }
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
      assertThat( info.getReturnTypeMatchingInfo(), notNullValue() );
      assertThat( info.getReturnTypeMatchingInfo().getSource(), equalTo( checkMethod.getReturnType() ) );
      assertThat( info.getArgumentTypeMatchingInfos(), notNullValue() );
      assertThat( info.getArgumentTypeMatchingInfos().size(), equalTo( 2 ) );

      Iterator<Entry<ParamPosition, SingleMatchingInfo>> iterator = info.getArgumentTypeMatchingInfos().entrySet()
          .iterator();
      while ( iterator.hasNext() ) {
        Entry<ParamPosition, SingleMatchingInfo> argInfoEntry1 = iterator.next();
        assertThat( argInfoEntry1.getValue().getSource(),
            equalTo( queryMethod.getParameterTypes()[argInfoEntry1.getKey().getSourceParamPosition()] ) );
        assertThat( argInfoEntry1.getValue().getTarget(),
            equalTo( checkMethod.getParameterTypes()[argInfoEntry1.getKey().getTargetParamPosition()] ) );
      }

    } );
  }

  @Test
  public void test8() {
    Method sourceMethod = getMethod( "addPartlyNativeWrapped" );
    Method targetMethod = getMethod( "subPartlyNativeWrapped" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test
  public void test9() {
    Method sourceMethod = getMethod( "addPartlyWrapped" );
    Method targetMethod = getMethod( "subPartlyWrapped" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test
  public void test10() {
    Method sourceMethod = getMethod( "addSpec" );
    Method targetMethod = getMethod( "addGen" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test( )
  public void test11() {
    Method sourceMethod = getMethod( "addSpec" );
    Method targetMethod = getMethod( "getTrue" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

}
