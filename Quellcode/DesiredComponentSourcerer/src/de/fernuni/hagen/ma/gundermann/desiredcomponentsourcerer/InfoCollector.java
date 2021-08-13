package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.HashMap;
import java.util.Map;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;

public enum InfoCollector {
	INSTANCE;

	private int matchingProvidedTypes = 0;

	private int testedProxies = 0;

	private Map<Integer, Double> iteration2CombinationCount = new HashMap<Integer, Double>();

	private Map<Integer, Double> iteration2CreateProxyCount = new HashMap<Integer, Double>();

	static void reset() {
		INSTANCE.testedProxies = 0;
		INSTANCE.matchingProvidedTypes = 0;
		INSTANCE.iteration2CombinationCount = new HashMap<Integer, Double>();
		INSTANCE.iteration2CreateProxyCount = new HashMap<Integer, Double>();
	}

	static void incrementTestedProxies() {
		INSTANCE.testedProxies++;
	}

	public static void incrementCreatedProxiesInIterationStep(int iteration) {
		if(INSTANCE.iteration2CreateProxyCount.containsKey(iteration)) {
			Double d = INSTANCE.iteration2CreateProxyCount.get(iteration)+1;
			INSTANCE.iteration2CreateProxyCount.put(iteration, d);
		}else {
			INSTANCE.iteration2CreateProxyCount.put(iteration, 1d);
		}
	}

	static void logInfos() {
		Logger.infoF("Tested Components variations: %d", INSTANCE.testedProxies);
		Logger.infoF("Anzahl strukturell matchender provided Typen: %d", INSTANCE.matchingProvidedTypes);
		Logger.infoF("Benötigte Iterationsschritte: %d", INSTANCE.iteration2CombinationCount.keySet().size());
		INSTANCE.iteration2CombinationCount.entrySet()
				.forEach(e -> Logger.infoF("Durchlauf: %d \n Anzahl Kombinationen: %f \n Erzeugte Proxies: %f",
						e.getKey(), e.getValue(), INSTANCE.iteration2CreateProxyCount.get(e.getKey())));
	}

	public static void addCombinationCountInIteration(double count, int iteration) {
		INSTANCE.iteration2CombinationCount.put(iteration, count);
	}

	static void setMatchingProvidedTypeCount(int count) {
		INSTANCE.matchingProvidedTypes = count;
	}

}
