package matching.types;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.signaturematching.SingleMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.GenSpecTypeMatcher;
import testcomponents.genspec.General;
import testcomponents.genspec.OfferedGenClass;
import testcomponents.genspec.Specific;

public class GenSpecTypeMatcherTest {

  @Test
  public void matchesGenSpecTypes() {
    GenSpecTypeMatcher matcher = new GenSpecTypeMatcher();
    assertTrue( matcher.matchesType( String.class, String.class ) );
    assertTrue( matcher.matchesType( String.class, Object.class ) );
    assertTrue( matcher.matchesType( General.class, Specific.class ) );
    assertTrue( matcher.matchesType( Specific.class, General.class ) );
  }

  @Test
  public void dismatchesNonGenSpecTypes() {
    GenSpecTypeMatcher matcher = new GenSpecTypeMatcher();
    assertFalse( matcher.matchesType( String.class, int.class ) );
    assertFalse( matcher.matchesType( OfferedGenClass.class, Specific.class ) );
  }

  @Test
  public void typeMatchingInfos() {
    GenSpecTypeMatcher matcher = new GenSpecTypeMatcher();
    Collection<SingleMatchingInfo> mis = matcher.calculateTypeMatchingInfos( String.class, String.class );
    assertTrue( mis.size() == 1 );
    SingleMatchingInfo mi = mis.iterator().next();
    assertThat( mi.getMethodMatchingInfos().size(), equalTo( 0 ) );
    mis = matcher.calculateTypeMatchingInfos( General.class, Specific.class );
    assertTrue( mis.size() == 1 );
    mi = mis.iterator().next();
    assertThat( mi.getMethodMatchingInfos().size(), equalTo( 0 ) );
    mis = matcher.calculateTypeMatchingInfos( Specific.class, General.class );
    assertThat( mis.size(), equalTo( 1 ) );
    mi = mis.iterator().next();
    assertThat( mi.getMethodMatchingInfos().size(), equalTo( 3 ) );
  }

}
