package de.fernuni.hagen.ma.gundermann.ejb.provided.beans.impl;

import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.Doctor;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.Patient;

public class EmergencyDoctor implements Doctor {

  @Override
  public void intubate( Patient patient ) {
    patient.setIntubated( true );
  }

  @Override
  public void provideFirstAid( Patient patient ) {
    patient.setFirstAidProvided( true );
  }

}
