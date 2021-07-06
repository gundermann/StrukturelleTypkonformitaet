package matching.methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.types.TypeMatchingInfo;

public final class MethodMatchingInfoFactory {

  private final Method source;

  private final Method target;

  public MethodMatchingInfoFactory( Method target, Method source ) {
    this.target = target;
    this.source = source;
  }

  public MethodMatchingInfo create( TypeMatchingInfo returnTypeMatchingInfo,
      Map<ParamPosition, TypeMatchingInfo> argumentTypeMatchingInfos ) {
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
      Collection<TypeMatchingInfo> returnTypeMatchingInfos,
      Collection<Map<ParamPosition, Collection<TypeMatchingInfo>>> argumentTypesMatchingInfos ) {
    Collection<MethodMatchingInfo> methodMatchingInfos = new ArrayList<>();
    for ( TypeMatchingInfo selectedRT : returnTypeMatchingInfos ) {
      Collection<Map<ParamPosition, TypeMatchingInfo>> restructMap = restructureArgumentTypeMatchingInfos(
          argumentTypesMatchingInfos );

      if ( restructMap.isEmpty() ) {
        methodMatchingInfos.add( create( selectedRT, new HashMap<ParamPosition, TypeMatchingInfo>() ) );
      }

      for ( Map<ParamPosition, TypeMatchingInfo> selectedAT : restructMap ) {
        methodMatchingInfos.add( create( selectedRT, selectedAT ) );
      }
    }
    return methodMatchingInfos;
  }

  private Collection<Map<ParamPosition, TypeMatchingInfo>> restructureArgumentTypeMatchingInfos(
      Collection<Map<ParamPosition, Collection<TypeMatchingInfo>>> argumentTypesMatchingInfos ) {
    return argumentTypesMatchingInfos.stream()
        .flatMap( elem -> restructureArgumentTypeMatchingInfosWithParamPosition( elem ).stream() )
        .collect( Collectors.toList() );
  }

  private Collection<Map<ParamPosition, TypeMatchingInfo>> restructureArgumentTypeMatchingInfosWithParamPosition(
      Map<ParamPosition, Collection<TypeMatchingInfo>> argumentTypesMatchingInfos ) {
    Collection<Map<ParamPosition, TypeMatchingInfo>> result = new ArrayList<>();
    Map<ParamPosition, Collection<TypeMatchingInfo>> localInfos = new HashMap<>( argumentTypesMatchingInfos );
    Iterator<ParamPosition> keyIterator = localInfos.keySet().iterator();
    if ( !keyIterator.hasNext() ) {
      return result;
    }
    ParamPosition selectedKey = keyIterator.next();
    Collection<TypeMatchingInfo> selectedArgumentTypeInfos = localInfos.get( selectedKey );
    localInfos.remove( selectedKey );
    if ( selectedArgumentTypeInfos.isEmpty() ) {
      return restructureArgumentTypeMatchingInfosWithParamPosition( localInfos );
    }
    for ( TypeMatchingInfo selectedArgumentTypeInfo : selectedArgumentTypeInfos ) {
      Collection<Map<ParamPosition, TypeMatchingInfo>> otherRestructInfos = restructureArgumentTypeMatchingInfosWithParamPosition(
          localInfos );
      if ( otherRestructInfos.isEmpty() ) {
        Map<ParamPosition, TypeMatchingInfo> singleMap = new HashMap<>();
        singleMap.put( selectedKey, selectedArgumentTypeInfo );
        result.add( singleMap );
        continue;
      }
      for ( Map<ParamPosition, TypeMatchingInfo> otherRestructInfo : otherRestructInfos ) {
        otherRestructInfo.put( selectedKey, selectedArgumentTypeInfo );
      }
      result.addAll( otherRestructInfos );
    }
    return result;

  }

}
