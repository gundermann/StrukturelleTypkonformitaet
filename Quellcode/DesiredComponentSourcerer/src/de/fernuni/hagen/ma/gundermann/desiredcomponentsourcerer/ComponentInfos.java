package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.HashSet;
import java.util.Set;

import matching.modules.ModuleMatchingInfo;

public class ComponentInfos {

  private Class<?> componentClass;

  private Set<ModuleMatchingInfo> matchingInfos = new HashSet<>();

  private String contexts;

  public ComponentInfos( Class<?> componentClass ) {
    this.componentClass = componentClass;
  }

  int getRank() {
    return matchingInfos.stream().map( ModuleMatchingInfo::getRating ).max( Integer::compare ).orElse( 0 );
  }

  public void setModuleMatchingInfos( Set<ModuleMatchingInfo> matchingInfos ) {
    this.matchingInfos.clear();
    this.matchingInfos.addAll( matchingInfos );
  }

  public void addContext( String context ) {
    if ( this.contexts == null ) {
      this.contexts = context;
    }
    else {
      this.contexts += "+" + context;
    }
  }

  public Set<ModuleMatchingInfo> getMatchingInfos() {
    return matchingInfos;
  }

  public Class<?> getComponentClass() {
    return componentClass;
  }

  public String getName() {
    return this.contexts;
  }

}
