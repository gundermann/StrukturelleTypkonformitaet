package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.util.Collection;
import java.util.function.Predicate;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.CombinationPartInfo;

public class SelfCombinatedPartFilter implements Predicate<Collection<CombinationPartInfo>> {

	@Override
	public boolean test(Collection<CombinationPartInfo> col) {
		if (col.isEmpty()) {
			return false;
		}
		if (col.size() == 1) {
			return true;
		}
		CombinationPartInfo info = col.iterator().next();
		Class<?> checkCC = info.getComponentClass();
		return col.stream().map(CombinationPartInfo::getComponentClass).anyMatch(cc -> !cc.equals(checkCC));
	}

}
