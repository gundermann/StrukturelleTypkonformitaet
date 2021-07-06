package matching.types;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PartlyTypeMatchingInfoFactory {

  private final Class<?> targetType;

  public PartlyTypeMatchingInfoFactory( Class<?> targetType ) {
    this.targetType = targetType;
  }

  public PartlyTypeMatchingInfo create() {
    return this.create( new ArrayList<>(), new HashMap<>(), 0 );
  }

  public PartlyTypeMatchingInfo create( Collection<Method> sourceMethods,
      Map<Method, MatchingSupplier> methodMatchingInfos, int countOfPotentialMethods ) {
    return new PartlyTypeMatchingInfo( targetType, sourceMethods, methodMatchingInfos, countOfPotentialMethods );
  }

}
