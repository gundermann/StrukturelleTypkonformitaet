package glue;

import java.util.Collection;
import java.util.Map;

import matching.methods.MethodMatchingInfo;

public class CombinationTypeConverter<T> {

  private final ProxyFactory<T> proxyFactory;

  // Was ist mit ENUMs?
  public CombinationTypeConverter( Class<T> targetStructure ) {
    // this.targetStructure = targetStructure;
    // Grundlagen pruefen:
    // Interface oder Klasse
    if ( targetStructure.isInterface() ) {
      proxyFactory = new InterfaceProxyFactory<>( targetStructure );
    }
    else {
      // keine finalisierte Klasse
      // Default-Kontruktor vorhanden
      proxyFactory = new ClassProxyFactory<>( targetStructure );
    }
  }

  public T convert( Map<Object, Collection<MethodMatchingInfo>> components2MatchingInfo ) {
    T targetInstance = proxyFactory.createProxy( components2MatchingInfo );
    return targetInstance;
  }

}
