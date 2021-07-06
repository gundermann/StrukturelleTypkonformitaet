package matching.types.testtypes;

public class ClassSpec {

  public Number add( Double a, Double b ) {
    return a + b;
  }

  public Double sub( Number a, Number b ) {
    return a.doubleValue() - b.doubleValue();
  }

  public Number div( Double a, Number b ) {
    return a / b.doubleValue();
  }

  public Double mult( Number a, Double b ) {
    return b * a.doubleValue();
  }
}
