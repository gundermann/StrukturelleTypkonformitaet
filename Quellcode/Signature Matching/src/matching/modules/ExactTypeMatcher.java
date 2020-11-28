package matching.modules;

import java.util.Collection;
import java.util.Collections;

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

}
