package testcomponents.genspec;

public class OfferedGenClass {

  public String concat( General gen1, General gen2 ) {
    return gen1.getStringAttr().concat( gen2.getStringAttr() );
  }

  public General add( General gen, Integer i ) {
    int sum = gen.getIntAttr() + i;
    return new General( gen.getStringAttr(), sum, gen.getBoxedLongAttr() );
  }

  public General add( General gen1, General gen2 ) {
    int sum = gen1.getIntAttr() + gen2.getIntAttr();
    long longSum = gen1.getBoxedLongAttr() + gen2.getBoxedLongAttr();
    return new General( gen1.getStringAttr(), sum, longSum );
  }

  public Long getBoxedLongAttr( General gen ) {
    return gen.getBoxedLongAttr();
  }

  public General getNull() {
    return null;
  }

}
