package spi;

public interface PivotMethodTestInfo {

  void reset();

  void markPivotMethodCallExecuted();

  boolean pivotMethodCallExecuted();
}
