package de.fernuni.hagen.ma.gundermann.matcherexamples.types;

public class SuperReturnSubParamClass {

  public SuperClass helloAdd( SubClass a ) {
    return new SuperClass( "hello" + a.getString() );
  }

  public SuperClass addParams( SubClass a, SubClass b ) {
    return new SuperClass( a.getString() + b.getString() );
  }

}
