package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.ArrayList;
import java.util.Collection;

import matching.methods.MethodMatchingInfo;
import tester.TestResult;
import tester.TestResult.Result;

class TestedComponent<S> {

  private S component;

  private TestResult testResult;

  private Collection<MethodMatchingInfo> outsortedMatchingInfos = new ArrayList<>();

  TestedComponent( S component, TestResult testResult ) {
    this.component = component;
    this.testResult = testResult;
  }

  void addOutsortedMatchingInfo( MethodMatchingInfo outsortedMatchingInfo ) {
    outsortedMatchingInfos.add( outsortedMatchingInfo );
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

  boolean isOutsortedMatchingInfoFound() {
    return !outsortedMatchingInfos.isEmpty();
  }

  Collection<MethodMatchingInfo> getOutsortedMatchingInfos() {
    return outsortedMatchingInfos;
  }

}
