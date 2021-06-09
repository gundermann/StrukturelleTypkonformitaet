package spi;

import org.junit.Before;

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
