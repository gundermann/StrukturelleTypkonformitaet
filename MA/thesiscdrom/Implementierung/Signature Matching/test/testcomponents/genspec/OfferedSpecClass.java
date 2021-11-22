package testcomponents.genspec;

public class OfferedSpecClass {

  public String concat( Specific spec, Specific s ) {
    return spec.getStringAttr().concat( s.getStringAttr() );
  }

  public Specific add( Specific spec, Integer i ) {
    int sum = spec.getIntAttr() + i;
    return new Specific( spec.getStringAttr(), sum );
  }

  public Specific add( Specific spec, Specific spec2 ) {
    int sum = spec.getIntAttr() + spec2.getIntAttr();
    return new Specific( spec.getStringAttr(), sum );
  }

  public Long getBoxedLongAttr( Specific spec ) {
    return spec.getBoxedLongAttr();
  }

  public Boolean and( Specific spec, Boolean b ) {
    return b && spec.isSpecificAttr();
  }

  public Specific getNull() {
    return null;
  }

}
