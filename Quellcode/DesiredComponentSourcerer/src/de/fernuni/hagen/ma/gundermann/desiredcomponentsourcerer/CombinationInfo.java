package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.Collection;
import java.util.Map;

import matching.methods.MethodMatchingInfo;

public class CombinationInfo {

  private final Map<Class<?>, Collection<MethodMatchingInfo>> component2MatchingInfo;

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
