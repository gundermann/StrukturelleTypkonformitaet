package de.fernuni.hagen.ma.gundermann.ejb.provided.beans.impl;

import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.Doctor;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.Intubator;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.AccidentParticipant;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.IntubationPartient;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.Suffer;

public class EmergencyDoctor implements Doctor, Intubator {

  @Override
  public void provideHeartbeatMassage( AccidentParticipant injured ) {
    injured.healSuffer( Suffer.NO_HEATBEAT );
  }

  @Override
  public void nurseWounds( AccidentParticipant injured ) {
    injured.healSuffer( Suffer.OPEN_WOUND );
  }

  @Override
  public void stablilizeBrokenBones( AccidentParticipant injured ) {
    injured.healSuffer( Suffer.BROKEN_BONE );
  }

  @Override
  public void placeInfusion( AccidentParticipant injured ) {
    injured.healSuffer( Suffer.NUTRITIONAL_DEFICIENCY );
  }

  @Override
  public void intubate( AccidentParticipant injured ) {
    injured.healSuffer( Suffer.BREATH_PROBLEMS );
  }

  @Override
  public void provideFirstAid( AccidentParticipant injured ) {
    provideHeartbeatMassage( injured );
    intubate( injured );
    placeInfusion( injured );
    nurseWounds( injured );
    stablilizeBrokenBones( injured );
  }

  @Override
  public void intubate( IntubationPartient patient ) {
    patient.setIntubated( true );
  }

}
