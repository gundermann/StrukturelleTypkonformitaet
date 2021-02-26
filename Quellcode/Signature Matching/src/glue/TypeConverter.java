package glue;

import java.util.Collection;
import java.util.Map;

import matching.methods.MethodMatchingInfo;

public class TypeConverter<T> {

  private final ProxyFactory<T> proxyFactory;

  // Was ist mit ENUMs?
  public TypeConverter( Class<T> targetStructure ) {
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

  TypeConverter( Class<T> targetStructure, ProxyFactoryCreator factoryCreator ) {
    this.proxyFactory = factoryCreator.createProxyFactory( targetStructure );
  }

  public T convert( Map<Object, Collection<MethodMatchingInfo>> components2MatchingInfo ) {
    T targetInstance = proxyFactory.createProxy( components2MatchingInfo );
    return targetInstance;
  }

}
