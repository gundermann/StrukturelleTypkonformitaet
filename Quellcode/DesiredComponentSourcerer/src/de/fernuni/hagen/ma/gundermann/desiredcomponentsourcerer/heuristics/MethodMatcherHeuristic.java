package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import matching.methods.ExactMethodMatcher;
import matching.methods.MethodMatcher;
import matching.methods.ParamPermMethodMatcher;

// Exact > ParamPerm + Exact > SpecGen > ParamPerm + SpecGen > Wrapped > ParamPerm + Wrapped
public final class MethodMatcherHeuristic {

  private MethodMatcher exactMM = new ExactMethodMatcher();

  private final MethodMatcher paramPermExactMM = new ParamPermMethodMatcher( () -> exactMM );

  private MethodMatcherHeuristic() {

  }

  public static MethodMatcher[] getMethodMatcher() {
    return new MethodMatcherHeuristic().getMatcherArray();
  }

  private MethodMatcher[] getMatcherArray() {
    return new MethodMatcher[] {
        this.exactMM,
        this.paramPermExactMM };
  }

}
