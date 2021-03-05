package de.fernuni.hagen.ma.gundermann.matcherexamples.types;

public class SuperWrapper {

  private SuperClass wrapped;

  public SuperWrapper( String string ) {
    this.wrapped = new SuperClass( string );
  }

  @Override
  public String toString() {
    return "WRAPPED_" + this.wrapped.getString();
  }

}
