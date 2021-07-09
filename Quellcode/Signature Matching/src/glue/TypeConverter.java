package glue;

import java.util.Collection;
import java.util.Map;

import matching.methods.MethodMatchingInfo;

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

  public TypeConverter( Class<T> targetStructure, ProxyFactoryCreator factoryCreator ) {
    this.proxyFactory = factoryCreator.createProxyFactory( targetStructure );
  }

  public T convert( ConvertableBundle convertable ) {
    T targetInstance = proxyFactory.createProxy( convertable.getComponentsWithMethodMatchingInfos() );
    return targetInstance;
  }

}
