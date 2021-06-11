package spi;

import org.junit.Before;


/**
 * Es gilt trotzdem zu pruefen, ob die Heuristik, fuer die dieses Konstrukt notwendig ist, Relevanz hat.
 * @author ngundermann
 *
 */
public interface FirstCalledMethodInfo {

  PivotMethodInfoContainer getPivotMethodInfoContainer();


  @Before
  default void before() {
    reset();
  }

  default void reset() {
    getPivotMethodInfoContainer().setPivotMethodExecuted( false );
  }

  default void markPivotMethodCallExecuted() {
    getPivotMethodInfoContainer().setPivotMethodExecuted( true );
  }

  default boolean pivotMethodCallExecuted() {
    return getPivotMethodInfoContainer().isPivotMethodExecuted();
  }
}
