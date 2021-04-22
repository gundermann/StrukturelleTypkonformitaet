package spi;

public interface PivotMethodTestInfo {

  PivotMethodInfoContainer getPivotMethodInfoContainer();

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
