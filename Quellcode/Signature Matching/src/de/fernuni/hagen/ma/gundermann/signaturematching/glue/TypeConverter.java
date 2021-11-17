package de.fernuni.hagen.ma.gundermann.signaturematching.glue;

import de.fernuni.hagen.ma.gundermann.signaturematching.ProxyFactory;
import de.fernuni.hagen.ma.gundermann.signaturematching.ProxyFactoryCreator;
import de.fernuni.hagen.ma.gundermann.signaturematching.glue.factories.StructProxyFactory;
import de.fernuni.hagen.ma.gundermann.signaturematching.glue.factories.SubProxyFactory;

/**
 * Konverter fuer einen Source-Typ
 * 
 * @author Niels Gundermann
 *
 * @param <T> Source-Typ
 */
public class TypeConverter<T> {

	private final ProxyFactory<T> proxyFactory;

	public TypeConverter(Class<T> targetStructure) {
		if (targetStructure.isInterface()) {
			proxyFactory = new StructProxyFactory<>(targetStructure);
		} else {
			proxyFactory = new SubProxyFactory<>(targetStructure);
		}
	}

	public TypeConverter(Class<T> targetStructure, ProxyFactoryCreator factoryCreator) {
		this.proxyFactory = factoryCreator.createProxyFactory(targetStructure);
	}

	public T convert(ConvertableBundle convertable) {
		T targetInstance = proxyFactory.createProxy(convertable.getComponentsWithMethodMatchingInfos());
		return targetInstance;
	}

}
