package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination;

import java.lang.reflect.Method;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;

public class CombinationPartInfo {

  private final Class<?> componentClass;

  private final Method sourceMethod;

  private final MethodMatchingInfo matchingInfo;

  public CombinationPartInfo( Class<?> componentClass, Method sourceMethod,
      MethodMatchingInfo matchingInfo ) {
    this.componentClass = componentClass;
    this.sourceMethod = sourceMethod;
    this.matchingInfo = matchingInfo;
  }

  public Class<?> getComponentClass() {
    return componentClass;
  }

  public Method getSourceMethod() {
    return sourceMethod;
  }

  public MethodMatchingInfo getMatchingInfo() {
    return matchingInfo;
  }

}
