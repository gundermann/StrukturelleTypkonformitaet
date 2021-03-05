package de.fernuni.hagen.ma.gundermann.matcherexamples.types;

public class SubReturnSubParamClass1 {

  public SubClass addHello( SubClass a ) {
    return new SubClass( a.getString() + "hello" );
  }

  public SubClass add( SubClass a, SubClass b ) {
    return new SubClass( a.getString() + b.getString() );
  }
}
