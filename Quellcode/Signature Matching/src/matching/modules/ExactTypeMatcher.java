package matching.modules;

import java.util.Collection;
import java.util.Collections;

import matching.MatcherRate;
import matching.Setting;

public class ExactTypeMatcher implements TypeMatcher {

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
    if ( matchesType( checkType, queryType ) ) {
      MatcherRate rate = new MatcherRate();
      rate.add( this.getClass().getSimpleName(), Setting.EXACT_TYPE_MATCHER_RATING );
      return rate;
    }
    return null;
  }

}
