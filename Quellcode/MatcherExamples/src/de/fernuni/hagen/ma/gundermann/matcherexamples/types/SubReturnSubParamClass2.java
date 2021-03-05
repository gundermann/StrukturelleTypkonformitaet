package de.fernuni.hagen.ma.gundermann.matcherexamples.types;

public class SubReturnSubParamClass2 {

  public SubClass helloAdd( SubClass a ) {
    return new SubClass( "hello" + a.getString() );
  }

  public SubClass addParams( SubClass a, SubClass b ) {
    return new SubClass( a.getString() + b.getString() );
  }

}
