package glue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import matching.methods.MethodMatchingInfo;

public class ConvertableBundle {
	private Collection<ConvertableComponent> components;

	private ConvertableBundle(Collection<ConvertableComponent> components) {
		this.components = components;
	}

	public static boolean canCreateBundle(Collection<ConvertableComponent> components) {
		return checkComponentsForBundle(components);
	}

	private static boolean checkComponentsForBundle(Collection<ConvertableComponent> components) {
		Collection<ConvertableComponent> tempComponents = new ArrayList<ConvertableComponent>(components);
		for (ConvertableComponent comp : components) {
			tempComponents.remove(comp);
			boolean hasDoubleDelegate = comp.getMethodMatchingInfos().stream().map(MethodMatchingInfo::getSource)
					.noneMatch(m -> methodInOtherComponent(tempComponents, m));
			if (hasDoubleDelegate) {
				return false;
			}
		}
		return true;
	}

	private static boolean methodInOtherComponent(Collection<ConvertableComponent> tempComponents, Method m) {
		return tempComponents.stream()
				.flatMap(cc -> cc.getMethodMatchingInfos().stream().map(MethodMatchingInfo::getSource))
				.collect(Collectors.toList()).contains(m);
	}

	public static ConvertableBundle createBundle(Collection<ConvertableComponent> components) {
		return new ConvertableBundle(components);
	}

	public Map<Object, Collection<MethodMatchingInfo>> getComponentsWithMethodMatchingInfos() {
		return components.stream().collect(
				Collectors.toMap(ConvertableComponent::getObject, ConvertableComponent::getMethodMatchingInfos));
	}
}
