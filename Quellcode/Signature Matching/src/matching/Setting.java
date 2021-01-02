package matching;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Setting {

  // SETTING F
  private static Function<Collection<MatcherRate>, MatcherRate> MIN_MAX_AVG = matcherRateCol -> {
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

  // SETTING G
  private static Function<Collection<MatcherRate>, MatcherRate> ALL_AVG = matcherRateCol -> {
    double avg = matcherRateCol.stream().mapToDouble( MatcherRate::getMatcherRating ).average().orElse( Double.NaN );
    if ( avg == Double.NaN ) {
      return null;
    }
    MatcherRate result = new MatcherRate();
    result.add( "Avarage", avg );
    return result;
  };

  // Delegation von MatcherRate::compare
  public static BiFunction<MatcherRate, MatcherRate, Integer> COMPARE_QUALITATIVE_METHOD_MATCH_RATE = ( m1,
      m2 ) -> m1 == null ? 1 : m2 == null ? -1 : Double.compare( m1.getMatcherRating(), m2.getMatcherRating() );

  public static double EXACT_TYPE_MATCHER_RATING = 100d;

  public static double GEN_SPEC_TYPE_MATCHER_BASE_RATING = 200d;

  public static double STRUCTURAL_TYPE_MATCHER_BASE_RATING = 1000d;

  public static double WRAPPEN_TYPE_MATCHER_BASE_RATING = 600d;

  public static double PARAM_PERM_METHOD_TYPE_MATCHER_BASE_RATING = 0d;

  // is MatcherRate >= Exact
  private static boolean isMatcherRateRelevant( MatcherRate mr ) {
    return mr.getMatcherRating() >= EXACT_TYPE_MATCHER_RATING;
  }

  // SETTING _MIN>=Exact
  private static BiFunction<MatcherRate, MatcherRate, Boolean> MIN_RATE_PER_METHOD = ( higher,
      lower ) -> ( COMPARE_QUALITATIVE_METHOD_MATCH_RATE.apply( higher, lower ) < 0
          || lower.getMatcherRating() < EXACT_TYPE_MATCHER_RATING )
          && isMatcherRateRelevant( higher );

  // SETTING _MAX
  private static BiFunction<MatcherRate, MatcherRate, Boolean> MAX_RATE_PER_METHOD = ( higher,
      lower ) -> COMPARE_QUALITATIVE_METHOD_MATCH_RATE.apply( higher, lower ) > 0;

  // SETTING _AVG
  private static Function<Stream<MatcherRate>, MatcherRate> ALL_AVG_PER_METHOD = matcherRateStream -> {
    double avg = matcherRateStream.filter( Setting::isMatcherRateRelevant )
        .mapToDouble( MatcherRate::getMatcherRating ).average().orElse( Double.NaN );
    if ( avg == Double.NaN ) {
      return null;
    }
    MatcherRate result = new MatcherRate();
    result.add( "Avarage", avg );
    return result;
  };

  public static Function<Stream<MatcherRate>, MatcherRate> QUALITATIVE_COMPONENT_MATCH_RATE_CUMULATION = matcherRateCol -> MIN_MAX_AVG
      .apply( matcherRateCol.collect( Collectors.toList() ) );

  private static Function<Stream<MatcherRate>, MatcherRate> HIGHER_QUALITATIVE_METHOD_MATCH_RATE_CONDITION = matcherRateCol -> matcherRateCol
      .reduce( ( higher,
          lower ) -> higher != null && lower != null
              && MAX_RATE_PER_METHOD.apply( higher, lower ) ? higher : lower )
      .get();

  public static Function<Stream<MatcherRate>, MatcherRate> QUALITATIVE_COMPONENT_METHOD_MATCH_RATE_CUMULATION = matcherRateCol -> ALL_AVG
      .apply( matcherRateCol.collect( Collectors.toList() ) );
}
