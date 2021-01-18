package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util;

public class AnalyzationUtils {
  public static int filterCount = 0;

  public static boolean filterWithAnalyticalCount( boolean filterPassed ) {
    if ( !filterPassed ) {
      filterCount++;
    }
    return filterPassed;
  }
}
