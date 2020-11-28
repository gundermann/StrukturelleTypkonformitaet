package testcomponents.genspec;

public class Specific extends General {

  private final boolean specificAttr;

  public Specific( String stringAttr ) {
    super( stringAttr );
    specificAttr = false;
  }

  public Specific( String stringAttr, Integer i ) {
    super( stringAttr, i, Long.valueOf( i ) );
    specificAttr = false;
  }

  public Specific( String stringAttr, boolean specificAttr ) {
    super( stringAttr );
    this.specificAttr = specificAttr;
  }

  public boolean isSpecificAttr() {
    return specificAttr;
  }

  @Override
  public Long getBoxedLongAttr() {
    return super.getBoxedLongAttr();
  }

}
