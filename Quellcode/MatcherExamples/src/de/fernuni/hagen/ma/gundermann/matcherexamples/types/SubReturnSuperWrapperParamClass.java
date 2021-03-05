package de.fernuni.hagen.ma.gundermann.matcherexamples.types;

public class SubReturnSuperWrapperParamClass {

  public SubClass addHello( SuperWrapper a ) {
    return new SubClass( a.toString() + "hello" );
  }

  public SubClass add( SuperWrapper a, SuperWrapper b ) {
    return new SubClass( a.toString() + b.toString() );
  }
}
