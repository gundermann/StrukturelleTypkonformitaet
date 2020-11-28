package matching.modules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

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
    Collection<ModuleMatchingInfo> tmi = matcher.calculateTypeMatchingInfos( String.class, String.class );
    assertTrue( tmi.size() == 1 );
    tmi = matcher.calculateTypeMatchingInfos( General.class, Specific.class );
    assertTrue( tmi.size() == 1 );
    tmi = matcher.calculateTypeMatchingInfos( Specific.class, General.class );
    assertTrue( tmi.size() == 1 );
  }

}
