package spi;

import org.junit.Before;

import glue.SigMaGlueException;

/**
 * @deprecated Die {@link PivotMethodTestInfo} wird nur gefüllt, wenn ein Methodenaufruf nicht durchgeführt werden konnte. In diesem Fall wird eine {@link SigMaGlueException} geworfen.
 * Da die {@link SigMaGlueException} das Kriterium für die dazugehörige Heuristik ist, kann auf dieses Interface verzichtet werden.
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
