package tester;

import java.lang.reflect.Method;

public class TestResult {

  private Result result;

  private int testCount = 0;

  private int passedTests = 0;

  private Throwable throwable;

  private String pivotTestName;

  private Method pivotMethodCalled;

  public TestResult( boolean testPassed ) {
    this.result = testPassed ? Result.PASSED : Result.FAILED;
  }

  public TestResult() {
  }

  public void canceled( Throwable e, Method pivotMethod ) {
    this.result = Result.CANCELED;
    this.throwable = e;
    this.pivotMethodCalled = pivotMethod;
  }

  public void failed( WrappedAssertionError ae ) {
    this.result = Result.FAILED;
    this.throwable = ae;
    this.pivotTestName = ae.getTestName();
  }

  public void passed() {
    this.result = Result.PASSED;
  }

  public void addTests( int additionalTestCount ) {
    this.testCount += additionalTestCount;
  }

  public void incrementPassedTests() {
    this.passedTests++;
  }

  public Result getResult() {
    return result;
  }

  public int getTestCount() {
    return testCount;
  }

  public int getPassedTests() {
    return passedTests;
  }

  public Throwable getException() {
    return throwable;
  }

  public String getPivotTestName() {
    return pivotTestName;
  }

  public Method getPivotMethodCall() {
    return pivotMethodCalled;
  }

  public static enum Result {
    CANCELED, PASSED, FAILED;
  }

}
