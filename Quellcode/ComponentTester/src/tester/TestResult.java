package tester;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TestResult {

	private Result result;

	private int testCount = 0;

	private int passedTests = 0;

	private Cause cause;

	private Throwable throwable;

	private Set<Method> triedMethodCalls = new HashSet<>();

	private Method failedMethodCall;

	public TestResult(boolean testPassed) {
		this.result = testPassed ? Result.PASSED : Result.FAILED;
	}

	public TestResult() {
	}

	public void canceledByFailedDelegation(Throwable e, Method failedMethodCall) {
		this.failedMethodCall = failedMethodCall;
		this.result = Result.CANCELED;
		this.cause = Cause.FAILED_DELEGATION;
		this.throwable = e;
	}

	public void canceledByException(Throwable e) {
		this.result = Result.CANCELED;
		this.cause = Cause.EXCEPTION;
		this.throwable = e;
	}

	public void failed(WrappedAssertionError ae) {
		this.result = Result.FAILED;
		this.cause = Cause.ASSERTION;
		this.throwable = ae;
	}

	public void passed() {
		this.result = Result.PASSED;
	}

	void enhanceResult(TestResult tempResult) {
		if (tempResult != null) {
			this.result = tempResult.getResult() != null ? tempResult.getResult() : this.result;
			addTests(tempResult.getTestCount());
			this.passedTests = this.passedTests + tempResult.getPassedTests();
			this.triedMethodCalls.addAll(tempResult.getTriedMethodCalls());
			this.throwable = tempResult.getException();
			this.failedMethodCall = tempResult.getFailedMethodCall();
			this.cause = tempResult.cause;
		}
	}

	public void addTests(int additionalTestCount) {
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

	public Method getFailedMethodCall() {
		return failedMethodCall;
	}

	public Cause getCause() {
		return cause;
	}

	public void setFailedMethodCall(Method failedMethodCall) {
		this.failedMethodCall = failedMethodCall;
	}

	public Collection<Method> getTriedMethodCalls() {
		return triedMethodCalls;
	}

	public void addTriedMethodCall(Method m) {
		triedMethodCalls.add(m);
	}

	public static enum Result {
		CANCELED, PASSED, FAILED;
	}

	public static enum Cause {
		ASSERTION, FAILED_DELEGATION, EXCEPTION;
	}

}
