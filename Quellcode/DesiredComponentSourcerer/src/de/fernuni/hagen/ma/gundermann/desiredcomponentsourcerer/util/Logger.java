package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util;

public class Logger {

  private static boolean isOn = true;

  public static void switchOn() {
    isOn = true;
  }

  public static void switchOff() {
    isOn = false;
  }

  public static void info( String msg ) {
    log( "INFO:", msg );
  }

  private static void log( String prefix, String msg ) {
    log( prefix, msg, false );
  }

  private static void log( String prefix, String msg, boolean error ) {
    if ( isOn ) {
      if ( !error ) {
        System.out.println( String.format( "%s %s", prefix, msg ) );
      }
      else {
        System.err.println( String.format( "%s %s", prefix, msg ) );
      }
    }
  }

  public static void err( String format ) {
    log( "ERR", format, true );
  }

  public static void infoF( String pattern, Object... args ) {
    info( String.format( pattern, args ) );
  }

}
