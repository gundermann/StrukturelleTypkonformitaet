package de.fernuni.hagen.ma.gundermann.descostest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum ComponentContainer {
	CONTAINER;

	Map<Class<?>, Object> containerMap = new HashMap<>();

	public void reset() {
		containerMap.clear();
	}

	public <BI> void registerComponent(Class<BI> compInterface, BI comp) {
		containerMap.put(compInterface, comp);
	}

	public Optional<?> getOptComponent(Class<?> componentClass) {
		return Optional.ofNullable(containerMap.get(componentClass));
	}

	public Class<?>[] getRegisteredComponentInterfaces() {
		return containerMap.keySet().toArray(new Class[] {});
	}

}
