package de.fernuni.hagen.ma.gundermann.matcherexamples.types;

public class SuperWrapperReturnSubParamClass {

  public SuperWrapper addHello( SubClass a ) {
    return new SuperWrapper( a.getString() + "hello" );
  }

  public SuperWrapper add( SubClass a, SubClass b ) {
    return new SuperWrapper( a.getString() + b.getString() );
  }

}
