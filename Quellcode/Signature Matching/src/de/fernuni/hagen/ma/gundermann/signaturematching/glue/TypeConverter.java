package de.fernuni.hagen.ma.gundermann.signaturematching.glue;

import de.fernuni.hagen.ma.gundermann.signaturematching.ProxyFactory;
import de.fernuni.hagen.ma.gundermann.signaturematching.ProxyFactoryCreator;

public class TypeConverter<T> {

  private final ProxyFactory<T> proxyFactory;

  public TypeConverter( Class<T> targetStructure ) {
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

  public T convert( ConvertableBundle convertable ) {
    T targetInstance = proxyFactory.createProxy( convertable.getComponentsWithMethodMatchingInfos() );
    return targetInstance;
  }

}
