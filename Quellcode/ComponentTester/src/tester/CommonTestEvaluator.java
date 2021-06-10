package tester;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;

import glue.SigMaGlueException;
import tester.annotation.QueryTypeTest;

public class CommonTestEvaluator {

  public TestResult test( Object testInstance ) {
    TestResult testResult = new TestResult( TestType.COMMON );
    try {
      // setup

      // test
      invokeTests( testInstance, testResult );

      // tear down
      return testResult;
    }
    catch ( WrappedAssertionError ae ) {
      String assertionError = ae.getMessage();
      if ( assertionError.isEmpty() ) {
        assertionError = "assertion error";
      }
      System.out.println( String.format( "TEST FAILED: %s => %s", ae.getTestName(), ae.getMessage() ) );
      testResult.failed( ae );
      return testResult;
    }
    catch ( InvocationTargetException e ) {
      // e.printStackTrace();
      Optional<SigMaGlueException> optSigMaGlueExc = findCausedSigMaGlueExcetion( e );
      if ( optSigMaGlueExc.isPresent() ) {
        Method calledPivotMethod = optSigMaGlueExc.get().getCalledSourceMethod();
        System.out.println( String.format( "called pivot method found: %s", calledPivotMethod.getName() ) );
        testResult.addPivotMethodCalled( calledPivotMethod );
        testResult.canceled( optSigMaGlueExc.get() );
      }
      else {
        testResult.canceled( e );
      }
      return testResult;
    }
    catch ( IllegalAccessException | IllegalArgumentException e ) {
      e.printStackTrace();
      testResult.canceled( e );
      return testResult;
    }

  }

  private Optional<SigMaGlueException> findCausedSigMaGlueExcetion( Throwable e ) {
    if ( SigMaGlueException.class.isInstance( e ) ) {
      return Optional.of( SigMaGlueException.class.cast( e ) );
    }
    if ( e.getCause() == null ) {
      return Optional.empty();
    }
    return findCausedSigMaGlueExcetion( e.getCause() );
  }

  private void invokeTests( Object testInstance, TestResult testResult )
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, WrappedAssertionError {
    Method[] testMethods = findTestMethods( testInstance.getClass() );
    testResult.addTests( testMethods.length );
    Optional<Method> optBefore = findBeforeMethod( testInstance.getClass() );
    Optional<Method> optAfter = findAfterMethod( testInstance.getClass() );
    int counter = 1;
    for ( Method test : testMethods ) {
      test.setAccessible( true );
      try {
        // setup
        if ( optBefore.isPresent() ) {
          optBefore.get().invoke( testInstance );
        }
        // test
        test.invoke( testInstance );

        // tear down
        if ( optAfter.isPresent() ) {
          optAfter.get().invoke( testInstance );
        }
      }
      catch ( InvocationTargetException e ) {
        Throwable targetException = e.getTargetException();
        if ( AssertionError.class.equals( targetException.getClass() ) ) {
          throw new WrappedAssertionError( AssertionError.class.cast( targetException ), test );
        }
        throw e;
      }
      testResult.incrementPassedTests();
      System.out.println( String.format( "test passed: %d/%d", counter++, testMethods.length ) );
    }
  }

  private Optional<Method> findAfterMethod( Class<?> testClass ) {
    Method[] declaredMethods = testClass.getDeclaredMethods();
    for ( Method method : declaredMethods ) {
      if ( method.getAnnotation( After.class ) != null ) {
        return Optional.of( method );
      }
    }
    return Optional.empty();
  }

  private Optional<Method> findBeforeMethod( Class<?> testClass ) {
    Method[] declaredMethods = testClass.getDeclaredMethods();
    for ( Method method : declaredMethods ) {
      if ( method.getAnnotation( Before.class ) != null ) {
        return Optional.of( method );
      }
    }
    return Optional.empty();
  }

  private Method[] findTestMethods( Class<?> testClass ) {
    Method[] declaredMethods = testClass.getDeclaredMethods();
    Collection<Method> testMethods = new ArrayList<>();
    for ( Method method : declaredMethods ) {
      if ( method.getAnnotation( QueryTypeTest.class ) != null ) {
        testMethods.add( method );
      }
    }
    return testMethods.toArray( new Method[] {} );
  }

}
