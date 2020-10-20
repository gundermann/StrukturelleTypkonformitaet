package tester;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import tester.annotation.QueryTypeInstanceSetter;
import tester.finder.TestFinder;

public class Tester<T> {

  public void testComponent( T component ) {
    Collection<Class<?>> testClasses = findTestClasses( component );
    for ( Class<?> testClass : testClasses ) {
      try {
        Object testInstance = testClass.newInstance();
        Method setter = findQueryTypeSetter( testClass );
        setter.invoke( testInstance, component );
        invokeTests( testInstance );
      }
      catch ( InstantiationException | IllegalAccessException | IllegalArgumentException
          | InvocationTargetException e ) {
        throw new RuntimeException( e );
      }
    }
  }

  private void invokeTests( Object testInstance )
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Method[] testMethods = findTestMethods( testInstance.getClass() );
    for ( Method test : testMethods ) {
      test.invoke( testInstance );
    }
  }

  private Method[] findTestMethods( Class<?> testClass ) {
    Method[] declaredMethods = testClass.getDeclaredMethods();
    Collection<Method> testMethods = new ArrayList<>();
    for ( Method method : declaredMethods ) {
      if ( method.getAnnotation( Test.class ) != null ) {
        testMethods.add( method );
      }
    }
    return testMethods.toArray( new Method[] {} );
  }

  private Method findQueryTypeSetter( Class<?> testClass ) {
    Method[] declaredMethods = testClass.getDeclaredMethods();
    for ( Method method : declaredMethods ) {
      if ( method.getAnnotation( QueryTypeInstanceSetter.class ) != null ) {
        return method;
      }
    }
    return null;
  }

  private Collection<Class<?>> findTestClasses( T component ) {
    return new TestFinder<T>().findTestClassesOfComponent( component );
  }
}
