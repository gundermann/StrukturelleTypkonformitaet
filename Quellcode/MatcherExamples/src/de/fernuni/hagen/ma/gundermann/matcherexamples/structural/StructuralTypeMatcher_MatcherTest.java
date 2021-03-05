package de.fernuni.hagen.ma.gundermann.matcherexamples.structural;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubReturnSubParamClass1;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubReturnSubParamClass2;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubReturnSuperParamClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubReturnSuperWrapperParamClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SubWrapperReturnSubParamClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SuperReturnSubParamClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SuperWrapperReturnSubParamClass;
import de.fernuni.hagen.ma.gundermann.matcherexamples.types.SuperWrapperReturnSubWrapperParamClass;
import matching.MatcherCombiner;
import matching.modules.ExactTypeMatcher;
import matching.modules.GenSpecTypeMatcher;
import matching.modules.StructuralTypeMatcher;
import matching.modules.WrappedTypeMatcher;

/**
 * Matcher Test fuer die Ubereinstimmung der Form:<br>
 * <b>checkType &equiv;<sub>Struct</sub> queryType</b>
 */
public class StructuralTypeMatcher_MatcherTest {

  // Hier muss ein Matcher angegeben werden, die fuer den rekursiven Aufruf bei der Pruefung auf Uebereinstimmung
  // verwendet werden soll.
  // Derzeit wird nur ein rekursiver Aufruf vorgenommen.
  // Der uebergebene Matcher kombiniert folgende Matcher:
  // 1. Einem ExactTypeMatcher
  // 2. Einem GenSpecTypeMatcher
  // 3. Einen WrappedTypeMatcher, der wiederum eine Kombination aus dem ExactTypeMatcher mit dem GenSpecTypeMatcher
  // verwendet
  private StructuralTypeMatcher matcher = new StructuralTypeMatcher(
      MatcherCombiner.combine( new ExactTypeMatcher(), new GenSpecTypeMatcher(),
          new WrappedTypeMatcher( MatcherCombiner.combine( new ExactTypeMatcher(), new GenSpecTypeMatcher() ) ) ) );

  /**
   * &forall; ( {@link SubReturnSubParamClass1}.m1( m1Param ) : m1Return ) : &exist; (
   * {@link SubReturnSubParamClass2}.m2( m2Param ) : m2Return) : m1Param &equiv;<sub>Exact</sub> m2Param &and; m1Return
   * &equiv;<sub>Exact</sub> m2Return
   */
  @Test
  public void match_exactReturn_exactParam() {
    assertTrue( matcher.matchesType( SubReturnSubParamClass1.class, SubReturnSubParamClass2.class ) );
  }

  /**
   * &forall; ( {@link SubReturnSubParamClass1}.m1( m1Param ) : m1Return ) : &exist; (
   * {@link SubReturnSubParamClass2}.m2( m2Param ) : m2Return) : m1Param &equiv;<sub>Gen</sub> m2Param &and; m1Return
   * &equiv;<sub>Exact</sub> m2Return
   */
  @Test
  public void match_exactReturn_genParam() {
    assertTrue( matcher.matchesType( SubReturnSuperParamClass.class, SubReturnSubParamClass1.class ) );
  }

  /**
   * &forall; ( {@link SubReturnSubParamClass1}.m1( m1Param ) : m1Return ) : &exist; (
   * {@link SubReturnSubParamClass2}.m2( m2Param ) : m2Return) : m1Param &equiv;<sub>Spec</sub> m2Param &and; m1Return
   * &equiv;<sub>Exact</sub> m2Return
   */
  @Test
  public void match_exactReturn_specParam() {
    assertTrue( matcher.matchesType( SubReturnSubParamClass1.class, SubReturnSuperParamClass.class ) );
  }

  /**
   * &forall; ( {@link SubReturnSubParamClass1}.m1( m1Param ) : m1Return ) : &exist; (
   * {@link SubReturnSubParamClass2}.m2( m2Param ) : m2Return) : m1Param &equiv;<sub>Spec</sub> m2Param &and; m1Return
   * &equiv;<sub>Gen</sub> m2Return
   */
  @Test
  public void match_genReturn_specParam() {
    assertTrue( matcher.matchesType( SuperReturnSubParamClass.class, SubReturnSuperParamClass.class ) );
  }

  /**
   * &forall; ( {@link SubReturnSubParamClass1}.m1( m1Param ) : m1Return ) : &exist; (
   * {@link SubReturnSubParamClass2}.m2( m2Param ) : m2Return) : m1Param &equiv;<sub>Gen</sub> m2Param &and; m1Return
   * &equiv;<sub>Spec</sub> m2Return
   */
  @Test
  public void match_specReturn_genParam() {
    assertTrue( matcher.matchesType( SubReturnSuperParamClass.class, SuperReturnSubParamClass.class ) );
  }

  /**
   * &forall; ( {@link SubReturnSubParamClass1}.m1( m1Param ) : m1Return ) : &exist; (
   * {@link SubReturnSubParamClass2}.m2( m2Param ) : m2Return) : m1Param &equiv;<sub>Wrapper<sub>Gen</sub></sub> m2Param
   * &and; m1Return &equiv;<sub>Spec</sub> m2Return
   */
  @Test
  public void match_specReturn_wrapperGenParam() {
    assertTrue( matcher.matchesType( SubReturnSuperWrapperParamClass.class, SuperReturnSubParamClass.class ) );
  }

  /**
   * &forall; ( {@link SubReturnSubParamClass1}.m1( m1Param ) : m1Return ) : &exist; (
   * {@link SubReturnSubParamClass2}.m2( m2Param ) : m2Return) : m1Param &equiv;<sub>Spec</sub> m2Param &and; m1Return
   * &equiv;<sub>Wrapper<sub>Gen</sub></sub> m2Return
   */
  @Test
  public void match_wrapperGenReturn_specParam() {
    assertTrue( matcher.matchesType( SuperWrapperReturnSubParamClass.class, SubReturnSuperParamClass.class ) );
  }

  /**
   * &forall; ( {@link SubReturnSubParamClass1}.m1( m1Param ) : m1Return ) : &exist; (
   * {@link SubReturnSubParamClass2}.m2( m2Param ) : m2Return) : m1Param &equiv;<sub>Wrapper<sub>Spec</sub></sub>
   * m2Param &and; m1Return &equiv;<sub>Wrapper<sub>Gen</sub></sub> m2Return
   */
  @Test
  public void match_wrapperGenReturn_wrapperSpecParam() {
    assertTrue( matcher.matchesType( SuperWrapperReturnSubWrapperParamClass.class, SubReturnSuperParamClass.class ) );
  }

  /**
   * &forall; ( {@link SubReturnSubParamClass1}.m1( m1Param ) : m1Return ) : &exist; (
   * {@link SubReturnSubParamClass2}.m2( m2Param ) : m2Return) : m1Param &equiv;<sub>Wrapper<sub>Exact</sub></sub>
   * m2Param &and; m1Return &equiv;<sub>Wrapper<sub>Spec</sub></sub> m2Return
   */
  @Test
  public void match_wrapperSpecReturn_wrapperExactParam() {
    assertTrue( matcher.matchesType( SubWrapperReturnSubParamClass.class, SuperReturnSubParamClass.class ) );
  }

}
