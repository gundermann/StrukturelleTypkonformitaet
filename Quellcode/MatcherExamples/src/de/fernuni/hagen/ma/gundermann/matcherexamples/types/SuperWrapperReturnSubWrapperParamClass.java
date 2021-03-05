package de.fernuni.hagen.ma.gundermann.matcherexamples.types;

public class SuperWrapperReturnSubWrapperParamClass {

  public SuperWrapper addHello( SubWrapper a ) {
    return new SuperWrapper( a.toString() + "hello" );
  }

  public SuperWrapper add( SubWrapper a, SubWrapper b ) {
    return new SuperWrapper( a.toString() + b.toString() );
  }
}
