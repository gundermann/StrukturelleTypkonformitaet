package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.util.Collection;
import java.util.function.Predicate;

import matching.modules.PartlyTypeMatchingInfo;

@Deprecated
public class SelfCombinatedComponentFilter implements Predicate<Collection<PartlyTypeMatchingInfo>> {

	@Override
	public boolean test(Collection<PartlyTypeMatchingInfo> col) {
		if (col.isEmpty()) {
			return false;
		}
		if (col.size() == 1) {
			return true;
		}
		PartlyTypeMatchingInfo typeMatchingInfo = col.iterator().next();
		Class<?> checkType = typeMatchingInfo.getCheckType();
		return col.stream().map(PartlyTypeMatchingInfo::getCheckType).anyMatch(t -> !t.equals(checkType));
	}

}
