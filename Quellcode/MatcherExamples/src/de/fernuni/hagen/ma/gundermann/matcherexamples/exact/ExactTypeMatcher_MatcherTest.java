package de.fernuni.hagen.ma.gundermann.matcherexamples.exact;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SuperClass;
import matching.modules.ExactTypeMatcher;

/**
 * Matcher Test fuer die Ubereinstimmung der Form:<br>
 * <b>queryType = checkType</b>
 */
public class ExactTypeMatcher_MatcherTest {

  @Test
  public void match() {
    ExactTypeMatcher matcher = new ExactTypeMatcher();
    assertTrue( matcher.matchesType( String.class, String.class ) );
    assertTrue( matcher.matchesType( int.class, int.class ) );
    assertTrue( matcher.matchesType( Object.class, Object.class ) );
    assertTrue( matcher.matchesType( SuperClass.class, SuperClass.class ) );
  }

  @Test
  public void noMatch() {
    ExactTypeMatcher matcher = new ExactTypeMatcher();
    assertFalse( matcher.matchesType( String.class, int.class ) );
    assertFalse( matcher.matchesType( int.class, Object.class ) );
    assertFalse( matcher.matchesType( Object.class, String.class ) );
    assertFalse( matcher.matchesType( SuperClass.class, SubClass.class ) );
  }

}
