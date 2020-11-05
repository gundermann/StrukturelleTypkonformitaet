package matching.methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.modules.ModuleMatchingInfo;

public final class MethodMatchingInfoFactory {

  private final Method source;

  private final Method target;

  public MethodMatchingInfoFactory( Method target, Method source ) {
    this.target = target;
    this.source = source;
  }

  public MethodMatchingInfo create( ModuleMatchingInfo returnTypeMatchingInfo,
      Map<ParamPosition, ModuleMatchingInfo> argumentTypeMatchingInfos ) {
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
      Collection<Map<ParamPosition, Collection<ModuleMatchingInfo>>> argumentTypesMatchingInfos ) {
    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();
    for ( ModuleMatchingInfo selectedRT : returnTypeMatchingInfos ) {
      Collection<Map<ParamPosition, ModuleMatchingInfo>> restructMap = restructureArgumentTypeMatchingInfos(
          argumentTypesMatchingInfos );

      if ( restructMap.isEmpty() ) {
        methodMatchingInfos.add( create( selectedRT, new HashMap<ParamPosition, ModuleMatchingInfo>() ) );
      }

      for ( Map<ParamPosition, ModuleMatchingInfo> selectedAT : restructMap ) {
        methodMatchingInfos.add( create( selectedRT, selectedAT ) );
      }
    }
    return methodMatchingInfos;
  }

  private Collection<Map<ParamPosition, ModuleMatchingInfo>> restructureArgumentTypeMatchingInfos(
      Collection<Map<ParamPosition, Collection<ModuleMatchingInfo>>> argumentTypesMatchingInfos ) {
    return argumentTypesMatchingInfos.stream()
        .flatMap( elem -> restructureArgumentTypeMatchingInfosWithParamPosition( elem ).stream() )
        .collect( Collectors.toList() );
  }

  private Collection<Map<ParamPosition, ModuleMatchingInfo>> restructureArgumentTypeMatchingInfosWithParamPosition(
      Map<ParamPosition, Collection<ModuleMatchingInfo>> argumentTypesMatchingInfos ) {
    Collection<Map<ParamPosition, ModuleMatchingInfo>> result = new ArrayList<>();
    Map<ParamPosition, Collection<ModuleMatchingInfo>> localInfos = new HashMap<>( argumentTypesMatchingInfos );
    Iterator<ParamPosition> keyIterator = localInfos.keySet().iterator();
    if ( !keyIterator.hasNext() ) {
      return result;
    }
    ParamPosition selectedKey = keyIterator.next();
    Collection<ModuleMatchingInfo> selectedArgumentTypeInfos = localInfos.remove( selectedKey );
    if ( selectedArgumentTypeInfos.isEmpty() ) {
      return restructureArgumentTypeMatchingInfosWithParamPosition( localInfos );
    }
    for ( ModuleMatchingInfo selectedArgumentTypeInfo : selectedArgumentTypeInfos ) {
      Collection<Map<ParamPosition, ModuleMatchingInfo>> otherRestructInfos = restructureArgumentTypeMatchingInfosWithParamPosition(
          localInfos );
      if ( otherRestructInfos.isEmpty() ) {
        Map<ParamPosition, ModuleMatchingInfo> singleMap = new HashMap<>();
        singleMap.put( selectedKey, selectedArgumentTypeInfo );
        result.add( singleMap );
        continue;
      }
      for ( Map<ParamPosition, ModuleMatchingInfo> otherRestructInfo : otherRestructInfos ) {
        otherRestructInfo.put( selectedKey, selectedArgumentTypeInfo );
      }
      result.addAll( otherRestructInfos );
    }
    return result;

  }

  private Set<ParamPosition> generateDefaultParamPosition( Set<Integer> paramIdices ) {
    Set<ParamPosition> paramPositions = new HashSet<>();
    for ( Integer index : paramIdices ) {
      paramPositions.add( new ParamPosition( index, index ) );
    }
    return paramPositions;
  }
}
