package matching.types;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import matching.MatcherCombiner;
import matching.types.testtypes.Class1;
import matching.types.testtypes.Class2;
import matching.types.testtypes.Enum2;
import matching.types.testtypes.EnumNative;
import matching.types.testtypes.Interface1;
import matching.types.testtypes.InterfaceWrapper;

public class StructuralTypeMatcherMatchingTest {

  ExactTypeMatcher exactTypeMatcher = new ExactTypeMatcher();

  GenSpecTypeMatcher genSpecTypeMatcher = new GenSpecTypeMatcher();

  TypeMatcher matcher = new StructuralTypeMatcher( MatcherCombiner.combine( genSpecTypeMatcher, exactTypeMatcher,
      new WrappedTypeMatcher( MatcherCombiner.combine( genSpecTypeMatcher, exactTypeMatcher ) ) ) );

  @Test
  public void interface2interface_full_match() {
    assertTrue( matcher.matchesType( Interface1.class, InterfaceWrapper.class ) );
  }

  @Test
  public void enum2interface_full_match() {
    assertTrue( matcher.matchesType( EnumNative.class, InterfaceWrapper.class ) );
  }

  @Test
  public void interface2interface_partly_match() {
    assertTrue( matcher.matchesType( InterfaceWrapper.class, Interface1.class ) );
    assertTrue( matcher.matchesType( Interface1.class, InterfaceWrapper.class ) );
  }

  @Test
  public void enum2interface_partly_match() {
    assertTrue( matcher.matchesType( EnumNative.class, Interface1.class ) );
    assertTrue( matcher.matchesType( Enum2.class, Interface1.class ) );
    assertTrue( matcher.matchesType( EnumNative.class, InterfaceWrapper.class ) );
    assertTrue( matcher.matchesType( Enum2.class, InterfaceWrapper.class ) );
  }

  @Test
  public void class2interface_partly_match() {
    assertTrue( matcher.matchesType( Class1.class, Interface1.class ) );
    assertTrue( matcher.matchesType( Class2.class, Interface1.class ) );
    assertTrue( matcher.matchesType( Class1.class, InterfaceWrapper.class ) );
    assertTrue( matcher.matchesType( Class2.class, InterfaceWrapper.class ) );
  }
  
}
