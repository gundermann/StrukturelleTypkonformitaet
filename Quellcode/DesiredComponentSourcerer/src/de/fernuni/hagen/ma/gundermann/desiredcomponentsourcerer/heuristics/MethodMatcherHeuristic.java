package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import matching.methods.ExactMethodMatcher;
import matching.methods.GenSpecMethodMatcher;
import matching.methods.MatcherCombiner;
import matching.methods.MethodMatcher;
import matching.methods.ParamPermMethodMatcher;
import matching.methods.WrappedTypeMethodMatcher;

// Exact > ParamPerm + Exact > GenSpec > ParamPerm + GenSpec > Wrapped > ParamPerm + Wrapped
public final class MethodMatcherHeuristic {

  private final MethodMatcher exactMM = new ExactMethodMatcher();

  private final MethodMatcher paramPermExactMM = new ParamPermMethodMatcher( () -> exactMM );

  private final MethodMatcher genSpecMM = new GenSpecMethodMatcher();

  private final MethodMatcher paramPermGenSpecMM = new ParamPermMethodMatcher( () -> genSpecMM );

  private final MethodMatcher combinedParamPermGenSpecExactMM = new ParamPermMethodMatcher(
      MatcherCombiner.combine( genSpecMM, exactMM ) );

  private final MethodMatcher wrappedMM = new WrappedTypeMethodMatcher( () -> combinedParamPermGenSpecExactMM );

  private final MethodMatcher paramPermWrappedMM = new ParamPermMethodMatcher( () -> wrappedMM );

  private MethodMatcherHeuristic() {

  }

  public static MethodMatcher[] getMethodMatcher() {
    return new MethodMatcherHeuristic().getMatcherArray();
  }

  private MethodMatcher[] getMatcherArray() {
    return new MethodMatcher[] {
        this.exactMM,
        this.paramPermExactMM,
        this.genSpecMM,
        this.paramPermGenSpecMM,
        this.wrappedMM,
        this.paramPermWrappedMM };
  }

}
