package de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.signaturematching.SingleMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo.ParamPosition;

public final class MethodMatchingInfoFactory {

  private final Method source;

  private final Method target;

  public MethodMatchingInfoFactory( Method target, Method source ) {
    this.target = target;
    this.source = source;
  }

  public MethodMatchingInfo create( SingleMatchingInfo returnTypeMatchingInfo,
      Map<ParamPosition, SingleMatchingInfo> argumentTypeMatchingInfos ) {
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
      Collection<SingleMatchingInfo> returnTypeMatchingInfos,
      Collection<Map<ParamPosition, Collection<SingleMatchingInfo>>> argumentTypesMatchingInfos ) {
    Collection<MethodMatchingInfo> methodMatchingInfos = new ArrayList<>();
    for ( SingleMatchingInfo selectedRT : returnTypeMatchingInfos ) {
      Collection<Map<ParamPosition, SingleMatchingInfo>> restructMap = restructureArgumentTypeMatchingInfos(
          argumentTypesMatchingInfos );

      if ( restructMap.isEmpty() ) {
        methodMatchingInfos.add( create( selectedRT, new HashMap<ParamPosition, SingleMatchingInfo>() ) );
      }

      for ( Map<ParamPosition, SingleMatchingInfo> selectedAT : restructMap ) {
        methodMatchingInfos.add( create( selectedRT, selectedAT ) );
      }
    }
    return methodMatchingInfos;
  }

  private Collection<Map<ParamPosition, SingleMatchingInfo>> restructureArgumentTypeMatchingInfos(
      Collection<Map<ParamPosition, Collection<SingleMatchingInfo>>> argumentTypesMatchingInfos ) {
    return argumentTypesMatchingInfos.stream()
        .flatMap( elem -> restructureArgumentTypeMatchingInfosWithParamPosition( elem ).stream() )
        .collect( Collectors.toList() );
  }

  private Collection<Map<ParamPosition, SingleMatchingInfo>> restructureArgumentTypeMatchingInfosWithParamPosition(
      Map<ParamPosition, Collection<SingleMatchingInfo>> argumentTypesMatchingInfos ) {
    Collection<Map<ParamPosition, SingleMatchingInfo>> result = new ArrayList<>();
    Map<ParamPosition, Collection<SingleMatchingInfo>> localInfos = new HashMap<>( argumentTypesMatchingInfos );
    Iterator<ParamPosition> keyIterator = localInfos.keySet().iterator();
    if ( !keyIterator.hasNext() ) {
      return result;
    }
    ParamPosition selectedKey = keyIterator.next();
    Collection<SingleMatchingInfo> selectedArgumentTypeInfos = localInfos.get( selectedKey );
    localInfos.remove( selectedKey );
    if ( selectedArgumentTypeInfos.isEmpty() ) {
      return restructureArgumentTypeMatchingInfosWithParamPosition( localInfos );
    }
    for ( SingleMatchingInfo selectedArgumentTypeInfo : selectedArgumentTypeInfos ) {
      Collection<Map<ParamPosition, SingleMatchingInfo>> otherRestructInfos = restructureArgumentTypeMatchingInfosWithParamPosition(
          localInfos );
      if ( otherRestructInfos.isEmpty() ) {
        Map<ParamPosition, SingleMatchingInfo> singleMap = new HashMap<>();
        singleMap.put( selectedKey, selectedArgumentTypeInfo );
        result.add( singleMap );
        continue;
      }
      for ( Map<ParamPosition, SingleMatchingInfo> otherRestructInfo : otherRestructInfos ) {
        otherRestructInfo.put( selectedKey, selectedArgumentTypeInfo );
      }
      result.addAll( otherRestructInfos );
    }
    return result;

  }

}
