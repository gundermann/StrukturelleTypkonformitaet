package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.Map;

import matching.modules.PartlyTypeMatchingInfo;

public class BestMatchingComponentCombinationFinder {

  private final Map<Class<?>, PartlyTypeMatchingInfo> compnentInterface2PartlyMatchingInfos;

  BestMatchingComponentCombinationFinder(
      Map<Class<?>, PartlyTypeMatchingInfo> compnentInterface2PartlyMatchingInfos ) {
    this.compnentInterface2PartlyMatchingInfos = compnentInterface2PartlyMatchingInfos;
  }

  public boolean hasNextCombination() {
    // TODO Auto-generated method stub
    return false;
  }

  public CombinationInfo getNextCombination() {
    // TODO Auto-generated method stub
    return null;
  }

}
