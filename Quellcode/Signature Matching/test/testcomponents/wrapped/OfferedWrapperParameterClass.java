package testcomponents.wrapped;

public class OfferedWrapperParameterClass {

  public String wrapper( Wrapper w ) {
    return w.get();
  }

  public Wrapper unify( Wrapper w1, Wrapper w2 ) {
    return new Wrapper( new Wrapped( w1.get() + w2.get() ) );
  }
}
