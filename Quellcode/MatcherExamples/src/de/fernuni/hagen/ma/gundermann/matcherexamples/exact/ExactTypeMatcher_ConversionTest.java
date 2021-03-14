package de.fernuni.hagen.ma.gundermann.matcherexamples.exact;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SuperClass;
import glue.ProxyFactory;
import matching.methods.MethodMatchingInfo;
import matching.modules.ExactTypeMatcher;
import matching.modules.ModuleMatchingInfo;

/**
 * Matcher Test fuer die Konvertierung fuer uebereinstimmende Typen in der Form:<br>
 * <b>queryType = checkType</b>
 */
public class ExactTypeMatcher_ConversionTest {

  @Test
  public void convertString() {
    SuperClass target = new SuperClass( "A" );
    Collection<ModuleMatchingInfo> matchingInfos = new ExactTypeMatcher().calculateTypeMatchingInfos( SuperClass.class,
        SuperClass.class );
    // Der ExactTypeMatcher erzeugt nur eine ModuleMatchingInfo (kein rekursives Matching)
    ModuleMatchingInfo moduleMatchingInfo = matchingInfos.iterator().next();

    ProxyFactory<SuperClass> proxyFactory = moduleMatchingInfo.getConverterCreator()
        .createProxyFactory( SuperClass.class );
    Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

    // Bei der Erzeugung dieses Proxies wird die offeredComponent einfach zurückgegeben
    SuperClass source = proxyFactory.createProxy( target, methodMatchingInfos );

    assertTrue( source.getString().equals( "A" ) );
  }

}
