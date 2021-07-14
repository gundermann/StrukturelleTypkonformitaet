package matching.methods;

import static matching.methods.testmethods.MethodPool.getMethod;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.signaturematching.SingleMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods.CommonMethodMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods.MethodMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.ExactTypeMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.TypeMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.WrappedTypeMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;

public class WrappedTypeMethodMatcherMatchingInfosTest {
  MethodMatcher matcher;

  @Before
  public void setup() {
    matcher = new CommonMethodMatcher( () -> new WrappedTypeMatcher( () -> new ExactTypeMatcher() ) );
  }

  @Test
  public void test1() {
    Method checkMethod = getMethod( "getTrue" );
    Method queryMethod = getMethod( "getTrue" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test
  public void test2() {
    Method checkMethod = getMethod( "getTrue" );
    Method queryMethod = getMethod( "getFalse" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
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
    Method checkMethod = getMethod( "getOneNativeWrapped" );
    Method queryMethod = getMethod( "getOne" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    for ( MethodMatchingInfo mmi : matchingInfos ) {
      assertThat( mmi.getSource(), equalTo( queryMethod ) );
      assertThat( mmi.getTarget(), equalTo( checkMethod ) );
      SingleMatchingInfo rtMatchingInfo = mmi.getReturnTypeMatchingInfo();
      assertThat( rtMatchingInfo, notNullValue() );
      assertThat( rtMatchingInfo.getSource(), notNullValue() );
      assertThat( rtMatchingInfo.getSource(), equalTo( Integer.class ) );

      assertThat( rtMatchingInfo.getTarget(), notNullValue() );
      assertThat( rtMatchingInfo.getTarget(), equalTo( int.class ) );
      assertThat( rtMatchingInfo.getConverterCreator(), notNullValue() );
      Collection<MethodMatchingInfo> rtMethodMI = rtMatchingInfo.getMethodMatchingInfos().values();
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
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    for ( MethodMatchingInfo mmi : matchingInfos ) {
      assertThat( mmi.getSource(), equalTo( queryMethod ) );
      assertThat( mmi.getTarget(), equalTo( checkMethod ) );
      SingleMatchingInfo rtMatchingInfo = mmi.getReturnTypeMatchingInfo();
      assertThat( rtMatchingInfo, notNullValue() );
      assertThat( rtMatchingInfo.getTarget(), notNullValue() );
      assertThat( rtMatchingInfo.getTarget(), equalTo( Integer.class ) );
      assertThat( rtMatchingInfo.getConverterCreator(), notNullValue() );
      assertThat( rtMatchingInfo.getSource(), notNullValue() );
      assertThat( rtMatchingInfo.getSource(), equalTo( int.class ) );

      Collection<MethodMatchingInfo> rtMethodMI = rtMatchingInfo.getMethodMatchingInfos().values();
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
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test
  public void test7() {
    Method checkMethod = getMethod( "add" );
    Method queryMethod = getMethod( "sub" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }

  @Test
  public void test8() {
    Method checkMethod = getMethod( "addPartlyNativeWrapped" );
    Method queryMethod = getMethod( "subPartlyNativeWrapped" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
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
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( checkMethod, queryMethod );
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

  @Test
  public void test11() {
    Method sourceMethod = getMethod( "setBool" );
    Method targetMethod = getMethod( "setObject" );
    Collection<MethodMatchingInfo> matchingInfos = matcher.calculateMatchingInfos( sourceMethod, targetMethod );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 0 ) );
  }
}
