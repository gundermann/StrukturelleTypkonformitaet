package spi;

@Deprecated
public class PivotMethodInfoContainer {

  boolean pivotMethodExecuted = false;

  boolean isPivotMethodExecuted() {
    return pivotMethodExecuted;
  }

  public void setPivotMethodExecuted( boolean pivotMethodExecuted ) {
    this.pivotMethodExecuted = pivotMethodExecuted;
  }

}
