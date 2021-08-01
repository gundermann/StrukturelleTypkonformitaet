package de.fernuni.hagen.ma.gundermann.signaturematching.matching;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.ProxyFactoryCreator;

public class MatchingInfo {

	// Hier stehen nur Methoden drin, bei deren Aufruf auch an ein anderes Objekt
	// delegiert werden muss. Methoden, die von dem Objekt selbst ausgefuehrt werden
	// koennen, stehen hier nicht drin. D.h. bei einem Exact Matching waere diese
	// Liste leer.
	private final Collection<Method> matchedSourceMethods;

	private final Map<Method, MatchingSupplier> methodMatchingSupplier;

	private final Class<?> source;

	private final Class<?> target;

	private final ProxyFactoryCreator converterCreator;

	private MatchingInfo(Class<?> source, Class<?> target, ProxyFactoryCreator converterCreator) {
		this.source = source;
		this.target = target;
		this.converterCreator = converterCreator;
		this.methodMatchingSupplier = new HashMap<Method, MatchingSupplier>();
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

	public Map<Method, MatchingSupplier> getMethodMatchingSupplier() {
		return methodMatchingSupplier;
	}

	public Map<Method, Supplier<Collection<MethodMatchingInfo>>> getMethodMatchingInfoSupplier() {
		return methodMatchingSupplier.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().getMethodMatchingInfosSupplier()));
	}

	public Collection<MatcherRate> getQualitativeMatchRating() {
		return methodMatchingSupplier.values().stream().map(MatchingSupplier::getMatcherRating)
				.flatMap(Collection::stream).collect(Collectors.toList());
//		return Setting.QUALITATIVE_COMPONENT_MATCH_RATE_CUMULATION
//				.apply(methodMatchingSupplier.values().stream().map(MatchingSupplier::getMatcherRating));
	}

	public static class Builder {

		private final MatchingInfo info;

		public Builder(Class<?> source, Class<?> target, ProxyFactoryCreator converterCreator) {
			this.info = new MatchingInfo(source, target, converterCreator);
		}

		public Builder withMatchedSourceMethods(Collection<Method> matchedSourceMethods) {
			this.info.matchedSourceMethods.clear();
			this.info.matchedSourceMethods.addAll(matchedSourceMethods);
			return this;
		}

		public Builder withMethodMatchingInfoSupplier(Map<Method, MatchingSupplier> methodMatchingInfoSupplier) {
			this.info.methodMatchingSupplier.clear();
			this.info.methodMatchingSupplier.putAll(methodMatchingInfoSupplier);
			return this;
		}

		public MatchingInfo build() {
			return info;
		}
	}

	public boolean isFullMatching() {
		return matchedSourceMethods.size() == methodMatchingSupplier.keySet().size()
				&& methodMatchingSupplier.keySet().containsAll(matchedSourceMethods);
	}

}
