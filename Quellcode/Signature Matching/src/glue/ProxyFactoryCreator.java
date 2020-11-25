package glue;

public interface ProxyFactoryCreator {

  <T> ProxyFactory<T> createProxyFactory( Class<T> targetType );
}
