package matching;

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
    if ( isOn ) {
      System.out.println( String.format( "%s %s", prefix, msg ) );
    }
  }

}
