package de.fernuni.hagen.ma.gundermann.matcherexamples.types;

public class SubClass extends SuperClass {

  public SubClass( String string ) {
    super( "Sub" + string );
  }

  public String getStringWithoutPrefix() {
    return getString().substring( 3 );
  }
}
