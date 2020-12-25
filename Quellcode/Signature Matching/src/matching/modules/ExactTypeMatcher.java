package matching.modules;

import java.util.Collection;
import java.util.Collections;

import matching.MatcherRate;

public class ExactTypeMatcher implements TypeMatcher {

  private static final double MATCHER_BASE_RATING = 100d;

  @Override
  public boolean matchesType( Class<?> checkType, Class<?> queryType ) {
    return checkType.equals( queryType );
  }

  @Override
  public Collection<ModuleMatchingInfo> calculateTypeMatchingInfos( Class<?> targetType,
      Class<?> sourceType ) {
    if ( matchesType( targetType, sourceType ) ) {
      return Collections
          .singletonList( new ModuleMatchingInfoFactory( targetType, sourceType ).create() );
    }
    return Collections.emptyList();
  }

  @Override
  public MatcherRate matchesWithRating( Class<?> checkType, Class<?> queryType ) {
	  if(matchesType( checkType, queryType )) {
		  MatcherRate rate = new MatcherRate();
		  rate.add(this.getClass().getSimpleName(), MATCHER_BASE_RATING);
		  return rate;
	  }
	  return null;
  }

}
