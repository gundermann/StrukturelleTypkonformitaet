package de.fernuni.hagen.ma.gundermann.matcherexamples.gen;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SuperClass;
import glue.ProxyFactory;
import matching.methods.MethodMatchingInfo;
import matching.modules.GenSpecTypeMatcher;
import matching.modules.ModuleMatchingInfo;

/**
 * Matcher Test fuer die Konvertierung fuer uebereinstimmende Typen in der Form:<br>
 * <b>checkType > queryType </b>
 */
public class GenSpecTypeMatcher_Gen_ConversionTest {

  @Test
  public void convertSpec2Gen() {
    SubClass offeredComponent = new SubClass( "A" );
    Collection<ModuleMatchingInfo> matchingInfos = new GenSpecTypeMatcher().calculateTypeMatchingInfos(
        SuperClass.class, SubClass.class );
    // Der GenSpecTypeMatcher erzeugt nur eine ModuleMatchingInfo (kein rekursives Matching)
    ModuleMatchingInfo moduleMatchingInfo = matchingInfos.iterator().next();

    ProxyFactory<SuperClass> proxyFactory = moduleMatchingInfo.getConverterCreator()
        .createProxyFactory( SuperClass.class );
    Collection<MethodMatchingInfo> methodMatchingInfos = moduleMatchingInfo.getMethodMatchingInfos();

    // Bei der Erzeugung dieses Proxies wird auch hier, wie im ExactTypeMatcher, die offeredComponent einfach
    // zurückgegeben (Substitionsprinzip)
    SuperClass proxy = proxyFactory.createProxy( offeredComponent, methodMatchingInfos );

    assertTrue( proxy.getString().equals( "SubA" ) );
  }

}
