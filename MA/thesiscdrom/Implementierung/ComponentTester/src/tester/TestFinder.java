package tester;

import java.util.ArrayList;
import java.util.Collection;

import api.RequiredTypeTestReference;

class TestFinder<T> {

  private Class<T> qt;

  TestFinder( Class<T> queryType ) {
    this.qt = queryType;
  }

  Collection<Class<?>> findTestClassesOfQueryType() {
    Collection<Class<?>> testClasses = new ArrayList<>();
    if ( qt.isAnnotationPresent( RequiredTypeTestReference.class ) ) {
      RequiredTypeTestReference queryTypeTestReference = qt.getAnnotation( RequiredTypeTestReference.class );
      for ( Class<?> testClass : queryTypeTestReference.testClasses() ) {
        testClasses.add( testClass );
      }
    }
    return testClasses;
  }

}
