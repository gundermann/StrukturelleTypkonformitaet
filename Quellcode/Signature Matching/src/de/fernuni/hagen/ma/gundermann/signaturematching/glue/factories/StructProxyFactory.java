package de.fernuni.hagen.ma.gundermann.signaturematching.glue.factories;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.ProxyFactory;
import de.fernuni.hagen.ma.gundermann.signaturematching.ProxyFactoryCreator;
import de.fernuni.hagen.ma.gundermann.signaturematching.glue.delegation.BehaviourDelegateInvocationHandler;

/**
 * {@link ProxyFactory} fuer strukturelle Proxies
 * 
 * @author Niels Gundermann
 *
 * @param <T> Source-Type
 */
public class StructProxyFactory<T> implements ProxyFactory<T> {

	private final Class<T> sourceType;

	public StructProxyFactory(Class<T> sourceType) {
		this.sourceType = sourceType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T createProxy(Map<Object, Collection<MethodMatchingInfo>> components2MatchingInfo) {
		InvocationHandler invocationHandler = new BehaviourDelegateInvocationHandler(components2MatchingInfo);

		return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[] { sourceType },
				invocationHandler);
	}

	@Override
	public T createProxy(Object component, Collection<MethodMatchingInfo> matchingInfos) {
		Map<Object, Collection<MethodMatchingInfo>> components2MatchingInfo = new HashMap<>();
		components2MatchingInfo.put(component, matchingInfos);
		return createProxy(components2MatchingInfo);
	}

	static class StructProxyFactoryCreator implements ProxyFactoryCreator {

		@Override
		public <T> ProxyFactory<T> createProxyFactory(Class<T> targetType) {
			return new StructProxyFactory<>(targetType);
		}

	}

}
