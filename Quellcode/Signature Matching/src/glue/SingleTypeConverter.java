package glue;

import matching.modules.ModuleMatchingInfo;

public class SingleTypeConverter<T> {

  // private final Class<T> targetStructure;

  private final ProxyFactory<T> proxyFactory;

  // Was ist mit ENUMs?
  public SingleTypeConverter( Class<T> targetStructure ) {
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

  public SingleTypeConverter( Class<T> targetStructure, ProxyFactoryCreator factoryCreator ) {
    this.proxyFactory = factoryCreator.createProxyFactory( targetStructure );
  }

  public T convert( Object component, ModuleMatchingInfo matchingInfo ) {
    T targetInstance = proxyFactory.createProxy( component, matchingInfo );
    return targetInstance;
  }

}
