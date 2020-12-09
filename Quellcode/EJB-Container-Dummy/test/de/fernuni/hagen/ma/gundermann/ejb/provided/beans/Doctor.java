package de.fernuni.hagen.ma.gundermann.ejb.provided.beans;

import de.fernuni.hagen.ma.gundermann.ejb.provided.business.Patient;

public interface Doctor {

  public void intubate( Patient patient );

  public void provideFirstAid( Patient patient );

}
