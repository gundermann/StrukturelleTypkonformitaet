package glue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.ProxyFactory;
import de.fernuni.hagen.ma.gundermann.signaturematching.ProxyFactoryCreator;

public class InterfaceProxyFactory<T> implements ProxyFactory<T> {

  private final Class<T> targetStructure;

  public InterfaceProxyFactory( Class<T> targetStructure ) {
    this.targetStructure = targetStructure;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public T createProxy( Map<Object, Collection<MethodMatchingInfo>> components2MatchingInfo ) {
    InvocationHandler invocationHandler = new BehaviourDelegateInvocationHandler( components2MatchingInfo );

    return (T) Proxy.newProxyInstance( this.getClass().getClassLoader(),

        // ClassLoader.getSystemClassLoader(),
        // Die Verwendung des SystemClassLoaders führt zum Fehler, wenn die targetStructure in dem Modul, in dem der
        // Aufruf des Proxies initiiert wird, nicht bekannt ist.
        // Exception in thread "main" java.lang.IllegalArgumentException: interface ... is not visible from class loader

        new Class<?>[] { targetStructure }, invocationHandler );
  }

  @Override
  public T createProxy( Object component, Collection<MethodMatchingInfo> matchingInfos ) {
    Map<Object, Collection<MethodMatchingInfo>> components2MatchingInfo = new HashMap<>();
    components2MatchingInfo.put( component, matchingInfos );
    return createProxy( components2MatchingInfo );
  }

  static class InterfaceProxyFactoryCreator implements ProxyFactoryCreator {

    @Override
    public <T> ProxyFactory<T> createProxyFactory( Class<T> targetType ) {
      return new InterfaceProxyFactory<>( targetType );
    }

  }

}
