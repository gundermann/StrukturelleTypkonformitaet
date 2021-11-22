package de.fernuni.hagen.ma.gundermann.signaturematching;

public interface ProxyFactoryCreator {

  <T> ProxyFactory<T> createProxyFactory( Class<T> targetType );
}
