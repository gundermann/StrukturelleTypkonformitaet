package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import matching.MatcherCombiner;
import matching.types.CombinableTypeMatcher;
import matching.types.ExactTypeMatcher;
import matching.types.GenSpecTypeMatcher;
import matching.types.StructuralTypeMatcher;
import matching.types.TypeMatcher;
import matching.types.WrappedTypeMatcher;

// Exact > ParamPerm + Exact > GenSpec > ParamPerm + GenSpec > Wrapped > ParamPerm + Wrapped
public enum DefaultTypeMatcherHeuristic {
  INSTANCE;

  private final CombinableTypeMatcher exactTM = new ExactTypeMatcher();

  private final Long exactTMPrio = 0L;

  private final CombinableTypeMatcher genSpecTM = new GenSpecTypeMatcher();

  private final Long genSpecTMPrio = 100L;

  private final TypeMatcher combinedGenSpecExactTM = MatcherCombiner.combine( genSpecTM, exactTM ).get();

  private final Long combinedGenSpecExactPrio = 200L;

  private final CombinableTypeMatcher wrappedTM = new WrappedTypeMatcher( () -> combinedGenSpecExactTM );

  private final Long wrappedTMPrio = 300L;

  private final TypeMatcher combinedWrappedGenSpecExact = MatcherCombiner.combine( genSpecTM, exactTM, wrappedTM )
      .get();

  private final Long combinedWrappedGenSpecExactPrio = 400L;

  private final TypeMatcher structExactTM = new StructuralTypeMatcher( () -> exactTM );

  private final Long structExactTMPrio = 500L;

  private final TypeMatcher structGenSpecExactTM = new StructuralTypeMatcher( () -> combinedGenSpecExactTM );

  private final Long structGenSpecExactTMPrio = 600L;

  private final TypeMatcher structWrappedGenSpecExactTM = new StructuralTypeMatcher(
      () -> combinedWrappedGenSpecExact );

  private final Long structWrappedGenSpecExactTMPrio = 700L;

  private final Map<TypeMatcher, Long> fullTypeMatcherWithPrio = initFullTypeMatcher();

  private TypeMatcher[] rankedTypeMatcher = new TypeMatcher[] {
      this.exactTM,
      this.genSpecTM,
      this.combinedGenSpecExactTM,
      this.wrappedTM,
      this.combinedWrappedGenSpecExact,
      this.structExactTM,
      this.structGenSpecExactTM,
      this.structWrappedGenSpecExactTM };

  private TypeMatcher[] typeMatchers = new TypeMatcher[] {
      // Werden ohne die ersten beiden bessere Ergebnisse erzielt? => scheinbar ja.
      // TODO Die Frage ist, wie es sich mit vielen Beans verhaelt
      // this.structExactTM,
      // this.structGenSpecExactTM,
      this.structWrappedGenSpecExactTM };

  // TODO später
  // private final TypeMatcher recursiveWrappedTM = new WrappedTypeMatcher(
  // () -> MatcherCombiner.combine( genSpecTM, exactTM, recursiveWrappedTM ) );

  private DefaultTypeMatcherHeuristic() {

  }

  private Map<TypeMatcher, Long> initFullTypeMatcher() {
    Map<TypeMatcher, Long> fullTypeMatcher = new HashMap<>();
    fullTypeMatcher.put( combinedGenSpecExactTM, combinedGenSpecExactPrio );
    fullTypeMatcher.put( exactTM, exactTMPrio );
    fullTypeMatcher.put( genSpecTM, genSpecTMPrio );
    fullTypeMatcher.put( wrappedTM, wrappedTMPrio );
    fullTypeMatcher.put( combinedWrappedGenSpecExact, combinedWrappedGenSpecExactPrio );
    fullTypeMatcher.put( structExactTM, structExactTMPrio );
    fullTypeMatcher.put( structGenSpecExactTM, structGenSpecExactTMPrio );
    fullTypeMatcher.put( structWrappedGenSpecExactTM, structWrappedGenSpecExactTMPrio );
    return fullTypeMatcher;
  }

  public static TypeMatcher[] getFullTypeMatcher() {
    return INSTANCE.getMatcherArray();
  }

  public static TypeMatcher[] getPartlyTypeMatcher() {
    return INSTANCE.getPartlyMatcherArray();
  }

  public static void addFullTypeMatcher( long prio, TypeMatcher matcher ) {
    INSTANCE.addTypeMatcher( prio, matcher );
  }

  private void addTypeMatcher( long prio, TypeMatcher matcher ) {
    fullTypeMatcherWithPrio.put( matcher, Long.valueOf( prio ) );
    reorganzieFullTypeMatcher();
  }

  private void reorganzieFullTypeMatcher() {
    rankedTypeMatcher = new TypeMatcher[fullTypeMatcherWithPrio.size()];
    long lastPrio = -1;
    for ( int i = 0; i < rankedTypeMatcher.length; i++ ) {
      TypeMatcher m = getNextRelevantFullTypeMatcher( lastPrio );
      rankedTypeMatcher[i] = m;
    }

  }

  private TypeMatcher getNextRelevantFullTypeMatcher( long lastPrio ) {
    return fullTypeMatcherWithPrio.entrySet().stream().filter( e -> e.getValue() > lastPrio )
        .min( ( e1, e2 ) -> Long.compare( e1.getValue(), e2.getValue() ) ).map( Entry::getKey ).get();
  }

  private TypeMatcher[] getPartlyMatcherArray() {
    return this.typeMatchers;
  }

  private TypeMatcher[] getMatcherArray() {
    return rankedTypeMatcher;
  }

  public static TypeMatcher[] createTypeMatcher( TypeMatcher[] fullTypeMatcher ) {
    INSTANCE.reorgansizeMatchers( fullTypeMatcher );
    return INSTANCE.getPartlyMatcherArray();
  }

  private void reorgansizeMatchers( TypeMatcher[] fullTypeMatcher ) {
    this.typeMatchers = Stream.of( fullTypeMatcher ).map( m -> new StructuralTypeMatcher( () -> m ) )
        .collect( Collectors.toList() ).toArray( new TypeMatcher[] {} );
  }

}
