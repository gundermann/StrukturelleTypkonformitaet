package matching;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Setting {

  static Function<Collection<MatcherRate>, MatcherRate> MIN_MAX_AVG = matcherRateCol -> {
    Optional<MatcherRate> optMin = matcherRateCol.stream()
        .min( MatcherRate::compare );
    if ( optMin.isPresent() ) {
      Optional<MatcherRate> optMax = matcherRateCol.stream()
          .max( MatcherRate::compare );
      if ( optMax.isPresent() ) {
        MatcherRate avg = new MatcherRate();
        avg.add( String.format( "Min: %s | Max: %s", optMin.get().toString(), optMax.get().toString() ),
            ( optMin.get().getMatcherRating() + optMax.get().getMatcherRating() ) / 2 );
        return avg;
      }
    }
    return null;
  };

  // Delegation von MatcherRate::compare
  public static BiFunction<MatcherRate, MatcherRate, Integer> COMPARE_QUALITATIVE_METHOD_MATCH_RATE = ( m1,
      m2 ) -> m1 == null ? 1 : m2 == null ? -1 : Double.compare( m1.getMatcherRating(), m2.getMatcherRating() );

  public static double EXACT_TYPE_MATCHER_RATING = 100d;

  public static double GEN_SPEC_TYPE_MATCHER_BASE_RATING = 200d;

  public static double STRUCTURAL_TYPE_MATCHER_BASE_RATING = 1000d;

  public static double WRAPPEN_TYPE_MATCHER_BASE_RATING = 600d;

  public static double PARAM_PERM_METHOD_TYPE_MATCHER_BASE_RATING = 0d;

  public static Function<Stream<MatcherRate>, MatcherRate> QUALITATIVE_COMPONENT_MATCH_RATE_CUMULATION = matcherRateCol -> MIN_MAX_AVG
      .apply( matcherRateCol.collect( Collectors.toList() ) );

  public static BiFunction<MatcherRate, MatcherRate, Boolean> HIGHER_QUALITATIVE_METHOD_MATCH_RATE_CONDITION = ( higher,
      lower ) -> higher != null && lower != null
          && ( COMPARE_QUALITATIVE_METHOD_MATCH_RATE.apply( higher, lower ) < 0
              || lower.getMatcherRating() < EXACT_TYPE_MATCHER_RATING )
          && higher.getMatcherRating() >= EXACT_TYPE_MATCHER_RATING;

}
