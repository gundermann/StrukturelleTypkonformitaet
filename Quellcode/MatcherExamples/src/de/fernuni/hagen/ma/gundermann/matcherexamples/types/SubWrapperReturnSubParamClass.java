package de.fernuni.hagen.ma.gundermann.matcherexamples.types;

public class SubWrapperReturnSubParamClass {

  public SubWrapper addHello( SubClass a ) {
    return new SubWrapper( a.getString() + "hello" );
  }

  public SubWrapper add( SubClass a, SubClass b ) {
    return new SubWrapper( a.getString() + b.getString() );
  }
}
