package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.Collection;
import java.util.Optional;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.CombinationInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;

public interface Selector {

  boolean hasNext();

  Optional<CombinationInfo> getNext();

  void addHigherPotentialType( Class<?> higherPotentialType );

  void addToBlacklist( Collection<Collection<MethodMatchingInfo>> mmiCombis );

  void addToBlacklist( Class<?> componentInterface );

}
