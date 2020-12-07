package matching.modules;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import matching.methods.MethodMatchingInfo;

public class PartlyTypeMatchingInfoFactory {

	private final Class<?> targetType;


	public PartlyTypeMatchingInfoFactory(Class<?> targetType) {
		this.targetType = targetType;
	}

	public PartlyTypeMatchingInfo create() {
		return this.create(new ArrayList<>(), new HashMap<>() );
	}

	public PartlyTypeMatchingInfo create(Collection<Method> sourceMethods, Map<Method, Supplier<Collection<MethodMatchingInfo>>> methodMatchingInfos) {
		return new PartlyTypeMatchingInfo(targetType, sourceMethods, methodMatchingInfos);
	}


}
