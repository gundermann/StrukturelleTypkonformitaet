package matching.modules;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import matching.modules.testmodules.Class1;
import matching.modules.testmodules.Class2;
import matching.modules.testmodules.Enum1;
import matching.modules.testmodules.Enum2;
import matching.modules.testmodules.Interface1;
import matching.modules.testmodules.Interface2;

public class ModuleMatcherTest {

  ModuleMatcher matcher = new ModuleMatcher();

  @Test
  public void interface2interface_full_match() {
    assertTrue( matcher.matches( Interface1.class, Interface2.class ) );
    assertTrue( matcher.partlyMatches( Interface1.class, Interface2.class ) );
  }

  @Test
  public void enum2interface_full_match() {
    assertTrue( matcher.matches( Enum1.class, Interface2.class ) );
    assertTrue( matcher.partlyMatches( Enum1.class, Interface2.class ) );
  }

  @Test
  public void interface2interface_partly_match() {
    assertTrue( matcher.partlyMatches( Interface2.class, Interface1.class ) );
    assertTrue( matcher.partlyMatches( Interface1.class, Interface2.class ) );
  }

  @Test
  public void enum2interface_partly_match() {
    assertTrue( matcher.partlyMatches( Enum1.class, Interface1.class ) );
    assertTrue( matcher.partlyMatches( Enum2.class, Interface1.class ) );
    assertTrue( matcher.partlyMatches( Enum1.class, Interface2.class ) );
    assertTrue( matcher.partlyMatches( Enum2.class, Interface2.class ) );
  }

  @Test
  public void class2interface_partly_match() {
    assertTrue( matcher.partlyMatches( Class1.class, Interface1.class ) );
    assertTrue( matcher.partlyMatches( Class2.class, Interface1.class ) );
    assertTrue( matcher.partlyMatches( Class1.class, Interface2.class ) );
    assertTrue( matcher.partlyMatches( Class2.class, Interface2.class ) );
  }

}
