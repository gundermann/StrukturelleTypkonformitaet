package tester;

import java.util.ArrayList;
import java.util.Collection;

import tester.annotation.QueryTypeTestReference;

class TestFinder<T> {

  private Class<T> qt;

  TestFinder( Class<T> queryType ) {
    this.qt = queryType;
  }

  Collection<Class<?>> findTestClassesOfQueryType() {
    Collection<Class<?>> testClasses = new ArrayList<>();
    // Die Annotation ist nur eine Übergangslösung.
    // Es funktioniert auch ohne diese Annotation, wenn der ClassLoader des queryTypes verwendet wird, um dessen Projekt
    // nach den Testklassen zu durchsuchen.
    // Vorteile:
    // - eine Annotation weniger
    // Nachteile:
    // - stärkere Bedingungen an die Ablage der Testklassen (mit der Annotation können die Testklassen sonstwo liegen.)
    // - die Angabe weiterer Informationen ist dadurch nicht gewaehrleistet
    if ( qt.isAnnotationPresent( QueryTypeTestReference.class ) ) {
      QueryTypeTestReference queryTypeTestReference = qt.getAnnotation( QueryTypeTestReference.class );
      for ( Class<?> testClass : queryTypeTestReference.testClasses() ) {
        testClasses.add( testClass );
      }
    }
    return testClasses;
  }

}
