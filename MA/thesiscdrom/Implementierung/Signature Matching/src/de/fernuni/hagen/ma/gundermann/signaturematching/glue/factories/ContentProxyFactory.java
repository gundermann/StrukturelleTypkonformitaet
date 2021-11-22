package de.fernuni.hagen.ma.gundermann.signaturematching.glue.factories;

import java.lang.reflect.Field;
import java.util.Collection;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.ProxyFactory;
import de.fernuni.hagen.ma.gundermann.signaturematching.ProxyFactoryCreator;
import de.fernuni.hagen.ma.gundermann.signaturematching.util.Logger;

/**
 * {@link ProxyFactory} fuer Content-Proxies
 * 
 * @author Niels Gundermann
 *
 * @param <T> Source-Type
 */
public class ContentProxyFactory<T> implements ProxyFactory<T> {

	private final Class<T> sourceType;

	private final String delegationField;

	public ContentProxyFactory(Class<T> sourceType, String delegationField) {
		this.sourceType = sourceType;
		this.delegationField = delegationField;
	}

	@Override
	public T createProxy(Object component, Collection<MethodMatchingInfo> matchingInfos) {
		try {
			Field wrappedField = getDeclaredFieldOfClassHierachry(component.getClass(), delegationField);
			if (wrappedField == null) {
				logFieldError(delegationField, component.getClass().getName());
			}
			wrappedField.setAccessible(true);
			ProxyFactory<T> proxyFactory = getRelevantProxyFactoryCreator(matchingInfos).createProxyFactory(sourceType);
			return proxyFactory.createProxy(wrappedField.get(component), matchingInfos);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logFieldError(delegationField, component.getClass().getName());
		}
		return null;

	}

	private ProxyFactoryCreator getRelevantProxyFactoryCreator(Collection<MethodMatchingInfo> matchingInfos) {
		return matchingInfos.isEmpty() ? ProxyCreatorFactories.getIdentityFactoryCreator()
				: ProxyCreatorFactories.getClassProxyFactoryCreator();
	}

	private void logFieldError(String fieldname, String classname) {
		Logger.err(String.format("field %s not found in class hierarchy of %s", fieldname, classname));
	}

	private Field getDeclaredFieldOfClassHierachry(Class<? extends Object> startClass, String fieldName) {
		Field declaredField = null;
		try {
			declaredField = startClass.getDeclaredField(fieldName);
		} catch (NoSuchFieldException | SecurityException e) {
			if (startClass.getSuperclass() != null) {
				return getDeclaredFieldOfClassHierachry(startClass.getSuperclass(), fieldName);
			}
		}
		return declaredField;

	}

}
