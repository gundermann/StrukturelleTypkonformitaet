package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.Optional;

public interface Selector {

  Optional<CombinationInfo> getNext();

  boolean hasNext();

}
