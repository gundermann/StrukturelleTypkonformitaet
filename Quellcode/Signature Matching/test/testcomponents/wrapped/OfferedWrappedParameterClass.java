package testcomponents.wrapped;

public class OfferedWrappedParameterClass {

  String wrapped( Wrapped w ) {
    return w.get();
  }

  Wrapped unify( Wrapped w1, Wrapped w2 ) {
    return new Wrapped( w1.get() + w2.get() );
  }
}
