package de.fernuni.hagen.ma.gundermann.matcherexamples.exact;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

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
    String offeredComponent = "A";
    Collection<ModuleMatchingInfo> matchingInfos = new ExactTypeMatcher().calculateTypeMatchingInfos( String.class,
        String.class );
    // Der ExactTypeMatcher erzeugt nur eine ModuleMatchingInfo (kein rekursives Matching)
    ModuleMatchingInfo moduleMatchingInfo = matchingInfos.iterator().next();

    ProxyFactory<String> proxyFactory = moduleMatchingInfo.getConverterCreator().createProxyFactory( String.class );
    Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

    // Bei der Erzeugung dieses Proxies wird die offeredComponent einfach zurückgegeben
    String proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

    assertTrue( proxy.equals( "A" ) );
  }

}
