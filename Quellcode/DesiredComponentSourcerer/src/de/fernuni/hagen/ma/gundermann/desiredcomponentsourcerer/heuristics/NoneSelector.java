package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.util.Optional;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.CombinationInfo;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.Selector;
import matching.methods.MethodMatchingInfo;

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

  @Override
  public void addToBlacklist( MethodMatchingInfo methodMatchingInfo ) {
    // do nothing

  }

  @Override
  public void addToBlacklist( Class<?> componentInterface ) {
    // do nothing

  }

}