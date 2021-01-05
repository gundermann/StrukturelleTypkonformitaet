package tester;

import java.lang.reflect.Method;

class WrappedAssertionError extends Throwable {

  /**
   *
   */
  private static final long serialVersionUID = -2884957207053109323L;

  private final AssertionError error;

  private final Method testMethod;

  private final String message;

  WrappedAssertionError( AssertionError error, Method testMethod ) {
    this.error = error;
    this.testMethod = testMethod;
    String assertionError = error.getMessage();
    if ( assertionError.isEmpty() ) {
      assertionError = "assertion error";
    }
    this.message = assertionError;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  public String getTestName() {
    return testMethod.getName();
  }

  public AssertionError getError() {
    return error;
  }

}
