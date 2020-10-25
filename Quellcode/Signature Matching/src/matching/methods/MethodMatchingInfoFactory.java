package matching.methods;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import matching.types.TypeMatchingInfo;

public final class MethodMatchingInfoFactory {

  private final Method source;

  private final Method target;

  public MethodMatchingInfoFactory( Method source, Method target ) {
    this.source = source;
    this.target = target;
  }

  public MethodMatchingInfo create( TypeMatchingInfo<?, ?> returnTypeMatchingInfo,
      Map<Integer, TypeMatchingInfo<?, ?>> argumentTypeMatchingInfos ) {
    return new MethodMatchingInfo( source, target, returnTypeMatchingInfo, argumentTypeMatchingInfos );
  }

  /**
   * @param returnTypeMatchingInfos
   *          size > 0
   * @param argumentTypesMatchingInfos
   *          size >= 0
   * @return
   */
  public Set<MethodMatchingInfo> createFromTypeMatchingInfos(
      Collection<TypeMatchingInfo<?, ?>> returnTypeMatchingInfos,
      Collection<Map<Integer, TypeMatchingInfo<?, ?>>> argumentTypesMatchingInfos ) {
    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();
    for ( TypeMatchingInfo<?, ?> selectedRT : returnTypeMatchingInfos ) {
      if ( argumentTypesMatchingInfos.isEmpty() ) {
        methodMatchingInfos.add( create( selectedRT, new HashMap<Integer, TypeMatchingInfo<?, ?>>() ) );
      }
      for ( Map<Integer, TypeMatchingInfo<?, ?>> selectedAT : argumentTypesMatchingInfos ) {
        methodMatchingInfos.add( create( selectedRT, selectedAT ) );
      }
    }
    return methodMatchingInfos;
  }

}
