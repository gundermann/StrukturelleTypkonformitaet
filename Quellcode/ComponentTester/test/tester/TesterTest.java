package tester;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import tester.TestResult.Result;
import tester.components.Interface1ConfirmTestsImpl;
import tester.components.Interface1FailTestsImpl;
import tester.components.Interface2ConfirmTestsImpl;
import tester.components.Interface2FailTestsImpl;
import tester.querytypes.Interface1;
import tester.querytypes.Interface2;
import tester.querytypes.tests.Interface1Test1;
import tester.querytypes.tests.Interface1Test2;
import tester.querytypes.tests.Interface2Test;

public class TesterTest {

  @Test
  public void interface2_pass() {
    Interface2 component = new Interface2ConfirmTestsImpl();
    Collection<Class<?>> testClasses = Arrays.asList( Interface2Test.class );
    TestResult result = new Tester().testComponent( component, testClasses );
    assertTrue( result.getResult() == Result.PASSED );
  }

  @Test
  public void interface2_fail() {
    Interface2 component = new Interface2FailTestsImpl();
    Collection<Class<?>> testClasses = Arrays.asList( Interface2Test.class );
    TestResult result = new Tester().testComponent( component, testClasses );
    assertTrue( result.getResult() == Result.FAILED );
  }

  @Test
  public void interface1_pass() {
    Interface1 component = new Interface1ConfirmTestsImpl();
    Collection<Class<?>> testClasses = Arrays.asList( Interface1Test1.class, Interface1Test2.class );
    TestResult result = new Tester().testComponent( component, testClasses );
    assertTrue( result.getResult() == Result.PASSED );
  }

  @Test
  public void interface1_fail() {
    Interface1 component = new Interface1FailTestsImpl();
    Collection<Class<?>> testClasses = Arrays.asList( Interface1Test1.class, Interface1Test2.class );
    TestResult result = new Tester().testComponent( component, testClasses );
    assertTrue( result.getResult() == Result.FAILED );
  }

  @Test
  public void componentTypeNotMatchSetterType() {
    Interface1 component = new Interface1FailTestsImpl();
    Collection<Class<?>> testClasses = Arrays.asList( Interface2Test.class );
    TestResult result = new Tester().testComponent( component, testClasses );
    assertTrue( result.getResult() == Result.CANCELED );
  }

}
