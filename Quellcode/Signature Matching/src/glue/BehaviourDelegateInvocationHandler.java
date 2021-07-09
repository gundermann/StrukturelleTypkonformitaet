package glue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import matching.MatchingInfo;
import matching.methods.MethodMatchingInfo;
import matching.methods.MethodMatchingInfo.ParamPosition;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import util.Logger;

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
				return invokeOnComponentWithMatchingInfo(optMatchingInfo.get(), args);
			} catch (Throwable e) {
				throw new SigMaGlueException(e, method);
			}
		}

		// Default-Methode
		// (Hierbei handelt es sich um Methoden, dei von jedem Objekt erf�llt werden
		// und
		// f�r die aufgrund einer fehlenden
		// Implementierung keine MatchingInfo existiert)
		try {
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
		MatchingInfo returnTypeMatchingInfo = methodMatchingInfo.getReturnTypeMatchingInfo();
		if (returnTypeMatchingInfo == null
//				|| returnTypeMatchingInfo.isSubstitutable()
		) {
			return returnValue;
		}
		// Bei einem allgemeineren returnValue des Targets (Target.retrunValue >
		// Source.returnValue) muss der ReturnType
		// ebenfalls gemocked werden

		return convertType(returnValue, returnTypeMatchingInfo);
	}

	private Object[] convertArgs(Object[] args, Map<ParamPosition, MatchingInfo> argMMI) {
		if (args == null) {
			return null;
		}
		Object[] convertedArgs = new Object[args.length];
		for (int i = 0; i < args.length; i++) {
			Entry<ParamPosition, MatchingInfo> moduleMatchingInfoEntry = getParameterWithRepectToPosition(i, argMMI);
			if (moduleMatchingInfoEntry != null && moduleMatchingInfoEntry.getValue() != null
					&& moduleMatchingInfoEntry.getKey() != null) {
				ParamPosition paramPosition = moduleMatchingInfoEntry.getKey();
				MatchingInfo moduleMatchingInfo = moduleMatchingInfoEntry.getValue();
				Object convertedArg = convertType(args[paramPosition.getSourceParamPosition()], moduleMatchingInfo);
				convertedArgs[paramPosition.getTargetParamPosition()] = convertedArg;
			} else {
				// Keine Konvertierung notwendig
				convertedArgs[i] = args[i];
			}
		}
		return convertedArgs;
	}

	private Entry<ParamPosition, MatchingInfo> getParameterWithRepectToPosition(int sourceParamPosition,
			Map<ParamPosition, MatchingInfo> argMMI) {
		return argMMI.entrySet().stream().filter(e -> e.getKey().getSourceParamPosition().equals(sourceParamPosition))
				.findFirst().orElse(null);
	}

	/**
	 * Rekursiver Aufruf des gesamten Konvertierungsprozesses
	 *
	 * @param sourceType
	 * @param moduleMatchingInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <RT> RT convertType(Object sourceType, MatchingInfo moduleMatchingInfo) {
		Logger.info(String.format("convert type %s -> %s", moduleMatchingInfo.getSource().getName(),
				moduleMatchingInfo.getTarget().getName()));
		// Wenn das zu konvertierende Objekt null ist, dann kann dies auch gleich
		// zur�ckgegeben werden, da null-Objekte
		// keinen speziellen Typ haben muessen
		// if ( sourceType == null ||
		// moduleMatchingInfo.getMethodMatchingInfos().isEmpty()
		// && moduleMatchingInfo.getTargetDelegate() == null
		// && moduleMatchingInfo.getSourceDelegate() == null ) {
		// return (RT) sourceType;
		// }

		if (sourceType == null) {
			return (RT) sourceType;
		}

		Object source = sourceType;
		// if ( moduleMatchingInfo.getSourceDelegate() != null ) {
		// source = moduleMatchingInfo.getSourceDelegate().apply( sourceType );
		// }
		Map<Object, Collection<MethodMatchingInfo>> comp2MatchingInfo = new HashMap<>();
		comp2MatchingInfo.put(source, moduleMatchingInfo.getMethodMatchingInfoSupplier().values().stream()
				.map(Supplier::get).flatMap(Collection::stream).collect(Collectors.toList()));
		List<ConvertableComponent> convertableComponents = comp2MatchingInfo.entrySet().stream().map(e -> new ConvertableComponent(e.getKey(), e.getValue()))
				.collect(Collectors.toList());
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
		for (Entry<ParamPosition, MatchingInfo> argMMIEntry : mmi.getArgumentTypeMatchingInfos().entrySet()) {
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

	private static class ComponentWithMatchingInfo {
		private final Object component;

		private final MethodMatchingInfo matchingInfo;

		private ComponentWithMatchingInfo(Object component, MethodMatchingInfo matchingInfo) {
			this.component = component;
			this.matchingInfo = matchingInfo;
		}

		public Object getComponent() {
			return component;
		}

		public MethodMatchingInfo getMatchingInfo() {
			return matchingInfo;
		}

	}

}
