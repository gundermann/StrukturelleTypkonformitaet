package de.fernuni.hagen.ma.gundermann.matcherexamples.wrapped;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubWrapper;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SuperClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SuperWrapper;
import glue.ProxyFactory;
import glue.SigMaGlueException;
import matching.MatcherCombiner;
import matching.methods.MethodMatchingInfo;
import matching.modules.ExactTypeMatcher;
import matching.modules.GenSpecTypeMatcher;
import matching.modules.ModuleMatchingInfo;
import matching.modules.WrappedTypeMatcher;

/**
 * Matcher Test fuer die Konvertierung fuer uebereinstimmende Typen in der Form:<br>
 * <b>checkType &equiv;<sub>Exact,Gen,Spec</sub> queryType#attr</b>
 */
public class WrappedTypeMatcher_Wrapped_ConversionTest {

  private WrappedTypeMatcher matcher = new WrappedTypeMatcher(
      MatcherCombiner.combine( new ExactTypeMatcher(), new GenSpecTypeMatcher() ) );

  /**
   * {@link SuperClass} &equiv;<sub>Exact</sub> {@link SuperWrapper}
   */
  @Test
  public void convertSuperWrapper2SuperClass() {
    SuperWrapper offeredComponent = new SuperWrapper( "A" );
    Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos(
        SuperClass.class, SuperWrapper.class );
    // Der WrappedTypeMatcher erzeugt nur eine ModuleMatchingInfo (kein rekursives Matching)
    ModuleMatchingInfo moduleMatchingInfo = matchingInfos.iterator().next();

    // Das Attribut an welches der Aufruf delegiert wird, wird vom TypeMatcher ermittelt und im ProxyFactoryCreator
    // innerhalb der ModuleMatchingInfo hinterlegt.
    ProxyFactory<SuperClass> proxyFactory = moduleMatchingInfo.getConverterCreator()
        .createProxyFactory( SuperClass.class );
    Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

    SuperClass proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

    assertTrue( proxy.getString().equals( "A" ) );
  }

  /**
   * {@link SubClass} &equiv;<sub>Spec</sub> {@link SuperWrapper} <br>
   * Downcast mit positivem Aufrug
   */
  @Test
  public void convertSuperWrapper2SubClass_positiveCall() {
    SuperWrapper offeredComponent = new SuperWrapper( "A" );
    Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos(
        SubClass.class, SuperWrapper.class );
    // Der WrappedTypeMatcher erzeugt nur eine ModuleMatchingInfo (kein rekursives Matching)
    ModuleMatchingInfo moduleMatchingInfo = matchingInfos.iterator().next();

    // Das Attribut an welches der Aufruf delegiert wird, wird vom TypeMatcher ermittelt und im ProxyFactoryCreator
    // innerhalb der ModuleMatchingInfo hinterlegt.
    ProxyFactory<SubClass> proxyFactory = moduleMatchingInfo.getConverterCreator()
        .createProxyFactory( SubClass.class );
    Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

    SubClass proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

    assertTrue( proxy.getString().equals( "A" ) );
  }

  /**
   * {@link SubClass} &equiv;<sub>Spec</sub> {@link SuperWrapper} <br>
   * Downcast mit negativem Aufrug
   */
  @Test( expected = SigMaGlueException.class )
  public void convertSuperWrapper2SubClass_negativeCall() {
    SuperWrapper offeredComponent = new SuperWrapper( "A" );
    Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos(
        SubClass.class, SuperWrapper.class );
    // Der WrappedTypeMatcher erzeugt nur eine ModuleMatchingInfo (kein rekursives Matching)
    ModuleMatchingInfo moduleMatchingInfo = matchingInfos.iterator().next();

    // Das Attribut an welches der Aufruf delegiert wird, wird vom TypeMatcher ermittelt und im ProxyFactoryCreator
    // innerhalb der ModuleMatchingInfo hinterlegt.
    ProxyFactory<SubClass> proxyFactory = moduleMatchingInfo.getConverterCreator()
        .createProxyFactory( SubClass.class );
    Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

    SubClass proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );
    proxy.getStringWithoutPrefix();
  }

  /**
   * {@link SuperClass} &equiv;<sub>Gen</sub> {@link SubWrapper} <br>
   * Upcast
   */
  @Test
  public void convertSubWrapper2SuperClass() {
    SubWrapper offeredComponent = new SubWrapper( "A" );
    Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos(
        SuperClass.class, SubWrapper.class );
    // Der WrappedTypeMatcher erzeugt nur eine ModuleMatchingInfo (kein rekursives Matching)
    ModuleMatchingInfo moduleMatchingInfo = matchingInfos.iterator().next();

    // Das Attribut an welches der Aufruf delegiert wird, wird vom TypeMatcher ermittelt und im ProxyFactoryCreator
    // innerhalb der ModuleMatchingInfo hinterlegt.
    ProxyFactory<SuperClass> proxyFactory = moduleMatchingInfo.getConverterCreator()
        .createProxyFactory( SuperClass.class );
    Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

    SuperClass proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

    // Die Delegation erfolgt an das Objekt der Klasse SubClass
    assertTrue( proxy.getString().equals( "SubA" ) );
  }

}
