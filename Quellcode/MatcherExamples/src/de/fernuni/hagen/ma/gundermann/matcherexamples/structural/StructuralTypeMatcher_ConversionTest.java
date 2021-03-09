package de.fernuni.hagen.ma.gundermann.matcherexamples.structural;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubReturnSubParamClass1;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubReturnSubParamClass2;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubReturnSuperParamClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubReturnSuperWrapperParamClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubWrapper;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubWrapperReturnSubParamClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SuperClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SuperReturnSubParamClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SuperWrapper;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SuperWrapperReturnSubParamClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SuperWrapperReturnSubWrapperParamClass;
import glue.ProxyFactory;
import matching.MatcherCombiner;
import matching.methods.MethodMatchingInfo;
import matching.modules.ExactTypeMatcher;
import matching.modules.GenSpecTypeMatcher;
import matching.modules.ModuleMatchingInfo;
import matching.modules.StructuralTypeMatcher;
import matching.modules.WrappedTypeMatcher;

/**
 * Matcher Test fuer die Ubereinstimmung der Form:<br>
 * <b>checkType &equiv;<sub>Struct</sub> queryType</b>
 */
public class StructuralTypeMatcher_ConversionTest {

  // Hier muss ein Matcher angegeben werden, die fuer den rekursiven Aufruf bei der Pruefung auf Uebereinstimmung
  // verwendet werden soll.
  // Derzeit wird nur ein rekursiver Aufruf vorgenommen.
  // Der uebergebene Matcher kombiniert folgende Matcher:
  // 1. Einem ExactTypeMatcher
  // 2. Einem GenSpecTypeMatcher
  // 3. Einen WrappedTypeMatcher, der wiederum eine Kombination aus dem ExactTypeMatcher mit dem GenSpecTypeMatcher
  // verwendet
  private StructuralTypeMatcher matcher = new StructuralTypeMatcher(
      MatcherCombiner.combine( new ExactTypeMatcher(), new GenSpecTypeMatcher(),
          new WrappedTypeMatcher( MatcherCombiner.combine( new ExactTypeMatcher(), new GenSpecTypeMatcher() ) ) ) );

  /**
   * &forall; ( {@link SubReturnSubParamClass1}.m1( m1Param ) : m1Return ) : &exist; (
   * {@link SubReturnSubParamClass2}.m2( m2Param ) : m2Return) : m1Param &equiv;<sub>Exact</sub> m2Param &and; m1Return
   * &equiv;<sub>Exact</sub> m2Return
   */
  @Test
  public void convert_exactReturn_exactParam() {
    SubReturnSubParamClass2 offeredComponent = new SubReturnSubParamClass2();
    Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos( SubReturnSubParamClass1.class,
        SubReturnSubParamClass2.class );
    // Hier wird ein Matching zwischen den einzelnen Methoden hergestellt. Da es zu einer erwarteten Methode mehrere
    // Matchings geben kann, zur Laufzeit aber nur eine der matchenden Methoden in Frage kommt, wird für jede
    // möglichen Kombinationen eine ModuleMatchingInfo generiert.
    for ( ModuleMatchingInfo moduleMatchingInfo : matchingInfos ) {
      ProxyFactory<SubReturnSubParamClass1> proxyFactory = moduleMatchingInfo.getConverterCreator()
          .createProxyFactory( SubReturnSubParamClass1.class );
      Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

      SubReturnSubParamClass1 proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

      SubClass param1 = new SubClass( "A" );
      SubClass param2 = new SubClass( "B" );
      assertTrue( proxy.addHello( param1 ).getString().equals( "SubSubAhello" ) );
      assertTrue( proxy.addHello( param1 ).getStringWithoutPrefix().equals( "SubAhello" ) );
      assertTrue( proxy.add( param1, param2 ).getString().equals( "SubSubASubB" ) );
      assertTrue( proxy.add( param1, param2 ).getStringWithoutPrefix().equals( "SubASubB" ) );
    }
  }

  /**
   * &forall; ( {@link SubReturnSubParamClass1}.m1( m1Param ) : m1Return ) : &exist; (
   * {@link SubReturnSubParamClass2}.m2( m2Param ) : m2Return) : m1Param &equiv;<sub>Gen</sub> m2Param &and; m1Return
   * &equiv;<sub>Exact</sub> m2Return
   */
  @Test
  public void convert_exactReturn_genParam() {
    SubReturnSubParamClass1 offeredComponent = new SubReturnSubParamClass1();
    Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos( SubReturnSuperParamClass.class,
        SubReturnSubParamClass1.class );
    for ( ModuleMatchingInfo moduleMatchingInfo : matchingInfos ) {
      ProxyFactory<SubReturnSuperParamClass> proxyFactory = moduleMatchingInfo.getConverterCreator()
          .createProxyFactory( SubReturnSuperParamClass.class );
      Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

      SubReturnSuperParamClass proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

      SuperClass param1 = new SuperClass( "A" );
      SuperClass param2 = new SuperClass( "B" );
      assertTrue( proxy.addHello( param1 ).getString().equals( "SubAhello" ) );
      assertTrue( proxy.addHello( param1 ).getStringWithoutPrefix().equals( "Ahello" ) );
      assertTrue( proxy.add( param1, param2 ).getString().equals( "SubAB" ) );
      assertTrue( proxy.add( param1, param2 ).getStringWithoutPrefix().equals( "AB" ) );
    }
  }

  /**
   * &forall; ( {@link SubReturnSubParamClass1}.m1( m1Param ) : m1Return ) : &exist; (
   * {@link SubReturnSubParamClass2}.m2( m2Param ) : m2Return) : m1Param &equiv;<sub>Spec</sub> m2Param &and; m1Return
   * &equiv;<sub>Exact</sub> m2Return
   */
  @Test
  public void convert_exactReturn_specParam() {
    SubReturnSuperParamClass offeredComponent = new SubReturnSuperParamClass();
    Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos( SubReturnSubParamClass1.class,
        SubReturnSuperParamClass.class );
    for ( ModuleMatchingInfo moduleMatchingInfo : matchingInfos ) {
      ProxyFactory<SubReturnSubParamClass1> proxyFactory = moduleMatchingInfo.getConverterCreator()
          .createProxyFactory( SubReturnSubParamClass1.class );
      Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

      SubReturnSubParamClass1 proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

      SubClass param1 = new SubClass( "A" );
      SubClass param2 = new SubClass( "B" );
      assertTrue( proxy.addHello( param1 ).getString().equals( "SubSubAhello" ) );
      assertTrue( proxy.addHello( param1 ).getStringWithoutPrefix().equals( "SubAhello" ) );
      assertTrue( proxy.add( param1, param2 ).getString().equals( "SubSubASubB" ) );
      assertTrue( proxy.add( param1, param2 ).getStringWithoutPrefix().equals( "SubASubB" ) );
    }
  }

  /**
   * &forall; ( {@link SubReturnSubParamClass1}.m1( m1Param ) : m1Return ) : &exist; (
   * {@link SubReturnSubParamClass2}.m2( m2Param ) : m2Return) : m1Param &equiv;<sub>Spec</sub> m2Param &and; m1Return
   * &equiv;<sub>Gen</sub> m2Return
   */
  @Test
  public void match_genReturn_specParam() {
    SuperReturnSubParamClass offeredComponent = new SuperReturnSubParamClass();
    Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos( SubReturnSuperParamClass.class,
        SuperReturnSubParamClass.class );
    for ( ModuleMatchingInfo moduleMatchingInfo : matchingInfos ) {
      ProxyFactory<SubReturnSuperParamClass> proxyFactory = moduleMatchingInfo.getConverterCreator()
          .createProxyFactory( SubReturnSuperParamClass.class );
      Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

      SubReturnSuperParamClass proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

      SubClass param1 = new SubClass( "A" );
      SubClass param2 = new SubClass( "B" );
      assertTrue( proxy.addHello( param1 ).getString().equals( "SubSubAhello" ) );
      assertTrue( proxy.addHello( param1 ).getStringWithoutPrefix().equals( "SubAhello" ) );
      assertTrue( proxy.add( param1, param2 ).getString().equals( "SubSubASubB" ) );
      assertTrue( proxy.add( param1, param2 ).getStringWithoutPrefix().equals( "SubASubB" ) );
    }
  }

  /**
   * &forall; ( {@link SubReturnSubParamClass1}.m1( m1Param ) : m1Return ) : &exist; (
   * {@link SubReturnSubParamClass2}.m2( m2Param ) : m2Return) : m1Param &equiv;<sub>Gen</sub> m2Param &and; m1Return
   * &equiv;<sub>Spec</sub> m2Return
   */
  @Test
  public void match_specReturn_genParam() {
    SuperReturnSubParamClass offeredComponent = new SuperReturnSubParamClass();
    Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos( SubReturnSuperParamClass.class,
        SuperReturnSubParamClass.class );
    for ( ModuleMatchingInfo moduleMatchingInfo : matchingInfos ) {
      ProxyFactory<SubReturnSuperParamClass> proxyFactory = moduleMatchingInfo.getConverterCreator()
          .createProxyFactory( SubReturnSuperParamClass.class );
      Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

      SubReturnSuperParamClass proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

      SuperClass param1 = new SuperClass( "A" );
      SuperClass param2 = new SuperClass( "B" );
      assertTrue( proxy.addHello( param1 ).getString().equals( "SubAhello" ) );
      assertTrue( proxy.addHello( param1 ).getStringWithoutPrefix().equals( "Ahello" ) );
      assertTrue( proxy.add( param1, param2 ).getString().equals( "SubAB" ) );
      assertTrue( proxy.add( param1, param2 ).getStringWithoutPrefix().equals( "AB" ) );
    }

  }

  /**
   * &forall; ( {@link SubReturnSubParamClass1}.m1( m1Param ) : m1Return ) : &exist; (
   * {@link SubReturnSubParamClass2}.m2( m2Param ) : m2Return) : m1Param &equiv;<sub>Wrapper<sub>Gen</sub></sub> m2Param
   * &and; m1Return &equiv;<sub>Spec</sub> m2Return
   */
  @Test
  public void match_specReturn_wrapperGenParam() {
    SuperReturnSubParamClass offeredComponent = new SuperReturnSubParamClass();
    Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos(
        SubReturnSuperWrapperParamClass.class,
        SuperReturnSubParamClass.class );
    for ( ModuleMatchingInfo moduleMatchingInfo : matchingInfos ) {
      ProxyFactory<SubReturnSuperWrapperParamClass> proxyFactory = moduleMatchingInfo.getConverterCreator()
          .createProxyFactory( SubReturnSuperWrapperParamClass.class );
      Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

      SubReturnSuperWrapperParamClass proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

      SuperWrapper param1 = new SuperWrapper( "A" );
      SuperWrapper param2 = new SuperWrapper( "B" );
      assertTrue( proxy.addHello( param1 ).getString().equals( "SubWRAPPED_Ahello" ) );
      assertTrue( proxy.addHello( param1 ).getStringWithoutPrefix().equals( "WRAPPED_Ahello" ) );
      assertTrue( proxy.add( param1, param2 ).getString().equals( "SubWRAPPED_AWRAPPED_B" ) );
      assertTrue( proxy.add( param1, param2 ).getStringWithoutPrefix().equals( "WRAPPED_AWRAPPED_B" ) );
    }
  }

  /**
   * &forall; ( {@link SubReturnSubParamClass1}.m1( m1Param ) : m1Return ) : &exist; (
   * {@link SubReturnSubParamClass2}.m2( m2Param ) : m2Return) : m1Param &equiv;<sub>Spec</sub> m2Param &and; m1Return
   * &equiv;<sub>Wrapper<sub>Gen</sub></sub> m2Return
   */
  @Test
  public void match_wrapperGenReturn_specParam() {
    SubReturnSuperParamClass offeredComponent = new SubReturnSuperParamClass();
    Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos(
        SuperWrapperReturnSubParamClass.class,
        SubReturnSuperParamClass.class );
    for ( ModuleMatchingInfo moduleMatchingInfo : matchingInfos ) {
      ProxyFactory<SuperWrapperReturnSubParamClass> proxyFactory = moduleMatchingInfo.getConverterCreator()
          .createProxyFactory( SuperWrapperReturnSubParamClass.class );
      Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

      SuperWrapperReturnSubParamClass proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

      SubClass param1 = new SubClass( "A" );
      SubClass param2 = new SubClass( "B" );
      assertTrue( proxy.addHello( param1 ).toString().equals( "WRAPPED_SubAhello" ) );
      assertTrue( proxy.add( param1, param2 ).toString().equals( "WRAPPED_SubASubB" ) );
    }

  }

  /**
   * &forall; ( {@link SubReturnSubParamClass1}.m1( m1Param ) : m1Return ) : &exist; (
   * {@link SubReturnSubParamClass2}.m2( m2Param ) : m2Return) : m1Param &equiv;<sub>Wrapper<sub>Spec</sub></sub>
   * m2Param &and; m1Return &equiv;<sub>Wrapper<sub>Gen</sub></sub> m2Return
   */
  @Test
  public void match_wrapperGenReturn_wrapperSpecParam() {
    SubReturnSuperParamClass offeredComponent = new SubReturnSuperParamClass();
    Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos(
        SuperWrapperReturnSubWrapperParamClass.class,
        SubReturnSuperParamClass.class );
    for ( ModuleMatchingInfo moduleMatchingInfo : matchingInfos ) {
      ProxyFactory<SuperWrapperReturnSubWrapperParamClass> proxyFactory = moduleMatchingInfo.getConverterCreator()
          .createProxyFactory( SuperWrapperReturnSubWrapperParamClass.class );
      Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

      SuperWrapperReturnSubWrapperParamClass proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

      SubWrapper param1 = new SubWrapper( "A" );
      SubWrapper param2 = new SubWrapper( "B" );
      assertTrue( proxy.addHello( param1 ).toString().equals( "WRAPPED_WRAPPED_Ahello" ) );
      assertTrue( proxy.add( param1, param2 ).toString().equals( "WRAPPED_WRAPPED_AWRAPPED_B" ) );
    }

  }

  /**
   * &forall; ( {@link SubReturnSubParamClass1}.m1( m1Param ) : m1Return ) : &exist; (
   * {@link SubReturnSubParamClass2}.m2( m2Param ) : m2Return) : m1Param &equiv;<sub>Wrapper<sub>Exact</sub></sub>
   * m2Param &and; m1Return &equiv;<sub>Wrapper<sub>Spec</sub></sub> m2Return
   */
  @Test
  public void match_wrapperSpecReturn_wrapperExactParam() {

    SuperReturnSubParamClass offeredComponent = new SuperReturnSubParamClass();
    Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos(
        SubWrapperReturnSubParamClass.class,
        SuperReturnSubParamClass.class );
    for ( ModuleMatchingInfo moduleMatchingInfo : matchingInfos ) {
      ProxyFactory<SubWrapperReturnSubParamClass> proxyFactory = moduleMatchingInfo.getConverterCreator()
          .createProxyFactory( SubWrapperReturnSubParamClass.class );
      Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

      SubWrapperReturnSubParamClass proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

      SubClass param1 = new SubClass( "A" );
      SubClass param2 = new SubClass( "B" );
      assertTrue( proxy.addHello( param1 ).toString().equals( "WRAPPED_SubAhello" ) );
      assertTrue( proxy.addHello( param1 ).toStringWithPrefix().equals( "WRAPPED_SubSubAhello" ) );
      assertTrue( proxy.add( param1, param2 ).toString().equals( "WRAPPED_SubASubB" ) );
      assertTrue( proxy.add( param1, param2 ).toStringWithPrefix().equals( "WRAPPED_SubSubASubB" ) );
    }

  }

}
