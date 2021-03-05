package de.fernuni.hagen.ma.gundermann.matcherexamples.types;

public class SubWrapper {

  private SubClass wrapped;

  public SubWrapper( String string ) {
    this.wrapped = new SubClass( string );
  }

  @Override
  public String toString() {
    return "WRAPPED_" + this.wrapped.getStringWithoutPrefix();
  }

  public String toStringWithPrefix() {
    return "WRAPPED_" + this.wrapped.getString();
  }

}
