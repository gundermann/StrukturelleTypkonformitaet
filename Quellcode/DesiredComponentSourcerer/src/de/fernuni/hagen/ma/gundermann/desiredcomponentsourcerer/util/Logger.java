package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util;

import java.util.ArrayList;
import java.util.Collection;

public class Logger {

  private static boolean isOn = true;

  private static Collection<AppendableLogger> appendedLogger = new ArrayList<>();

  public static void appendLogger( AppendableLogger logger ) {
    if ( !appendedLogger.contains( logger ) ) {
      appendedLogger.add( logger );
    }
  }

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
        appendedLogger.forEach( l -> l.logInfo( String.format( "%s %s", prefix, msg ) ) );
      }
      else {
        System.err.println( String.format( "%s %s", prefix, msg ) );
        appendedLogger.forEach( l -> l.logError( String.format( "%s %s", prefix, msg ) ) );
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
