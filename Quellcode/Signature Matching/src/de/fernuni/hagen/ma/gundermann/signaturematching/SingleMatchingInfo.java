package de.fernuni.hagen.ma.gundermann.signaturematching;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import matching.MatcherRate;
import matching.MatchingSupplier;
import matching.Setting;

public class SingleMatchingInfo {

	// Hier stehen nur Methoden drin, bei deren Aufruf auch an ein anderes Objekt
	// delegiert werden muss. Methoden, die von dem Objekt selbst ausgefuehrt werden
	// koennen, stehen hier nicht drin. D.h. bei einem Exact Matching waere diese
	// Liste leer.
	private final Collection<Method> matchedSourceMethods;

	private final Map<Method, MethodMatchingInfo> methodMatchingInfos;

	private final Class<?> source;

	private final Class<?> target;

	private final ProxyFactoryCreator converterCreator;

	private SingleMatchingInfo(Class<?> source, Class<?> target, ProxyFactoryCreator converterCreator) {
		this.source = source;
		this.target = target;
		this.converterCreator = converterCreator;
		this.methodMatchingInfos = new HashMap<Method, MethodMatchingInfo>();
		this.matchedSourceMethods = new ArrayList<Method>();

	}

	public Class<?> getSource() {
		return source;
	}

	public Class<?> getTarget() {
		return target;
	}

	public ProxyFactoryCreator getConverterCreator() {
		return this.converterCreator;
	}

	public Collection<Method> getMatchedSourceMethods() {
		return matchedSourceMethods;
	}


	public Map<Method, MethodMatchingInfo> getMethodMatchingInfos() {
		return methodMatchingInfos;
	}

	public static class Builder {

		private final SingleMatchingInfo info;

		public Builder(Class<?> source, Class<?> target, ProxyFactoryCreator converterCreator) {
			this.info = new SingleMatchingInfo(source, target, converterCreator);
		}

		public Builder withMatchedSourceMethods(Collection<Method> matchedSourceMethods) {
			this.info.matchedSourceMethods.clear();
			this.info.matchedSourceMethods.addAll(matchedSourceMethods);
			return this;
		}

		public Builder withMethodMatchingInfos(Map<Method, MethodMatchingInfo> methodMatchingInfos) {
			this.info.methodMatchingInfos.clear();
			this.info.methodMatchingInfos.putAll(methodMatchingInfos);
			return this;
		}

		public SingleMatchingInfo build() {
			return info;
		}
	}

	public boolean isFullMatching() {
		return matchedSourceMethods.size() == methodMatchingInfos.keySet().size()
				&& methodMatchingInfos.keySet().containsAll(matchedSourceMethods);
	}

}
