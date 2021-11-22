package de.fernuni.hagen.ma.gundermann.signaturematching.glue.delegation;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;

class ComponentWithMatchingInfo {

	private final Object component;

	private final MethodMatchingInfo matchingInfo;

	ComponentWithMatchingInfo(Object component, MethodMatchingInfo matchingInfo) {
		this.component = component;
		this.matchingInfo = matchingInfo;
	}

	Object getComponent() {
		return component;
	}

	MethodMatchingInfo getMatchingInfo() {
		return matchingInfo;
	}

}