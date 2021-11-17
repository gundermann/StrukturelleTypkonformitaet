package de.fernuni.hagen.ma.gundermann.signaturematching.glue;

import java.util.Collection;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;

public class ConvertableComponent {

	private final Object convertableObject;

	private final Collection<MethodMatchingInfo> methodMatchingInfos;

	public ConvertableComponent(Object convertableObject, Collection<MethodMatchingInfo> methodMatchingInfos) {
		this.convertableObject = convertableObject;
		this.methodMatchingInfos = methodMatchingInfos;

	}

	public Object getObject() {
		return convertableObject;
	}

	public Collection<MethodMatchingInfo> getMethodMatchingInfos() {
		return methodMatchingInfos;
	}

}
