package glue;

import java.util.Collection;

import matching.methods.MethodMatchingInfo;

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
