package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import matching.MatcherCombiner;
import matching.modules.ExactTypeMatcher;
import matching.modules.GenSpecTypeMatcher;
import matching.modules.StructuralTypeMatcher;
import matching.modules.TypeMatcher;
import matching.modules.WrappedTypeMatcher;

// Exact > ParamPerm + Exact > GenSpec > ParamPerm + GenSpec > Wrapped > ParamPerm + Wrapped
public final class MethodMatcherHeuristic {

  private final TypeMatcher exactTM = new ExactTypeMatcher();

  private final TypeMatcher genSpecTM = new GenSpecTypeMatcher();

  private final TypeMatcher combinedGenSpecExactTM = MatcherCombiner.combine( genSpecTM, exactTM ).get();

  private final TypeMatcher wrappedTM = new WrappedTypeMatcher( () -> combinedGenSpecExactTM );

  private final TypeMatcher combinedWrappedGenSpecExact = MatcherCombiner.combine( wrappedTM, genSpecTM, exactTM )
      .get();

  private final TypeMatcher structExactTM = new StructuralTypeMatcher( () -> exactTM );

  private final TypeMatcher structGenSpecExactTM = new StructuralTypeMatcher( () -> combinedGenSpecExactTM );

  private final TypeMatcher structWrappedGenSpecExactTM = new StructuralTypeMatcher(
      () -> combinedWrappedGenSpecExact );

  // TODO später
  // private final TypeMatcher recursiveWrappedTM = new WrappedTypeMatcher(
  // () -> MatcherCombiner.combine( genSpecTM, exactTM, recursiveWrappedTM ) );

  private MethodMatcherHeuristic() {

  }

  private TypeMatcher[] getMatcherArray() {
    return new TypeMatcher[] {
        this.exactTM,
        this.genSpecTM,
        this.combinedGenSpecExactTM,
        this.wrappedTM,
        this.combinedWrappedGenSpecExact,
        this.structExactTM,
        this.structGenSpecExactTM,
        this.structWrappedGenSpecExactTM };
  }

  public static TypeMatcher[] getTypeMatcher() {
    return new MethodMatcherHeuristic().getMatcherArray();
  }

}
