package de.fernuni.hagen.ma.gundermann.matcherexamples.wrapper;

import static org.junit.Assert.assertFalse;
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
 * <b>checkType#attr &equiv;<sub>Exact,Gen,Spec</sub> queryType#attr</b>
 */
public class WrappedTypeMatcher_Wrapper_ConversionTest {

  private WrappedTypeMatcher matcher = new WrappedTypeMatcher(
      MatcherCombiner.combine( new ExactTypeMatcher(), new GenSpecTypeMatcher() ) );

  /**
   * {@link SuperWrapper}#wrapped &equiv;<sub>Exact</sub> {@link SuperClass}
   */
  @Test
  public void convertSuperWrapper2SuperClass() {
    SuperClass offeredComponent = new SuperClass( "A" );
    Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos(
        SuperWrapper.class, SuperClass.class );
    // Der WrappedTypeMatcher erzeugt nur eine ModuleMatchingInfo (kein rekursives Matching)
    ModuleMatchingInfo moduleMatchingInfo = matchingInfos.iterator().next();

    // Das Attribut an welches der Aufruf delegiert wird, wird vom TypeMatcher ermittelt und im ProxyFactoryCreator
    // innerhalb der ModuleMatchingInfo hinterlegt.
    ProxyFactory<SuperWrapper> proxyFactory = moduleMatchingInfo.getConverterCreator()
        .createProxyFactory( SuperWrapper.class );
    Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

    SuperWrapper proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

    // Das Wrapped-Objekt wird in das Attribut des Wrappers injiziert. Methodenaufrufe am Wrapper-Objekt werden nicht an
    // andere Objekte delegiert.
    assertTrue( proxy.toString().equals( "WRAPPED_A" ) );
    assertFalse( proxy.hashCode() == offeredComponent.hashCode() );
  }

  /**
   * {@link SuperWrapper}#wrapped &equiv;<sub>Gen</sub> {@link SubClass} <br>
   * Upcast
   */
  @Test
  public void convertSuperWrapper2SubClass() {
    SubClass offeredComponent = new SubClass( "A" );
    Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos(
        SuperWrapper.class, SubClass.class );
    // Der WrappedTypeMatcher erzeugt nur eine ModuleMatchingInfo (kein rekursives Matching)
    ModuleMatchingInfo moduleMatchingInfo = matchingInfos.iterator().next();

    // Das Attribut an welches der Aufruf delegiert wird, wird vom TypeMatcher ermittelt und im ProxyFactoryCreator
    // innerhalb der ModuleMatchingInfo hinterlegt.
    ProxyFactory<SuperWrapper> proxyFactory = moduleMatchingInfo.getConverterCreator()
        .createProxyFactory( SuperWrapper.class );
    Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

    SuperWrapper proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

    // Das Wrapped-Objekt wird in das Attribut des Wrappers injiziert. Methodenaufrufe am Wrapper-Objekt werden nicht an
    // andere Objekte delegiert.
    assertTrue( proxy.toString().equals( "WRAPPED_SubA" ) );
    assertFalse( proxy.hashCode() == offeredComponent.hashCode() );
  }

  /**
   * {@link SubWrapper}#wrapped &equiv;<sub>Gen</sub> {@link SuperClass} <br>
   * Downcast mit positivem Aufruf
   */
  @Test
  public void convertSubWrapper2SuperClass_positiveCall() {
    SuperClass offeredComponent = new SuperClass( "A" );
    Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos(
        SubWrapper.class, SuperClass.class );
    // Der WrappedTypeMatcher erzeugt nur eine ModuleMatchingInfo (kein rekursives Matching)
    ModuleMatchingInfo moduleMatchingInfo = matchingInfos.iterator().next();

    // Das Attribut an welches der Aufruf delegiert wird, wird vom TypeMatcher ermittelt und im ProxyFactoryCreator
    // innerhalb der ModuleMatchingInfo hinterlegt.
    ProxyFactory<SubWrapper> proxyFactory = moduleMatchingInfo.getConverterCreator()
        .createProxyFactory( SubWrapper.class );
    Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

    SubWrapper proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

    // Das Wrapped-Objekt wird in das Attribut des Wrappers injiziert. Methodenaufrufe am Wrapper-Objekt werden nicht an
    // andere Objekte delegiert.
    // Da das injizierte Objekt aber auch ein Proxy ist, wird der Aufruf, der innerhalb des Wrappers stattfindet wieder
    // an die offeredComponent delegiert
    assertTrue( proxy.toStringWithPrefix().equals( "WRAPPED_A" ) );
    assertFalse( proxy.hashCode() == offeredComponent.hashCode() );
  }

  /**
   * {@link SubWrapper}#wrapped &equiv;<sub>Gen</sub> {@link SuperClass} <br>
   * Downcast mit negativem Aufruf
   */
  @Test( expected = SigMaGlueException.class )
  public void convertSubWrapper2SuperClass_negativeCall() {
    SuperClass offeredComponent = new SuperClass( "A" );
    Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos(
        SubWrapper.class, SuperClass.class );
    // Der WrappedTypeMatcher erzeugt nur eine ModuleMatchingInfo (kein rekursives Matching)
    ModuleMatchingInfo moduleMatchingInfo = matchingInfos.iterator().next();

    // Das Attribut an welches der Aufruf delegiert wird, wird vom TypeMatcher ermittelt und im ProxyFactoryCreator
    // innerhalb der ModuleMatchingInfo hinterlegt.
    ProxyFactory<SubWrapper> proxyFactory = moduleMatchingInfo.getConverterCreator()
        .createProxyFactory( SubWrapper.class );
    Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

    SubWrapper proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

    // Das Wrapped-Objekt wird in das Attribut des Wrappers injiziert. Methodenaufrufe am Wrapper-Objekt werden nicht an
    // andere Objekte delegiert.
    // Da das injizierte Objekt aber auch ein Proxy ist, wird der Aufruf, der innerhalb des Wrappers stattfindet wieder
    // an die offeredComponent delegiert. Die aufgerufene Methoden steht aber in der Super-Class nicht zur Verfuegung
    proxy.toString().equals( "WRAPPED_A" );
  }

}
