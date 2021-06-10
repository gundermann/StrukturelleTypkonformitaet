package spi;

import org.junit.Before;

import glue.SigMaGlueException;

/**
 * @deprecated Die {@link PivotMethodTestInfo} wird nur gef�llt, wenn ein Methodenaufruf nicht durchgef�hrt werden konnte. In diesem Fall wird eine {@link SigMaGlueException} geworfen.
 * Da die {@link SigMaGlueException} das Kriterium f�r die dazugeh�rige Heuristik ist, kann auf dieses Interface verzichtet werden.
 * @author ngundermann
 *
 */
@Deprecated
public interface PivotMethodTestInfo {

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
