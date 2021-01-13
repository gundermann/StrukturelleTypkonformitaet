package matching.modules;

import matching.MatcherRate;

public interface CombinableTypeMatcher extends TypeMatcher {

  double getTypeMatcherRate();

  @Override
  default MatcherRate matchesWithRating( Class<?> checkType, Class<?> queryType ) {
    if ( matchesType( checkType, queryType ) ) {
      MatcherRate rate = new MatcherRate();
      rate.add( this.getClass().getSimpleName(), getTypeMatcherRate() );
      return rate;
    }
    return null;
  }
}