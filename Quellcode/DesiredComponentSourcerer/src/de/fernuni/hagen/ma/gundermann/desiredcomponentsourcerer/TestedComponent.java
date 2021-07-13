package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import tester.TestResult;
import tester.TestResult.Result;

class TestedComponent<S> {

	private S component;

	private TestResult testResult;

	@Deprecated
	private Collection<MethodMatchingInfo> outsortedMatchingInfos = new ArrayList<>();

	private Collection<Collection<MethodMatchingInfo>> blacklistedMMICombis = new ArrayList<Collection<MethodMatchingInfo>>();

	TestedComponent(S component, TestResult testResult) {
		this.component = component;
		this.testResult = testResult;
	}

	@Deprecated
	void addOutsortedMatchingInfo(MethodMatchingInfo outsortedMatchingInfo) {
		outsortedMatchingInfos.add(outsortedMatchingInfo);
	}

	void addBlacklistedMMICombi(List<MethodMatchingInfo> failedMMICombi) {
		blacklistedMMICombis.add(failedMMICombi);
	}

	S getComponent() {
		return component;
	}

	TestResult getTestResult() {
		return testResult;
	}

	boolean allTestsPassed() {
		return testResult.getResult() == Result.PASSED;
	}

	boolean anyTestPassed() {
		return testResult.getPassedTests() > 0;
	}

	@Deprecated
	boolean isOutsortedMatchingInfoFound() {
		return !outsortedMatchingInfos.isEmpty();
	}

	boolean foundBlacklistedMMICombi() {
	  return !blacklistedMMICombis.isEmpty();
  }

	@Deprecated
	Collection<MethodMatchingInfo> getOutsortedMatchingInfos() {
		return outsortedMatchingInfos;
	}

	Collection<Collection<MethodMatchingInfo>> getBlacklistedMMICombis() {
		return blacklistedMMICombis;
	}

}
