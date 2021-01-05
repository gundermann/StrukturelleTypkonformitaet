package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import tester.TestResult;
import tester.TestResult.Result;

class TestedComponent<S> {

  private S component;

  private TestResult testResult;

  TestedComponent( S component, TestResult testResult ) {
    this.component = component;
    this.testResult = testResult;
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

}
