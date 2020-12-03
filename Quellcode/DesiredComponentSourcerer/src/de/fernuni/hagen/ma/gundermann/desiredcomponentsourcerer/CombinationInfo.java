package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.Collection;
import java.util.Map;

import matching.modules.ModuleMatchingInfo;

public class CombinationInfo {

  private final Map<Class<?>, ModuleMatchingInfo> component2MatchingInfo;

  public CombinationInfo( Map<Class<?>, ModuleMatchingInfo> component2MatchingInfo ) {
    this.component2MatchingInfo = component2MatchingInfo;
  }

  public Collection<Class<?>> getComponentClasses() {
    return component2MatchingInfo.keySet();
  }

  public ModuleMatchingInfo getModuleMatchingInfo( Class<?> componentClass ) {
    return component2MatchingInfo.get( componentClass );
  }

}
