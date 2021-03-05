package de.fernuni.hagen.ma.gundermann.matcherexamples.spec;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SuperClass;
import glue.ProxyFactory;
import glue.SigMaGlueException;
import matching.methods.MethodMatchingInfo;
import matching.modules.GenSpecTypeMatcher;
import matching.modules.ModuleMatchingInfo;

/**
 * Matcher Test fuer die Konvertierung fuer uebereinstimmende Typen in der Form:<br>
 * <b>checkType < queryType</b>
 */
public class GenSpecTypeMatcher_Spec_ConversionTest {

  /**
   * Downcast mit erfolgreichem Methodenaufruf
   */
  @Test
  public void convertGen2Spec_positivCall() {
    SuperClass offeredComponent = new SuperClass( "A" );
    Collection<ModuleMatchingInfo> matchingInfos = new GenSpecTypeMatcher().calculateTypeMatchingInfos(
        SubClass.class, SuperClass.class );
    // Der GenSpecTypeMatcher erzeugt nur eine ModuleMatchingInfo (kein rekursives Matching)
    ModuleMatchingInfo moduleMatchingInfo = matchingInfos.iterator().next();

    ProxyFactory<SubClass> proxyFactory = moduleMatchingInfo.getConverterCreator().createProxyFactory( SubClass.class );
    Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

    SubClass proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

    assertTrue( proxy.getString().equals( "A" ) );
  }

  /**
   * Downcast mit fehlschlagendem Methodenaufruf
   */
  @Test( expected = SigMaGlueException.class )
  public void convertGen2Spec_negativeCall() {
    SuperClass offeredComponent = new SuperClass( "A" );
    Collection<ModuleMatchingInfo> matchingInfos = new GenSpecTypeMatcher().calculateTypeMatchingInfos(
        SubClass.class, SuperClass.class );
    // Der GenSpecTypeMatcher erzeugt nur eine ModuleMatchingInfo (kein rekursives Matching)
    ModuleMatchingInfo moduleMatchingInfo = matchingInfos.iterator().next();

    ProxyFactory<SubClass> proxyFactory = moduleMatchingInfo.getConverterCreator().createProxyFactory( SubClass.class );
    Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

    SubClass proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

    // Beim Aufruf von getString wird die Methode direkt an die offeredComponent delegiert. Daher wird das Praefix "Sub"
    // (siehe SubClass) nicht mitgeliefert
    assertFalse( proxy.getString().equals( "SubA" ) );
    assertTrue( proxy.getString().equals( "A" ) );

    // Wenn nun Methoden aufgerufen werden, die auf den vererbten Attributen operieren, schlägt der Aufruf fehl
    proxy.getStringWithoutPrefix();
  }

}
