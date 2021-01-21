package tester;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;

class Tester {

  TestResult testComponent( Object component, Collection<Class<?>> testClasses ) {
    TestResult testResult = new TestResult( TestType.NONE );
    for ( Class<?> testClass : testClasses ) {
      try {
        Object testInstance = setupTestObject( testClass, component );
        if ( testInstance == null ) {
          continue;
        }
        if ( containsAnySingleMethodTests( testClass ) ) {
          testResult = new SingleMethodTestEvaluator().test( testInstance );
        }
        else {
          testResult = new CommonTestEvaluator().test( testInstance );
        }

      }
      catch ( InstantiationException | IllegalAccessException | IllegalArgumentException
          | InvocationTargetException e ) {
        // e.printStackTrace();
        testResult.canceled( e );
        return testResult;
      }

    }
    if ( testResult.getResult() == null ) {
      System.out.println( String.format( "TESTS PASSED" ) );
      testResult.passed();
    }
    return testResult;
  }

  private boolean containsAnySingleMethodTests( Class<?> testClass ) {
    Method[] declaredMethods = testClass.getDeclaredMethods();
    for ( Method method : declaredMethods ) {
      QueryTypeTest queryTypeTestAnnotation = method.getAnnotation( QueryTypeTest.class );
      if ( queryTypeTestAnnotation != null ) {
        if ( queryTypeTestAnnotation.testedSingleMethod() != "" ) {
          return true;
        }
      }
    }
    return false;
  }

  private Object setupTestObject( Class<?> testClass, Object component )
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Object testInstance = testClass.newInstance();
    Method setter = findQueryTypeSetter( testClass, component.getClass() );
    if ( setter == null ) {
      System.out.println( "setter not found in test class: " + testClass.getName() );
      return null;
    }
    setter.setAccessible( true );
    setter.invoke( testInstance, component );
    return testInstance;
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
