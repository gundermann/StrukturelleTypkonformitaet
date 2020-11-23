package tester;

import java.util.Collection;

public class ComponentTester<S> {

  private Class<S> queryType;

  public ComponentTester( Class<S> queryType ) {
    this.queryType = queryType;
  }

  public <T> TestResult testComponent( T component ) {
    System.out.println( String.format( "test component: %s", component.getClass().getName() ) );
    TestFinder<S> testFinder = new TestFinder<>( queryType );
    Collection<Class<?>> testClasses = testFinder.findTestClassesOfQueryType();
    if ( testClasses.isEmpty() ) {
      System.out.println( "no test classes defined" );
      return new TestResult( false );
    }
    return new Tester().testComponent( component, testClasses );
  }
}
