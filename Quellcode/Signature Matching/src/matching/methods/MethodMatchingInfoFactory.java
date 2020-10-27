package matching.methods;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import matching.modules.ModuleMatchingInfo;

public final class MethodMatchingInfoFactory {

  private final Method source;

  private final Method target;

  public MethodMatchingInfoFactory( Method target, Method source ) {
    this.target = target;
    this.source = source;
  }

  public MethodMatchingInfo create( ModuleMatchingInfo<?> returnTypeMatchingInfo,
      Map<Integer, ModuleMatchingInfo<?>> argumentTypeMatchingInfos ) {
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
      Collection<ModuleMatchingInfo<?>> returnTypeMatchingInfos,
      Collection<Map<Integer, ModuleMatchingInfo<?>>> argumentTypesMatchingInfos ) {
    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();
    for ( ModuleMatchingInfo<?> selectedRT : returnTypeMatchingInfos ) {
      if ( argumentTypesMatchingInfos.isEmpty() ) {
        methodMatchingInfos.add( create( selectedRT, new HashMap<Integer, ModuleMatchingInfo<?>>() ) );
      }
      for ( Map<Integer, ModuleMatchingInfo<?>> selectedAT : argumentTypesMatchingInfos ) {
        methodMatchingInfos.add( create( selectedRT, selectedAT ) );
      }
    }
    return methodMatchingInfos;
  }

}
