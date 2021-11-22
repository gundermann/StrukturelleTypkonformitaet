package de.fernuni.hagen.ma.gundermann.signaturematching.glue.factories;

import java.util.Collection;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.ProxyFactory;
import de.fernuni.hagen.ma.gundermann.signaturematching.ProxyFactoryCreator;

public final class ProxyCreatorFactories {

	private ProxyCreatorFactories() {
	}

	public static ProxyFactoryCreator getInterfaceProxyFactoryCreator() {
		return new ProxyFactoryCreator() {

			@Override
			public <T> ProxyFactory<T> createProxyFactory(Class<T> targetType) {
				return new StructProxyFactory<T>(targetType);
			}
		};
	}

	public static ProxyFactoryCreator getWrapperFactoryCreator(String delegateAttr) {
		return new ProxyFactoryCreator() {

			@Override
			public <T> ProxyFactory<T> createProxyFactory(Class<T> targetType) {
				return new ContainerProxyFactory<>(targetType, delegateAttr);
			}
		};
	}

	public static ProxyFactoryCreator getClassProxyFactoryCreator() {
		return new ProxyFactoryCreator() {

			@Override
			public <T> ProxyFactory<T> createProxyFactory(Class<T> targetType) {
				return new SubProxyFactory<>(targetType);
			}
		};
	}

	public static ProxyFactoryCreator getWrappedFactoryCreator(String delegateAttr) {
		return new ProxyFactoryCreator() {

			@Override
			public <T> ProxyFactory<T> createProxyFactory(Class<T> targetType) {
				return new ContentProxyFactory<>(targetType, delegateAttr);
			}
		};
	}

	public static ProxyFactoryCreator getIdentityFactoryCreator() {
		return new ProxyFactoryCreator() {

			@Override
			public <T> ProxyFactory<T> createProxyFactory(Class<T> targetType) {
				return new ProxyFactory<T>() {

					@SuppressWarnings("unchecked")
					@Override
					public T createProxy(Object component, Collection<MethodMatchingInfo> matchingInfos) {
						return (T) component;
					}
				};
			}
		};
	}
}
