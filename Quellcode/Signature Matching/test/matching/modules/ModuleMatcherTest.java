package matching.modules;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import matching.MatcherCombiner;
import matching.modules.testmodules.Class1;
import matching.modules.testmodules.Class2;
import matching.modules.testmodules.Enum2;
import matching.modules.testmodules.EnumNative;
import matching.modules.testmodules.Interface1;
import matching.modules.testmodules.InterfaceWrapper;

public class ModuleMatcherTest {

  ExactTypeMatcher exactTypeMatcher = new ExactTypeMatcher();

  GenSpecTypeMatcher genSpecTypeMatcher = new GenSpecTypeMatcher();

  TypeMatcher matcher = new StructuralTypeMatcher( MatcherCombiner.combine( genSpecTypeMatcher, exactTypeMatcher,
      new WrappedTypeMatcher( MatcherCombiner.combine( genSpecTypeMatcher, exactTypeMatcher ) ) ) );

  @Test
  public void interface2interface_full_match() {
    assertTrue( matcher.matchesType( Interface1.class, InterfaceWrapper.class ) );
    assertTrue( matcher.matchesTypePartly( Interface1.class, InterfaceWrapper.class ) );
  }

  @Test
  public void enum2interface_full_match() {
    assertTrue( matcher.matchesType( EnumNative.class, InterfaceWrapper.class ) );
    assertTrue( matcher.matchesTypePartly( EnumNative.class, InterfaceWrapper.class ) );
  }

  @Test
  public void interface2interface_partly_match() {
    assertTrue( matcher.matchesTypePartly( InterfaceWrapper.class, Interface1.class ) );
    assertTrue( matcher.matchesTypePartly( Interface1.class, InterfaceWrapper.class ) );
  }

  @Test
  public void enum2interface_partly_match() {
    assertTrue( matcher.matchesTypePartly( EnumNative.class, Interface1.class ) );
    assertTrue( matcher.matchesTypePartly( Enum2.class, Interface1.class ) );
    assertTrue( matcher.matchesTypePartly( EnumNative.class, InterfaceWrapper.class ) );
    assertTrue( matcher.matchesTypePartly( Enum2.class, InterfaceWrapper.class ) );
  }

  @Test
  public void class2interface_partly_match() {
    assertTrue( matcher.matchesTypePartly( Class1.class, Interface1.class ) );
    assertTrue( matcher.matchesTypePartly( Class2.class, Interface1.class ) );
    assertTrue( matcher.matchesTypePartly( Class1.class, InterfaceWrapper.class ) );
    assertTrue( matcher.matchesTypePartly( Class2.class, InterfaceWrapper.class ) );
  }

}
