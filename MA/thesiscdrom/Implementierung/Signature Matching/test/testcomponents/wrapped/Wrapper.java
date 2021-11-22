package testcomponents.wrapped;

public class Wrapper {

  private final Wrapped wrapped;

  public Wrapper( Wrapped wrapped ) {
    this.wrapped = wrapped;
  }

  public String get() {
    return wrapped.get();
  }
}
