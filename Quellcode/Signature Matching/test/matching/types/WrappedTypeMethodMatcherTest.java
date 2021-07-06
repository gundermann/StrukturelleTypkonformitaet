package matching.types;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import matching.types.ExactTypeMatcher;
import matching.types.TypeMatchingInfo;
import matching.types.WrappedTypeMatcher;
import testcomponents.genspec.General;
import testcomponents.genspec.Specific;
import testcomponents.wrapped.Wrapped;
import testcomponents.wrapped.Wrapper;

public class WrappedTypeMethodMatcherTest {

  @Test
  public void matchesWrappedTypes() {
    WrappedTypeMatcher matcher = new WrappedTypeMatcher( () -> new ExactTypeMatcher() );
    assertTrue( matcher.matchesType( Wrapped.class, Wrapper.class ) );
    assertTrue( matcher.matchesType( Wrapper.class, Wrapped.class ) );
    assertTrue( matcher.matchesType( Integer.class, int.class ) );
    assertTrue( matcher.matchesType( long.class, Long.class ) );
  }

  @Test
  public void dismatchesNonWrappedTypes() {
    WrappedTypeMatcher matcher = new WrappedTypeMatcher( () -> new ExactTypeMatcher() );
    assertFalse( matcher.matchesType( String.class, String.class ) );
    assertFalse( matcher.matchesType( General.class, Specific.class ) );
  }

  @Test
  public void emptyTypeMatchingInfos() {
    WrappedTypeMatcher matcher = new WrappedTypeMatcher( () -> new ExactTypeMatcher() );
    Collection<TypeMatchingInfo> tmi = matcher.calculateTypeMatchingInfos( Wrapped.class, Wrapper.class );
    assertTrue( tmi.size() == 1 );
    tmi = matcher.calculateTypeMatchingInfos( Wrapper.class, Wrapped.class );
    assertTrue( tmi.size() == 1 );
    tmi = matcher.calculateTypeMatchingInfos( Specific.class, General.class );
    assertTrue( tmi.size() == 0 );
  }

}
