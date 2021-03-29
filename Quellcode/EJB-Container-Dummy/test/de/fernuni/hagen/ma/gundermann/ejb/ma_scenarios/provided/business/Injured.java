package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business;

import java.util.ArrayList;
import java.util.Collection;

public class Injured {

  private final Collection<Suffer> suffers;

  public Injured( Collection<Suffer> suffers ) {
    this.suffers = new ArrayList<>( suffers );
  }

  public Collection<Suffer> getSuffers() {
    return suffers;
  }

  public void healSuffer( Suffer suffer ) {
    suffers.remove( suffer );
  }

  public boolean isStabilized() {
    return suffers.isEmpty();
  }
}
