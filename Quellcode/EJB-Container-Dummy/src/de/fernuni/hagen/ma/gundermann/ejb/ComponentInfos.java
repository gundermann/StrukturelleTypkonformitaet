package de.fernuni.hagen.ma.gundermann.ejb;

import java.util.HashSet;
import java.util.Set;

import matching.modules.ModuleMatchingInfo;

public class ComponentInfos<S> {

  private Class<?> componentClass;

  private Set<ModuleMatchingInfo<S>> matchingInfos = new HashSet<>();

  public ComponentInfos( Class<?> componentClass ) {
    this.componentClass = componentClass;
  }

  int getRank() {
    return matchingInfos.stream().map( ModuleMatchingInfo::getRating ).max( Integer::compare ).orElse( 0 );
  }

  public void setModuleMatchingInfos( Set<ModuleMatchingInfo<S>> matchingInfos ) {
    this.matchingInfos.clear();
    this.matchingInfos.addAll( matchingInfos );

  }

  public Set<ModuleMatchingInfo<S>> getMatchingInfos() {
    return matchingInfos;
  }

  public Class<?> getComponentClass() {
    return componentClass;
  }

}
