package matching;

import java.util.function.Function;
import java.util.stream.Stream;

public interface Setting {

  public static double EXACT_TYPE_MATCHER_RATING = 100d;

  public static double GEN_SPEC_TYPE_MATCHER_BASE_RATING = 200d;

  public static double STRUCTURAL_TYPE_MATCHER_BASE_RATING = 400d;

  public static double WRAPPEN_TYPE_MATCHER_BASE_RATING = 300d;

  public static double PARAM_PERM_METHOD_TYPE_MATCHER_BASE_RATING = 600d;

  public static Function<Stream<MatcherRate>, MatcherRate> QUALITATIVE_MATCHER_RATE_CUMULATION = matcherRateStream -> matcherRateStream
      .max( MatcherRate::compare )
      .orElse( null );

}
