package de.fernuni.hagen.ma.gundermann.ejb.provided.beans.impl;

import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.FireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.Patient;

public class VolunteerFireFighter implements FireFighter {

  @Override
  public void extinguishFire( Fire fire ) {
    fire.extinguish();
  }

  @Override
  public void provideFirstAid( Patient patient ) {
    patient.setFirstAidProvided( true );
  }

}
