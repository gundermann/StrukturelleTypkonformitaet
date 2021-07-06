package matching.types;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import matching.types.ExactTypeMatcher;
import matching.types.TypeMatchingInfo;

public class ExactTypeMatcherTest {

  @Test
  public void matchesExactTypes() {
    ExactTypeMatcher matcher = new ExactTypeMatcher();
    assertTrue( matcher.matchesType( String.class, String.class ) );
    assertTrue( matcher.matchesType( int.class, int.class ) );
    assertTrue( matcher.matchesType( Object.class, Object.class ) );
    assertTrue( matcher.matchesType( ExactTypeMatcherTest.class, ExactTypeMatcherTest.class ) );
  }

  @Test
  public void dismatchesNonExactTypes() {
    ExactTypeMatcher matcher = new ExactTypeMatcher();
    assertFalse( matcher.matchesType( String.class, int.class ) );
    assertFalse( matcher.matchesType( int.class, Object.class ) );
    assertFalse( matcher.matchesType( Object.class, String.class ) );
    assertFalse( matcher.matchesType( String.class, ExactTypeMatcherTest.class ) );
  }

  @Test
  public void typeMatchingInfos() {
    ExactTypeMatcher matcher = new ExactTypeMatcher();
    Collection<TypeMatchingInfo> tmi = matcher.calculateTypeMatchingInfos( String.class, String.class );
    assertTrue( tmi.size() == 1 );
    tmi = matcher.calculateTypeMatchingInfos( Object.class, String.class );
    assertTrue( tmi.isEmpty() );
    tmi = matcher.calculateTypeMatchingInfos( ExactTypeMatcherTest.class, ExactTypeMatcherTest.class );
    assertTrue( tmi.size() == 1 );
    tmi = matcher.calculateTypeMatchingInfos( String.class, int.class );
    assertTrue( tmi.isEmpty() );
  }

}
