package tester;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;

import glue.SigMaGlueException;
import spi.PivotMethodTestInfo;
import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;

class Tester {

  TestResult testComponent( Object component, Collection<Class<?>> testClasses ) {
    TestResult testResult = new TestResult();
    for ( Class<?> testClass : testClasses ) {
      try {
        Object testInstance = testClass.newInstance();
        Method setter = findQueryTypeSetter( testClass, component.getClass() );
        if ( setter == null ) {
          System.out.println( "setter not found in test class: " + testClass.getName() );
          continue;
        }
        setter.setAccessible( true );
        setter.invoke( testInstance, component );

        try {
          // setup

          // test
          invokeTests( testInstance, testResult );

          // tear down
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
          e.printStackTrace();
          Method calledPivotMethod = null;
          Optional<SigMaGlueException> optSigMaGlueExc = findCausedSigMaGlueExcetion( e );
          if ( optSigMaGlueExc.isPresent() && testInstance instanceof PivotMethodTestInfo
              && !PivotMethodTestInfo.class.cast( testInstance ).pivotMethodCallExecuted() ) {
            calledPivotMethod = optSigMaGlueExc.get().getCalledSourceMethod();
            System.out.println( String.format( "called pivot method found: %s", calledPivotMethod.getName() ) );
            testResult.canceled( optSigMaGlueExc.get(), calledPivotMethod );
          }
          else {
            testResult.canceled( e, calledPivotMethod );
          }
          return testResult;
        }
        catch ( IllegalAccessException | IllegalArgumentException e ) {
          e.printStackTrace();
          testResult.canceled( e, null );
          return testResult;
        }
      }
      catch ( InstantiationException | IllegalAccessException | IllegalArgumentException
          | InvocationTargetException e ) {
        e.printStackTrace();
        testResult.canceled( e, null );
        return testResult;
      }

    }
    System.out.println( String.format( "TESTS PASSED" ) );
    testResult.passed();
    return testResult;
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

  private Method findQueryTypeSetter( Class<?> testClass, Class<?> componentType ) {
    Method[] declaredMethods = testClass.getDeclaredMethods();
    for ( Method method : declaredMethods ) {
      if ( method.getAnnotation( QueryTypeInstanceSetter.class ) != null ) {
        if ( method.getParameterCount() == 1
            && method.getParameters()[0].getType().isAssignableFrom( componentType ) ) {
          return method;
        }
        throw new IllegalArgumentException(
            String.format( "query type setter %s:%s is not applicable with component type %s",
                testClass.getName(), method.getName(), componentType.getName() ) );
      }
    }
    return null;
  }

}
