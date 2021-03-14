package de.fernuni.hagen.ma.gundermann.matcherexamples.spec;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SuperClass;
import matching.modules.GenSpecTypeMatcher;

/**
 * Matcher Test fuer die Ubereinstimmung der Form:<br>
 * <b>checkType < queryType</b>
 */
public class GenSpecTypeMatcher_Spec_MatcherTest {

  @Test
  public void match() {
    GenSpecTypeMatcher matcher = new GenSpecTypeMatcher();
    assertTrue( matcher.matchesType( String.class, Object.class ) );
    assertTrue( matcher.matchesType( SubClass.class, SuperClass.class ) );
    assertTrue( matcher.matchesType( Integer.class, Number.class ) );
  }

  @Test
  public void noMatch() {
    GenSpecTypeMatcher matcher = new GenSpecTypeMatcher();
    assertFalse( matcher.matchesType( int.class, String.class ) );
  }

}
