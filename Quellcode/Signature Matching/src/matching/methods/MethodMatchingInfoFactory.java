package matching.methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import matching.MatchingInfo;
import matching.methods.MethodMatchingInfo.ParamPosition;

public final class MethodMatchingInfoFactory {

  private final Method source;

  private final Method target;

  public MethodMatchingInfoFactory( Method target, Method source ) {
    this.target = target;
    this.source = source;
  }

  public MethodMatchingInfo create( MatchingInfo returnTypeMatchingInfo,
      Map<ParamPosition, MatchingInfo> argumentTypeMatchingInfos ) {
    return new MethodMatchingInfo( source, target, returnTypeMatchingInfo, argumentTypeMatchingInfos );
  }

  /**
   * @param returnTypeMatchingInfos
   *          size > 0
   * @param argumentTypesMatchingInfos
   *          size >= 0
   * @return
   */
  public Collection<MethodMatchingInfo> createFromTypeMatchingInfos(
      Collection<MatchingInfo> returnTypeMatchingInfos,
      Collection<Map<ParamPosition, Collection<MatchingInfo>>> argumentTypesMatchingInfos ) {
    Collection<MethodMatchingInfo> methodMatchingInfos = new ArrayList<>();
    for ( MatchingInfo selectedRT : returnTypeMatchingInfos ) {
      Collection<Map<ParamPosition, MatchingInfo>> restructMap = restructureArgumentTypeMatchingInfos(
          argumentTypesMatchingInfos );

      if ( restructMap.isEmpty() ) {
        methodMatchingInfos.add( create( selectedRT, new HashMap<ParamPosition, MatchingInfo>() ) );
      }

      for ( Map<ParamPosition, MatchingInfo> selectedAT : restructMap ) {
        methodMatchingInfos.add( create( selectedRT, selectedAT ) );
      }
    }
    return methodMatchingInfos;
  }

  private Collection<Map<ParamPosition, MatchingInfo>> restructureArgumentTypeMatchingInfos(
      Collection<Map<ParamPosition, Collection<MatchingInfo>>> argumentTypesMatchingInfos ) {
    return argumentTypesMatchingInfos.stream()
        .flatMap( elem -> restructureArgumentTypeMatchingInfosWithParamPosition( elem ).stream() )
        .collect( Collectors.toList() );
  }

  private Collection<Map<ParamPosition, MatchingInfo>> restructureArgumentTypeMatchingInfosWithParamPosition(
      Map<ParamPosition, Collection<MatchingInfo>> argumentTypesMatchingInfos ) {
    Collection<Map<ParamPosition, MatchingInfo>> result = new ArrayList<>();
    Map<ParamPosition, Collection<MatchingInfo>> localInfos = new HashMap<>( argumentTypesMatchingInfos );
    Iterator<ParamPosition> keyIterator = localInfos.keySet().iterator();
    if ( !keyIterator.hasNext() ) {
      return result;
    }
    ParamPosition selectedKey = keyIterator.next();
    Collection<MatchingInfo> selectedArgumentTypeInfos = localInfos.get( selectedKey );
    localInfos.remove( selectedKey );
    if ( selectedArgumentTypeInfos.isEmpty() ) {
      return restructureArgumentTypeMatchingInfosWithParamPosition( localInfos );
    }
    for ( MatchingInfo selectedArgumentTypeInfo : selectedArgumentTypeInfos ) {
      Collection<Map<ParamPosition, MatchingInfo>> otherRestructInfos = restructureArgumentTypeMatchingInfosWithParamPosition(
          localInfos );
      if ( otherRestructInfos.isEmpty() ) {
        Map<ParamPosition, MatchingInfo> singleMap = new HashMap<>();
        singleMap.put( selectedKey, selectedArgumentTypeInfo );
        result.add( singleMap );
        continue;
      }
      for ( Map<ParamPosition, MatchingInfo> otherRestructInfo : otherRestructInfos ) {
        otherRestructInfo.put( selectedKey, selectedArgumentTypeInfo );
      }
      result.addAll( otherRestructInfos );
    }
    return result;

  }

}
