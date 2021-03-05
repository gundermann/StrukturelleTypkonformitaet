package de.fernuni.hagen.ma.gundermann.matcherexamples.wrapped;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubWrapper;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SuperClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SuperWrapper;
import matching.MatcherCombiner;
import matching.modules.ExactTypeMatcher;
import matching.modules.GenSpecTypeMatcher;
import matching.modules.WrappedTypeMatcher;

/**
 * Matcher Test fuer die Ubereinstimmung der Form:<br>
 * <b>checkType &equiv;<sub>Exact,Gen,Spec</sub> queryType#attr</b>
 */
public class WrappedTypeMatcher_Wrapped_MatcherTest {

  // Hier muss ein Matcher angegeben werden, die fuer den rekursiven Aufruf bei der Pruefung auf Uebereinstimmung
  // verwendet werden soll.
  // Derzeit wird nur ein rekursiver Aufruf vorgenommen.
  // Der uebergebene Matcher kombiniert den ExactTypeMatcher mit dem GenSpecTypeMatcher, in der Form, dass sie in der
  // genannten Reihenfolge angesprochen werden.
  private WrappedTypeMatcher matcher = new WrappedTypeMatcher(
      MatcherCombiner.combine( new ExactTypeMatcher(), new GenSpecTypeMatcher() ) );

  @Test
  public void match() {
    assertTrue( matcher.matchesType( boolean.class, Boolean.class ) );
    assertTrue( matcher.matchesType( int.class, Integer.class ) );
  }

  /**
   * {@link SubClass} &equiv;<sub>Exact</sub> {@link SubWrapper}#wrapped
   */
  @Test
  public void match_wrapped_exact() {
    assertTrue( matcher.matchesType( SubClass.class, SubWrapper.class ) );
  }

  /**
   * {@link SubClass} &equiv;<sub>Spec</sub> {@link SuperWrapper}#wrapped
   */
  @Test
  public void match_wrapped_spec() {
    assertTrue( matcher.matchesType( SubClass.class, SuperWrapper.class ) );
  }

  /**
   * {@link SuperClass} &equiv;<sub>Gen</sub> {@link SubWrapper}#wrapped
   */
  @Test
  public void match_wrapped_gen() {
    assertTrue( matcher.matchesType( SuperClass.class, SubWrapper.class ) );
  }

  @Test
  public void noMatch() {
    assertFalse( matcher.matchesType( String.class, String.class ) );
  }

}
