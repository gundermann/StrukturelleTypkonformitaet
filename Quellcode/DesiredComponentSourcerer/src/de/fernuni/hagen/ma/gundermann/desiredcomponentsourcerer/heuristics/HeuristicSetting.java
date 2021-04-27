package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

public interface HeuristicSetting {

  static boolean COMBINE_LOW_MATCHER_RATING_FIRST = true;

  static boolean BLACKLIST_NO_IMPLEMENTATION_AVAILABLE = true;

  static boolean BLACKLIST_PIVOT_METHOD_CALL = true;

  static boolean BLACKLIST_SINGLE_METHOD_TEST_FAILED = true;

  static boolean COMBINE_COMPOMENTS_WITH_PASSED_TESTS_FIRST = true;
}
