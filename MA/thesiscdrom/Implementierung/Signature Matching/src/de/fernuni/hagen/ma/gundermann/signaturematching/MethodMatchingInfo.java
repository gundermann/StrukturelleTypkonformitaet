package de.fernuni.hagen.ma.gundermann.signaturematching;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

public class MethodMatchingInfo {

	private final Method source;

	private final Method target;

	private final SingleMatchingInfo returnTypeMatchingInfo;

	private final Map<ParamPosition, SingleMatchingInfo> argumentTypeMatchingInfos;

	public MethodMatchingInfo(Method source, Method target, SingleMatchingInfo returnTypeMatchingInfo,
			Map<ParamPosition, SingleMatchingInfo> argumentTypeMatchingInfos) {
		this.source = source;
		this.target = target;
		this.returnTypeMatchingInfo = returnTypeMatchingInfo;
		this.argumentTypeMatchingInfos = argumentTypeMatchingInfos;
	}

	public Method getSource() {
		return source;
	}

	public Method getTarget() {
		return target;
	}

	public SingleMatchingInfo getReturnTypeMatchingInfo() {
		return returnTypeMatchingInfo;
	}

	public Map<ParamPosition, SingleMatchingInfo> getArgumentTypeMatchingInfos() {
		return argumentTypeMatchingInfos;
	}

	@Override
	public String toString() {
		return String.format("%s -> %s", source.getName(), target.getName());
	}

	@Override
	public int hashCode() {
		int prime = 13;
		int hash = source.hashCode() * prime;
		hash += target.hashCode() * prime;
		hash += returnTypeMatchingInfo.hashCode() * prime;
		for (Entry<ParamPosition, SingleMatchingInfo> argMI : argumentTypeMatchingInfos.entrySet()) {
			hash += argMI.getKey().hashCode() * prime + argMI.getValue().hashCode() * prime;
		}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MethodMatchingInfo) {
			MethodMatchingInfo mmi2 = MethodMatchingInfo.class.cast(obj);
			if (!source.equals(mmi2.source)) {
				return false;
			}
			if (!target.equals(mmi2.target)) {
				return false;
			}
			if (!returnTypeMatchingInfo.equals(mmi2.returnTypeMatchingInfo)) {
				return false;
			}
			for (Entry<ParamPosition, SingleMatchingInfo> argMI : argumentTypeMatchingInfos.entrySet()) {
				if (!mmi2.argumentTypeMatchingInfos.containsKey(argMI.getKey())
						|| !mmi2.argumentTypeMatchingInfos.get(argMI.getKey()).equals(argMI.getValue())) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public static class ParamPosition {

		private final Integer sourceParamPosition;

		private final Integer targetParamPosition;

		public ParamPosition(Integer sourceParamPosition, Integer targetParamPosition) {
			this.sourceParamPosition = sourceParamPosition;
			this.targetParamPosition = targetParamPosition;
		}

		public Integer getTargetParamPosition() {
			return targetParamPosition;
		}

		public Integer getSourceParamPosition() {
			return sourceParamPosition;
		}

		@Override
		public int hashCode() {
			int prime = 47;
			int hash = prime * sourceParamPosition.hashCode();
			hash += prime * targetParamPosition.hashCode();
			return hash;
		}
	}

}
