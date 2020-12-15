package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import matching.modules.PartlyTypeMatchingInfo;

abstract class CombinationFinderUtils {

  private static final double FULL_MATCH = 1.0d;

  private CombinationFinderUtils() {

  }

  static boolean isFullMatchingComponent( PartlyTypeMatchingInfo matchingInfo ) {
    return matchingInfo.getQuantitaiveMatchRating() == FULL_MATCH;
  }
}
