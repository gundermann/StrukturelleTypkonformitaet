package de.fernuni.hagen.ma.gundermann.signaturematching.glue.delegation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo.ParamPosition;
import de.fernuni.hagen.ma.gundermann.signaturematching.glue.ConvertableBundle;
import de.fernuni.hagen.ma.gundermann.signaturematching.glue.ConvertableComponent;
import de.fernuni.hagen.ma.gundermann.signaturematching.glue.SigMaGlueException;
import de.fernuni.hagen.ma.gundermann.signaturematching.glue.TypeConverter;
import de.fernuni.hagen.ma.gundermann.signaturematching.util.Logger;
import de.fernuni.hagen.ma.gundermann.signaturematching.SingleMatchingInfo;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class BehaviourDelegateInvocationHandler implements MethodInterceptor, InvocationHandler {

	private final Map<Object, Collection<MethodMatchingInfo>> components2MatchingInfo;

	public BehaviourDelegateInvocationHandler(Object component, Collection<MethodMatchingInfo> methodMatchingInfos) {
		this.components2MatchingInfo = new HashMap<>();
		this.components2MatchingInfo.put(component, methodMatchingInfos);
	}

	public BehaviourDelegateInvocationHandler(Map<Object, Collection<MethodMatchingInfo>> components2MatchingInfo) {
		this.components2MatchingInfo = components2MatchingInfo;
	}

	@Override
	public Object intercept(Object callObject, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		Optional<ComponentWithMatchingInfo> optMatchingInfo = getMethodMatchingInfo(method);
		if (optMatchingInfo.isPresent()) {
			try {
				return invokeOnComponentWithMatchingInfo(optMatchingInfo.get(), args);
			} catch (Throwable e) {
				throw new SigMaGlueException(e, method);
			}
		}
		try {
			return methodProxy.invokeSuper(callObject, args);
		} catch (Throwable e) {
			throw new SigMaGlueException(e, method);
		}
	}

	@Override
	public Object invoke(Object methodProxy, Method method, Object[] args) throws Throwable {
		Optional<ComponentWithMatchingInfo> optMatchingInfo = getMethodMatchingInfo(method);
		if (optMatchingInfo.isPresent()) {
			try {
				// Ausfuehren der im Target-Type deklarierten Methoden (Delegation)
				return invokeOnComponentWithMatchingInfo(optMatchingInfo.get(), args);
			} catch (Throwable e) {
				throw new SigMaGlueException(e, method);
			}
		}
		try {
			// Ausfuehrung der im Source-Type deklarierten Methoden
			return method.invoke(getSingleComponent(), args);
		} catch (Throwable e) {
			throw new SigMaGlueException(e, method);
		}
	}

	private Object getSingleComponent() {
		if (components2MatchingInfo.size() == 1) {
			return components2MatchingInfo.keySet().iterator().next();
		}
		return null;
	}

	private Object invokeOnComponentWithMatchingInfo(ComponentWithMatchingInfo component, Object[] args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		MethodMatchingInfo methodMatchingInfo = component.getMatchingInfo();
		Method targetMethod = methodMatchingInfo.getTarget();
		Object[] convertedArgs = convertArgs(args, methodMatchingInfo.getArgumentTypeMatchingInfos());
		Object returnValue = targetMethod.invoke(component.getComponent(), convertedArgs);
		SingleMatchingInfo returnTypeMatchingInfo = methodMatchingInfo.getReturnTypeMatchingInfo();
		if (returnTypeMatchingInfo == null) {
			return returnValue;
		}
		return convertType(returnValue, returnTypeMatchingInfo);
	}

	private Object[] convertArgs(Object[] args, Map<ParamPosition, SingleMatchingInfo> argMMI) {
		if (args == null) {
			return null;
		}
		Object[] convertedArgs = new Object[args.length];
		for (int i = 0; i < args.length; i++) {
			Entry<ParamPosition, SingleMatchingInfo> moduleMatchingInfoEntry = getParameterWithRepectToPosition(i,
					argMMI);
			if (moduleMatchingInfoEntry != null && moduleMatchingInfoEntry.getValue() != null
					&& moduleMatchingInfoEntry.getKey() != null) {
				ParamPosition paramPosition = moduleMatchingInfoEntry.getKey();
				SingleMatchingInfo moduleMatchingInfo = moduleMatchingInfoEntry.getValue();
				Object convertedArg = convertType(args[paramPosition.getSourceParamPosition()], moduleMatchingInfo);
				convertedArgs[paramPosition.getTargetParamPosition()] = convertedArg;
			} else {
				// Keine Konvertierung notwendig
				convertedArgs[i] = args[i];
			}
		}
		return convertedArgs;
	}

	private Entry<ParamPosition, SingleMatchingInfo> getParameterWithRepectToPosition(int sourceParamPosition,
			Map<ParamPosition, SingleMatchingInfo> argMMI) {
		return argMMI.entrySet().stream().filter(e -> e.getKey().getSourceParamPosition().equals(sourceParamPosition))
				.findFirst().orElse(null);
	}

	@SuppressWarnings("unchecked")
	private <RT> RT convertType(Object sourceType, SingleMatchingInfo moduleMatchingInfo) {
		Logger.info(String.format("convert type %s -> %s", moduleMatchingInfo.getSource().getName(),
				moduleMatchingInfo.getTarget().getName()));
		if (sourceType == null) {
			return (RT) sourceType;
		}
		Object source = sourceType;
		Map<Object, Collection<MethodMatchingInfo>> comp2MatchingInfo = new HashMap<>();
		comp2MatchingInfo.put(source, moduleMatchingInfo.getMethodMatchingInfos().values());
		List<ConvertableComponent> convertableComponents = comp2MatchingInfo.entrySet().stream()
				.map(e -> new ConvertableComponent(e.getKey(), e.getValue())).collect(Collectors.toList());
		ConvertableBundle convertableBundle = ConvertableBundle.createBundle(convertableComponents);
		return new TypeConverter<>((Class<RT>) moduleMatchingInfo.getTarget(), moduleMatchingInfo.getConverterCreator())
				.convert(convertableBundle);
	}

	private Optional<ComponentWithMatchingInfo> getMethodMatchingInfo(Method method) {
		for (Entry<Object, Collection<MethodMatchingInfo>> entry : components2MatchingInfo.entrySet()) {
			for (MethodMatchingInfo mmi : entry.getValue()) {
				if (mmi.getSource().getName().equals(method.getName()) && argumentsMatches(mmi, method)) {
					return Optional.of(new ComponentWithMatchingInfo(entry.getKey(), mmi));
				}
			}
		}
		return Optional.empty();
	}

	private boolean argumentsMatches(MethodMatchingInfo mmi, Method method) {
		for (Entry<ParamPosition, SingleMatchingInfo> argMMIEntry : mmi.getArgumentTypeMatchingInfos().entrySet()) {
			if (method.getParameterCount() <= argMMIEntry.getKey().getSourceParamPosition()) {
				throw new RuntimeException("wrong parameter count");
			}
			Class<?> parameterType = method.getParameterTypes()[argMMIEntry.getKey().getSourceParamPosition()];
			if (!parameterType.equals(argMMIEntry.getValue().getSource())) {
				return false;
			}
		}
		return true;
	}

}
