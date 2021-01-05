package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.Optional;

public interface Selector {

  boolean hasNext();

  Optional<CombinationInfo> getNext();

  void addHigherPotentialType( Class<?> higherPotentialType );

}
