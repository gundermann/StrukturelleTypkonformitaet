package tester;

public class TestResult {

  private Result result;

  private int testCount = 0;

  private int passedTests = 0;

  public TestResult( boolean testPassed ) {
    this.result = testPassed ? Result.PASSED : Result.FAILED;
  }

  public TestResult() {
  }

  public void canceled() {
    this.result = Result.CANCELED;
  }

  public void failed() {
    this.result = Result.FAILED;
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

  public static enum Result {
    CANCELED, PASSED, FAILED;
  }
}
