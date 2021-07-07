package matching.types;

import java.util.Collection;
import java.util.Collections;

import matching.Setting;

public class ExactTypeMatcher implements CombinableTypeMatcher {

  @Override
  public boolean matchesType( Class<?> checkType, Class<?> queryType ) {
    return checkType.equals( queryType );
  }

  @Override
  public Collection<TypeMatchingInfo> calculateTypeMatchingInfos( Class<?> targetType,
      Class<?> sourceType ) {
    if ( matchesType( targetType, sourceType ) ) {
      return Collections
          .singletonList( new TypeMatchingInfoFactory( targetType, sourceType ).create() );
    }
    return Collections.emptyList();
  }

  @Override
  public double getTypeMatcherRate() {
    return Setting.EXACT_TYPE_MATCHER_RATING;
  }

}