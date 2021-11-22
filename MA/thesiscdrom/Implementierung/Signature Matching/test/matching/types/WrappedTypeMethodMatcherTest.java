package matching.types;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.Collection;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.signaturematching.SingleMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.ExactTypeMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.ContainerTypeMatcher;
import testcomponents.genspec.General;
import testcomponents.genspec.Specific;
import testcomponents.wrapped.Wrapped;
import testcomponents.wrapped.Wrapper;

public class WrappedTypeMethodMatcherTest {

  @Test
  public void matchesWrappedTypes() {
    ContainerTypeMatcher matcher = new ContainerTypeMatcher( () -> new ExactTypeMatcher() );
    assertTrue( matcher.matchesType( Wrapped.class, Wrapper.class ) );
    assertTrue( matcher.matchesType( Wrapper.class, Wrapped.class ) );
    assertTrue( matcher.matchesType( Integer.class, int.class ) );
    assertTrue( matcher.matchesType( long.class, Long.class ) );
  }

  @Test
  public void dismatchesNonWrappedTypes() {
    ContainerTypeMatcher matcher = new ContainerTypeMatcher( () -> new ExactTypeMatcher() );
    assertFalse( matcher.matchesType( String.class, String.class ) );
    assertFalse( matcher.matchesType( General.class, Specific.class ) );
  }

  @Test
  public void matchingInfos() {
    ContainerTypeMatcher matcher = new ContainerTypeMatcher( () -> new ExactTypeMatcher() );
    Collection<SingleMatchingInfo> mis = matcher.calculateTypeMatchingInfos( Wrapped.class, Wrapper.class );
    assertTrue( mis.size() == 1 );
    SingleMatchingInfo mi = mis.iterator().next();
    assertThat(mi.getSource(), equalTo(Wrapper.class));
    assertThat(mi.getTarget(), equalTo(Wrapped.class));
    mis = matcher.calculateTypeMatchingInfos( Wrapper.class, Wrapped.class );
    assertTrue( mis.size() == 1 );
    mi = mis.iterator().next();
    assertThat(mi.getSource(), equalTo(Wrapped.class));
    assertThat(mi.getTarget(), equalTo(Wrapper.class));
    mis = matcher.calculateTypeMatchingInfos( Specific.class, General.class );
    assertTrue( mis.size() == 0 );
  }

}
