package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;

public class Logger {

  private static boolean isOn = true;

  private static boolean doOutput = false;

  private static Collection<AppendableLogger> appendedLogger = new ArrayList<>();

  private static File output;

  private static String OUTPUT_DIR = "./output";

  public static void setOutputFile( String filename ) {
    try {
      Path outputPath = Paths.get( OUTPUT_DIR );
      if ( !outputPath.toFile().exists() ) {
        outputPath.toFile().mkdir();
      }
      Path output = Paths.get( OUTPUT_DIR + "/" + filename );
      if ( !output.toFile().exists() ) {
        output.toFile().createNewFile();
      }
      Logger.output = output.toFile();
      switchOutputOn();
    }
    catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }

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

  public static void switchOutputOn() {
    doOutput = true;
  }

  public static void switchOutputOff() {
    doOutput = false;
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

  public static void toFile( String pattern, Object... args ) {
    String line = String.format( pattern, args );
    log( "OUTPUT:", line );
    if ( !doOutput ) {
      return;
    }
    try {
      if ( output != null ) {
        Files.write( output.toPath(), line.getBytes(),
            StandardOpenOption.APPEND );
      }
    }
    catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }
}
