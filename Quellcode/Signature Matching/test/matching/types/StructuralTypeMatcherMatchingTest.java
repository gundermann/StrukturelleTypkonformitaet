package matching.types;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import matching.MatcherCombiner;
import matching.types.ExactTypeMatcher;
import matching.types.GenSpecTypeMatcher;
import matching.types.PartlyTypeMatcher;
import matching.types.StructuralTypeMatcher;
import matching.types.WrappedTypeMatcher;
import matching.types.testtypes.Class1;
import matching.types.testtypes.Class2;
import matching.types.testtypes.Enum2;
import matching.types.testtypes.EnumNative;
import matching.types.testtypes.Interface1;
import matching.types.testtypes.InterfaceWrapper;

public class StructuralTypeMatcherMatchingTest {

  ExactTypeMatcher exactTypeMatcher = new ExactTypeMatcher();

  GenSpecTypeMatcher genSpecTypeMatcher = new GenSpecTypeMatcher();

  PartlyTypeMatcher matcher = new StructuralTypeMatcher( MatcherCombiner.combine( genSpecTypeMatcher, exactTypeMatcher,
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
