package glue;

import java.util.Collection;

import matching.methods.MethodMatchingInfo;

public final class ProxyCreatorFactories {

  private ProxyCreatorFactories() {
  }

  public static ProxyFactoryCreator getWrapperFactoryCreator( String delegateAttr ) {
    return new ProxyFactoryCreator() {

      @Override
      public <T> ProxyFactory<T> createProxyFactory( Class<T> targetType ) {
        return new WrapperProxyFactory<>( targetType, delegateAttr );
      }
    };
  }

  public static ProxyFactoryCreator getClassFactoryCreator() {
    return new ProxyFactoryCreator() {

      @Override
      public <T> ProxyFactory<T> createProxyFactory( Class<T> targetType ) {
        return new ClassProxyFactory<>( targetType );
      }
    };
  }

  public static ProxyFactoryCreator getWrappedFactoryCreator( String delegateAttr ) {
    return new ProxyFactoryCreator() {

      @Override
      public <T> ProxyFactory<T> createProxyFactory( Class<T> targetType ) {
        return new WrappedProxyFactory<>( delegateAttr );
      }
    };
  }

  public static ProxyFactoryCreator getIdentityFactoryCreator() {
    return new ProxyFactoryCreator() {

      @Override
      public <T> ProxyFactory<T> createProxyFactory( Class<T> targetType ) {
        return new ProxyFactory<T>() {

          @SuppressWarnings( "unchecked" )
          @Override
          public T createProxy( Object component, Collection<MethodMatchingInfo> matchingInfos ) {
            return (T) component;
          }
        };
      }
    };
  }
}
