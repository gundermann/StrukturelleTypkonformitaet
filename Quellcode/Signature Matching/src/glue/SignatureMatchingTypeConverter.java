package glue;

import matching.modules.ModuleMatchingInfo;

public class SignatureMatchingTypeConverter<T> {

  // private final Class<T> targetStructure;

  private final ProxyFactory<T> proxyFactory;

  // Was ist mit ENUMs?
  public SignatureMatchingTypeConverter( Class<T> targetStructure ) {
    // this.targetStructure = targetStructure;
    // Grundlagen pruefen:
    // Interface oder Klasse
    // if ( this.targetStructure.isInterface() ) {
    proxyFactory = new InterfaceProxyFactory<>( targetStructure );
    // }
    // else {
    // // keine finalisierte Klasse
    // // Default-Kontruktor vorhanden
    // proxyFactory = new ClassProxyFactory<>( targetStructure );
    // }
  }

  public T convert( Object component, ModuleMatchingInfo<T> matchingInfo ) {
    T targetInstance = proxyFactory.createProxy( component, matchingInfo );
    return targetInstance;
  }

}
