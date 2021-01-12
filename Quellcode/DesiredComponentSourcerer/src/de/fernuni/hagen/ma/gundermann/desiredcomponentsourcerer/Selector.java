package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.Optional;

import matching.methods.MethodMatchingInfo;

public interface Selector {

  boolean hasNext();

  Optional<CombinationInfo> getNext();

  void addHigherPotentialType( Class<?> higherPotentialType );

  void addToBlacklist( MethodMatchingInfo methodMatchingInfo );

  void addToBlacklist( Class<?> componentInterface );

}
