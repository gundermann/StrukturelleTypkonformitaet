package tester;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

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
        invokeTests( testInstance, testResult );
      }
      catch ( InstantiationException | IllegalAccessException | IllegalArgumentException
          | InvocationTargetException e ) {
        e.printStackTrace();
        testResult.canceled();
        return testResult;
      }
      catch ( WrappedAssertionError ae ) {
        String assertionError = ae.getMessage();
        if ( assertionError.isEmpty() ) {
          assertionError = "assertion error";
        }
        System.out.println( String.format( "TEST FAILED: %s => %s", ae.getTestName(), ae.getMessage() ) );
        testResult.failed();
        return testResult;

      }
    }
    System.out.println( String.format( "TEST PASSED" ) );
    testResult.passed();
    return testResult;
  }

  private void invokeTests( Object testInstance, TestResult testResult )
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, WrappedAssertionError {
    Method[] testMethods = findTestMethods( testInstance.getClass() );
    testResult.addTests( testMethods.length );
    int counter = 1;
    for ( Method test : testMethods ) {
      test.setAccessible( true );
      try {
        test.invoke( testInstance );
      }
      catch ( InvocationTargetException e ) {
        Throwable targetException = e.getTargetException();
        if ( AssertionError.class.equals( targetException.getClass() ) ) {
          throw new WrappedAssertionError( AssertionError.class.cast( targetException ), test );
        }
        throw e;
      }
      testResult.incrementPassedTests();
      System.out.println( String.format( "Test passed: %d/%d", counter++, testMethods.length ) );
    }
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
