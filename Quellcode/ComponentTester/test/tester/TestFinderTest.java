package tester;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.junit.Test;

import tester.querytypes.Interface1;
import tester.querytypes.Interface2;

public class TestFinderTest {

  @Test
  public void findTests_interface2() {
    TestFinder<Interface2> testFinder = new TestFinder<>( Interface2.class );
    Collection<Class<?>> testClassesOfQueryType = testFinder.findTestClassesOfQueryType();
    assertThat( testClassesOfQueryType.size(), equalTo( 1 ) );
  }

  @Test
  public void findTests_interface1() {
    TestFinder<Interface1> testFinder = new TestFinder<>( Interface1.class );
    Collection<Class<?>> testClassesOfQueryType = testFinder.findTestClassesOfQueryType();
    assertThat( testClassesOfQueryType.size(), equalTo( 2 ) );
  }

}
