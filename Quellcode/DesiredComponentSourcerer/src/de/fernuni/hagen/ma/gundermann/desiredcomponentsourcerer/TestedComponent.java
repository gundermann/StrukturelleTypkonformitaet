package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import matching.methods.MethodMatchingInfo;
import tester.TestResult;
import tester.TestResult.Result;

class TestedComponent<S> {

  private S component;

  private TestResult testResult;

  private MethodMatchingInfo pivotMatchingInfo;

  TestedComponent( S component, TestResult testResult ) {
    this.component = component;
    this.testResult = testResult;
  }

  TestedComponent( S component, TestResult testResult, MethodMatchingInfo pivotMatchingInfo ) {
    this.component = component;
    this.testResult = testResult;
    this.pivotMatchingInfo = pivotMatchingInfo;
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

  boolean isPivotMatchingInfoFound() {
    return pivotMatchingInfo != null;
  }

  MethodMatchingInfo getPivotMatchingInfo() {
    return pivotMatchingInfo;
  }

}
