package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import matching.modules.ModuleMatchingInfo;

class DesiredComponentInfos {

  private final Map<Class<?>, ModuleMatchingInfo> componentClass2MatchingInfo;

  DesiredComponentInfos( Map<Class<?>, ModuleMatchingInfo> componentClass2MatchingInfo ) {
    this.componentClass2MatchingInfo = componentClass2MatchingInfo;
  }

  DesiredComponentInfos( Class<?> componentClass, ModuleMatchingInfo matchingInfo ) {
    this.componentClass2MatchingInfo = new HashMap<>();
    componentClass2MatchingInfo.put( componentClass, matchingInfo );
  }

  ModuleMatchingInfo getMatchingInfos( Class<?> componentClass ) {
    return componentClass2MatchingInfo.get( componentClass );
  }

  Collection<Class<?>> getComponentClasses() {
    return componentClass2MatchingInfo.keySet();
  }

  String getName() {
    return componentClass2MatchingInfo.keySet().stream().map( Class::getSimpleName )
        .collect( Collectors.joining( "+" ) );
  }

}
