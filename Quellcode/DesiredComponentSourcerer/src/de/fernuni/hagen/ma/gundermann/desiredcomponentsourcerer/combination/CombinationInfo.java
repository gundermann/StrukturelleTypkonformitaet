package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.CollectionUtil;
import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;

public class CombinationInfo {

  private final Map<Class<?>, Collection<MethodMatchingInfo>> component2MatchingInfo;

  public CombinationInfo( Collection<CombinationPartInfo> combiPartInfos ) {
    this.component2MatchingInfo = new HashMap<>();
    for ( CombinationPartInfo cpi : combiPartInfos ) {
      Class<?> componentClass = cpi.getComponentClass();
      MethodMatchingInfo matchingInfo = cpi.getMatchingInfo();
      this.component2MatchingInfo.compute( componentClass,
          CollectionUtil.remapping_addToValueCollection( matchingInfo ) );
    }
  }

  public CombinationInfo( Map<Class<?>, Collection<MethodMatchingInfo>> component2MatchingInfo ) {
    this.component2MatchingInfo = component2MatchingInfo;
  }

  public Collection<Class<?>> getComponentClasses() {
    return component2MatchingInfo.keySet();
  }

  public Collection<MethodMatchingInfo> getModuleMatchingInfo( Class<?> componentClass ) {
    return component2MatchingInfo.get( componentClass );
  }

}
