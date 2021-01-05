package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.Optional;

/**
 * Selektor, der gar keine Komponente selektiert
 */
public class NoneSelector implements Selector {

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public Optional<CombinationInfo> getNext() {
    return Optional.empty();
  }

  @Override
  public void addHigherPotentialType( Class<?> higherPotentialType ) {
    // do nothing

  }

}
