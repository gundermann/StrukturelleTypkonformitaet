package matching.methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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

  public MethodMatchingInfo create( ModuleMatchingInfo returnTypeMatchingInfo,
      Map<Integer, ModuleMatchingInfo> argumentTypeMatchingInfos ) {
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
      Collection<ModuleMatchingInfo> returnTypeMatchingInfos,
      Map<Integer, Collection<ModuleMatchingInfo>> argumentTypesMatchingInfos ) {
    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();
    for ( ModuleMatchingInfo<?> selectedRT : returnTypeMatchingInfos ) {
      Collection<Map<Integer, ModuleMatchingInfo>> restructMap = restructureArgumentTypeMatchingInfos(
          argumentTypesMatchingInfos );

      if ( restructMap.isEmpty() ) {
        methodMatchingInfos.add( create( selectedRT, new HashMap<Integer, ModuleMatchingInfo>() ) );
      }

      for ( Map<Integer, ModuleMatchingInfo> selectedAT : restructMap ) {
        methodMatchingInfos.add( create( selectedRT, selectedAT ) );
      }
    }
    return methodMatchingInfos;
  }

  private Collection<Map<Integer, ModuleMatchingInfo>> restructureArgumentTypeMatchingInfos(
      Map<Integer, Collection<ModuleMatchingInfo>> argumentTypesMatchingInfos ) {
    Collection<Map<Integer, ModuleMatchingInfo>> result = new ArrayList<>();
    Map<Integer, Collection<ModuleMatchingInfo>> localInfos = new HashMap<>( argumentTypesMatchingInfos );
    Iterator<Integer> keyIterator = localInfos.keySet().iterator();
    if ( !keyIterator.hasNext() ) {
      return result;
    }
    Integer selectedKey = keyIterator.next();
    Collection<ModuleMatchingInfo> selectedArgumentTypeInfos = localInfos.remove( selectedKey );
    if ( selectedArgumentTypeInfos.isEmpty() ) {
      return restructureArgumentTypeMatchingInfos( localInfos );
    }
    for ( ModuleMatchingInfo selectedArgumentTypeInfo : selectedArgumentTypeInfos ) {
      Collection<Map<Integer, ModuleMatchingInfo>> otherRestructInfos = restructureArgumentTypeMatchingInfos(
          localInfos );
      if ( otherRestructInfos.isEmpty() ) {
        Map<Integer, ModuleMatchingInfo> singleMap = new HashMap<>();
        singleMap.put( selectedKey, selectedArgumentTypeInfo );
        result.add( singleMap );
        continue;
      }
      for ( Map<Integer, ModuleMatchingInfo> otherRestructInfo : otherRestructInfos ) {
        otherRestructInfo.put( selectedKey, selectedArgumentTypeInfo );
      }
      result.addAll( otherRestructInfos );
    }
    return result;

  }

}
