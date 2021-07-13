package matching.types;

import java.util.Collection;
import java.util.Collections;

import de.fernuni.hagen.ma.gundermann.signaturematching.SingleMatchingInfo;
import glue.ProxyCreatorFactories;
import matching.Setting;

public class ExactTypeMatcher implements CombinableTypeMatcher {

  @Override
  public boolean matchesType( Class<?> checkType, Class<?> queryType ) {
    return checkType.equals( queryType );
  }

  @Override
  public Collection<SingleMatchingInfo> calculateTypeMatchingInfos( Class<?> targetType,
      Class<?> sourceType ) {
    if ( matchesType( targetType, sourceType ) ) {
      return Collections
          .singletonList( new SingleMatchingInfo.Builder(sourceType, targetType, ProxyCreatorFactories.getIdentityFactoryCreator()).build()
        		  
//        		  new TypeMatchingInfoFactory( targetType, sourceType ).create() 
        		  );
    }
    return Collections.emptyList();
  }

  @Override
  public double getTypeMatcherRate() {
    return Setting.EXACT_TYPE_MATCHER_RATING;
  }

}
