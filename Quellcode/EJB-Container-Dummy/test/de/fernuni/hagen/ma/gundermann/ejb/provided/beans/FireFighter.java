package de.fernuni.hagen.ma.gundermann.ejb.provided.beans;

import de.fernuni.hagen.ma.gundermann.ejb.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.Patient;

public interface FireFighter {

  public void extinguishFire( Fire fire );

  public void provideFirstAid( Patient patient );

}
