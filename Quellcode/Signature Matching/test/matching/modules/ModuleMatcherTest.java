package matching.modules;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import matching.methods.CombinedMethodMatcher;
import matching.modules.testmodules.Class1;
import matching.modules.testmodules.Class2;
import matching.modules.testmodules.Enum2;
import matching.modules.testmodules.EnumNative;
import matching.modules.testmodules.Interface1;
import matching.modules.testmodules.InterfaceWrapper;

public class ModuleMatcherTest {

  @Test
  public void interface2interface_full_match() {
    ModuleMatcher<InterfaceWrapper> matcher = ModuleMatcherTestSupport.createModuleMatcher( InterfaceWrapper.class,
        new CombinedMethodMatcher() );
    assertTrue( matcher.matches( Interface1.class ) );
    assertTrue( matcher.partlyMatches( Interface1.class ) );
  }

  @Test
  public void enum2interface_full_match() {
    ModuleMatcher<InterfaceWrapper> matcher = ModuleMatcherTestSupport.createModuleMatcher( InterfaceWrapper.class,
        new CombinedMethodMatcher() );
    assertTrue( matcher.matches( EnumNative.class ) );
    assertTrue( matcher.partlyMatches( EnumNative.class ) );
  }

  @Test
  public void interface2interface_partly_match() {
    ModuleMatcher<Interface1> matcher1 = ModuleMatcherTestSupport.createModuleMatcher( Interface1.class,
        new CombinedMethodMatcher() );
    assertTrue( matcher1.partlyMatches( InterfaceWrapper.class ) );

    ModuleMatcher<InterfaceWrapper> matcher2 = ModuleMatcherTestSupport.createModuleMatcher( InterfaceWrapper.class,
        new CombinedMethodMatcher() );
    assertTrue( matcher2.partlyMatches( Interface1.class ) );
  }

  @Test
  public void enum2interface_partly_match() {
    ModuleMatcher<Interface1> matcher1 = ModuleMatcherTestSupport.createModuleMatcher( Interface1.class,
        new CombinedMethodMatcher() );
    assertTrue( matcher1.partlyMatches( EnumNative.class ) );
    assertTrue( matcher1.partlyMatches( Enum2.class ) );

    ModuleMatcher<InterfaceWrapper> matcher2 = ModuleMatcherTestSupport.createModuleMatcher( InterfaceWrapper.class,
        new CombinedMethodMatcher() );
    assertTrue( matcher2.partlyMatches( EnumNative.class ) );
    assertTrue( matcher2.partlyMatches( Enum2.class ) );
  }

  @Test
  public void class2interface_partly_match() {
    ModuleMatcher<Interface1> matcher1 = ModuleMatcherTestSupport.createModuleMatcher( Interface1.class,
        new CombinedMethodMatcher() );
    assertTrue( matcher1.partlyMatches( Class1.class ) );
    assertTrue( matcher1.partlyMatches( Class2.class ) );

    ModuleMatcher<InterfaceWrapper> matcher2 = ModuleMatcherTestSupport.createModuleMatcher( InterfaceWrapper.class,
        new CombinedMethodMatcher() );
    assertTrue( matcher2.partlyMatches( Class1.class ) );
    assertTrue( matcher2.partlyMatches( Class2.class ) );
  }

}
