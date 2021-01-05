package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.IntubationPartient;

public interface Intubator {

  public void intubate( IntubationPartient patient );
}
