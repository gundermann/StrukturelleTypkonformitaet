package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

public interface HeuristicSetting {

	static boolean COMBINE_LOW_MATCHER_RATING_FIRST = true;

	/**
	 * Wird in der Arbeit nicht weiter analysiert. Sie dient nur einer effizienteren
	 * Analyse innerhalb des Testsystems
	 */
	static boolean BLACKLIST_NO_IMPLEMENTATION_AVAILABLE = true;

	static boolean BLACKLIST_FIRST_CALLED_METHOD = true;

	static boolean BLACKLIST_SINGLE_METHOD_TEST_FAILED = true;

	static boolean COMBINE_COMPOMENTS_WITH_PASSED_TESTS_FIRST = true;
}
