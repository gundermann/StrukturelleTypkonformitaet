package de.fernuni.hagen.ma.gundermann.matcherexamples.gen;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SuperClass;
import matching.modules.GenSpecTypeMatcher;

/**
 * Matcher Test fuer die Ubereinstimmung der Form:<br>
 * <b>checkType > queryType</b>
 */
public class GenTypeMatcher_Gen_MatcherTest {

  @Test
  public void match() {
    GenSpecTypeMatcher matcher = new GenSpecTypeMatcher();
    assertTrue( matcher.matchesType( Object.class, String.class ) );
    assertTrue( matcher.matchesType( SuperClass.class, SubClass.class ) );
    assertTrue( matcher.matchesType( Number.class, Integer.class ) );
  }

  @Test
  public void noMatch() {
    GenSpecTypeMatcher matcher = new GenSpecTypeMatcher();
    assertFalse( matcher.matchesType( int.class, String.class ) );
  }

}
