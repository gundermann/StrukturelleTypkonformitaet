package de.fernuni.hagen.ma.gundermann.matcherexamples.wrapper;

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
 * <b>checkType#attr &equiv;<sub>Exact,Gen,Spec</sub> queryType</b>
 */
public class WrappedTypeMatcher_Wrapper_MatcherTest {

  // Hier muss ein Matcher angegeben werden, die fuer den rekursiven Aufruf bei der Pruefung auf Uebereinstimmung
  // verwendet werden soll.
  // Derzeit wird nur ein rekursiver Aufruf vorgenommen.
  // Der uebergebene Matcher kombiniert den ExactTypeMatcher mit dem GenSpecTypeMatcher, in der Form, dass sie in der
  // genannten Reihenfolge angesprochen werden.
  private WrappedTypeMatcher matcher = new WrappedTypeMatcher(
      MatcherCombiner.combine( new ExactTypeMatcher(), new GenSpecTypeMatcher() ) );

  @Test
  public void match() {
    assertTrue( matcher.matchesType( Boolean.class, boolean.class ) );
    assertTrue( matcher.matchesType( Integer.class, int.class ) );
  }

  /**
   * {@link SubWrapper}#wrapped &equiv;<sub>Exact</sub> {@link SubClass}
   */
  @Test
  public void match_wrapped_exact() {
    assertTrue( matcher.matchesType( SubWrapper.class, SubClass.class ) );
  }

  /**
   * {@link SuperWrapper}#wrapped &equiv;<sub>Spec</sub> {@link SubClass}
   */
  @Test
  public void match_wrapped_spec() {
    assertTrue( matcher.matchesType( SuperWrapper.class, SubClass.class ) );
  }

  /**
   * {@link SubWrapper}#wrapped &equiv;<sub>Gen</sub> {@link SuperClass}
   */
  @Test
  public void match_wrapped_gen() {
    assertTrue( matcher.matchesType( SubWrapper.class, SuperClass.class ) );
  }

  @Test
  public void noMatch() {
    assertFalse( matcher.matchesType( String.class, String.class ) );
  }

}
