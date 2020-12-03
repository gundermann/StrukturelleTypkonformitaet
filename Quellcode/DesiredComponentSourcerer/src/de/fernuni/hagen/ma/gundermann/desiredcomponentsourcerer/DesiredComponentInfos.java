package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import matching.modules.ModuleMatchingInfo;

public class DesiredComponentInfos {

  private final Map<Class<?>, ModuleMatchingInfo> componentClass2MatchingInfo;

  public DesiredComponentInfos( Map<Class<?>, ModuleMatchingInfo> componentClass2MatchingInfo ) {
    this.componentClass2MatchingInfo = componentClass2MatchingInfo;
  }

  public ModuleMatchingInfo getMatchingInfos( Class<?> componentClass ) {
    return componentClass2MatchingInfo.get( componentClass );
  }

  public Collection<Class<?>> getComponentClasses() {
    return componentClass2MatchingInfo.keySet();
  }

  public String getName() {
    return componentClass2MatchingInfo.keySet().stream().map( Class::getSimpleName )
        .collect( Collectors.joining( "+" ) );
  }

}
