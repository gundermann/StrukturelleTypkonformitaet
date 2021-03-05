package de.fernuni.hagen.ma.gundermann.matcherexamples.types;

public class SubReturnSuperParamClass {

  public SubClass addHello( SuperClass a ) {
    return new SubClass( a.getString() + "hello" );
  }

  public SubClass add( SuperClass a, SuperClass b ) {
    return new SubClass( a.getString() + b.getString() );
  }
}
