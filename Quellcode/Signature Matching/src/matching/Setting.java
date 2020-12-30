package matching;

import java.util.Collection;
import java.util.Optional;
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

  public static double EXACT_TYPE_MATCHER_RATING = 100d;

  public static double GEN_SPEC_TYPE_MATCHER_BASE_RATING = 200d;

  public static double STRUCTURAL_TYPE_MATCHER_BASE_RATING = 1000d;

  public static double WRAPPEN_TYPE_MATCHER_BASE_RATING = 600d;

  public static double PARAM_PERM_METHOD_TYPE_MATCHER_BASE_RATING = 0d;

  public static Function<Stream<MatcherRate>, MatcherRate> QUALITATIVE_MATCHER_RATE_CUMULATION = matcherRateCol -> MIN_MAX_AVG
      .apply( matcherRateCol.collect( Collectors.toList() ) );

}
