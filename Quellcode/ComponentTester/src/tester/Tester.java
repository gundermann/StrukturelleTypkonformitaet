package tester;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;

class Tester {

  boolean testComponent( Object component, Collection<Class<?>> testClasses ) {
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
        invokeTests( testInstance );
      }
      catch ( InstantiationException | IllegalAccessException | IllegalArgumentException e ) {
        e.printStackTrace();
        return false;
      }
      catch ( InvocationTargetException ite ) {
        Throwable targetException = ite.getTargetException();
        if ( targetException.getClass().equals( AssertionError.class ) ) {
          System.out.println( String.format( "TEST FAILED: %s", targetException.getMessage() ) );
          return false;
        }
        ite.printStackTrace();
        return false;
      }
    }
    System.out.println( String.format( "TEST PASSED" ) );
    return true;
  }

  private void invokeTests( Object testInstance )
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, AssertionError {
    Method[] testMethods = findTestMethods( testInstance.getClass() );
    int counter = 1;
    for ( Method test : testMethods ) {
      test.setAccessible( true );
      test.invoke( testInstance );
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
