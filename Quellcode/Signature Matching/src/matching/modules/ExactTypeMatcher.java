package matching.modules;

import java.util.Collection;
import java.util.Collections;

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
  public double matchesWithRating( Class<?> checkType, Class<?> queryType ) {
    return matchesType( checkType, queryType ) ? MATCHER_BASE_RATING : -1;
  }

}
