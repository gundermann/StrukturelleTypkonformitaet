package tester;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

public class TestResult {

  private Result result;

  private final TestType testType;

  private int testCount = 0;

  private int passedTests = 0;

  private Throwable throwable;

  private String pivotTestName;

  private Collection<Method> pivotMethodsCalled = new ArrayList<>();

  private Collection<String> failedSingleMethods = new ArrayList<>();

  public TestResult( boolean testPassed, TestType testType ) {
    this.result = testPassed ? Result.PASSED : Result.FAILED;
    this.testType = testType;
  }

  public TestResult( TestType testType ) {
    this.testType = testType;
  }

  public TestType getTestType() {
    return testType;
  }

  public void canceled( Throwable e ) {
    this.result = Result.CANCELED;
    this.throwable = e;
  }

  @Deprecated
  public void canceled( Throwable e, Method pivotMethod ) {
    this.result = Result.CANCELED;
    this.throwable = e;
    addPivotMethodCalled( pivotMethod );
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

  public void addPivotMethodCalled( Method pivotMethod ) {
    if ( pivotMethod != null ) {
      pivotMethodsCalled.add( pivotMethod );
    }
  }

  public Collection<Method> getPivotMethodCalls() {
    return pivotMethodsCalled;
  }

  public void addFailedSingleMethod( String singleMethod ) {
    failedSingleMethods.add( singleMethod );
  }

  public Collection<String> getFailedSingleMethods() {
    return failedSingleMethods;
  }

  public static enum Result {
    CANCELED, PASSED, FAILED;
  }

}
