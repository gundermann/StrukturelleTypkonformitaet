package tester;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;

import api.RequiredTypeTest;
import glue.SigMaGlueException;
import spi.TriedMethodCallsInfo;

public class TestEvaluator {

	public TestResult test(Object testInstance) {
		TestResult testResult = new TestResult();
		try {
			// setup before @BeforeClass

			// test
			invokeTests(testInstance, testResult);

			// tear down after @AfterClass

			return testResult;
		} catch (IllegalAccessException | IllegalArgumentException e) {
			// e.printStackTrace();
			testResult.canceledByException(e);
			return testResult;
		}

	}

	private void handleError(InvocationTargetException e, Object testInstance, TestResult testResult) {
		// e.printStackTrace();
		Optional<SigMaGlueException> optSigMaGlueExc = findCausedSigMaGlueExcetion(e);
		if (optSigMaGlueExc.isPresent()) {
			SigMaGlueException sigMaGlueException = optSigMaGlueExc.get();
			Method failedMethod = sigMaGlueException.getCalledSourceMethod();
			testResult.canceledByFailedDelegation(sigMaGlueException, failedMethod);
		} else {
			testResult.canceledByException(e);
		}

	}

	private Optional<SigMaGlueException> findCausedSigMaGlueExcetion(Throwable e) {
		if (SigMaGlueException.class.isInstance(e)) {
			return Optional.of(SigMaGlueException.class.cast(e));
		}
		if (e.getCause() == null) {
			return Optional.empty();
		}
		return findCausedSigMaGlueExcetion(e.getCause());
	}

	private void invokeTests(Object testInstance, TestResult testResult)
			throws IllegalAccessException, IllegalArgumentException {
		Method[] testMethods = findTestMethods(testInstance.getClass());
		testResult.addTests(testMethods.length);
		Optional<Method> optBefore = findBeforeMethod(testInstance.getClass());
		Optional<Method> optAfter = findAfterMethod(testInstance.getClass());
		for (Method test : testMethods) {
			test.setAccessible(true);
			try {
				// setup
				if (optBefore.isPresent()) {
					optBefore.get().invoke(testInstance);
				}
				// test
				test.invoke(testInstance);

				// tear down
				if (optAfter.isPresent()) {
					optAfter.get().invoke(testInstance);
				}
			} catch (InvocationTargetException e) {
				Throwable targetException = e.getTargetException();
				collectCalledMethods(testInstance, testResult);
				if (AssertionError.class.equals(targetException.getClass())) {
					WrappedAssertionError ae = new WrappedAssertionError(AssertionError.class.cast(targetException),
							test);
					testResult.failed(ae);
				} else {
					handleError(e, testInstance, testResult);
				}
				return;
			}
			testResult.incrementPassedTests();
			collectCalledMethods(testInstance, testResult);
			// System.out.println( String.format( "test passed: %d/%d", counter++,
			// testMethods.length ) );
		}
	}

	private void collectCalledMethods(Object testInstance, TestResult testResult) {
		if (testInstance instanceof TriedMethodCallsInfo) {
			Collection<Method> calledMethods = TriedMethodCallsInfo.class.cast(testInstance).getTriedMethodCalls();
			calledMethods.forEach(testResult::addTriedMethodCall);
		}
	}

	private Optional<Method> findAfterMethod(Class<?> testClass) {
		Method[] declaredMethods = testClass.getDeclaredMethods();
		for (Method method : declaredMethods) {
			if (method.getAnnotation(After.class) != null) {
				return Optional.of(method);
			}
		}
		return Optional.empty();
	}

	private Optional<Method> findBeforeMethod(Class<?> testClass) {
		Method[] declaredMethods = testClass.getMethods();
		for (Method method : declaredMethods) {
			if (method.getAnnotation(Before.class) != null) {
				return Optional.of(method);
			}
		}
		return Optional.empty();
	}

	private Method[] findTestMethods(Class<?> testClass) {
		Method[] declaredMethods = testClass.getMethods();
		Collection<Method> testMethods = new ArrayList<>();
		for (Method method : declaredMethods) {
			if (method.getAnnotation(RequiredTypeTest.class) != null) {
				testMethods.add(method);
			}
		}
		return testMethods.toArray(new Method[] {});
	}

}
