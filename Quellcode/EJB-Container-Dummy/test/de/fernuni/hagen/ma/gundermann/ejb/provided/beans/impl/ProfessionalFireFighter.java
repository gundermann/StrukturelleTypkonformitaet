package de.fernuni.hagen.ma.gundermann.ejb.provided.beans.impl;

import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.FireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.ParaMedic;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.AccidentParticipant;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.Suffer;

public class ProfessionalFireFighter implements ParaMedic, FireFighter {

  @Override
  public void extinguishFire( Fire fire ) {
    fire.extinguish();
  }

  @Override
  public void free( AccidentParticipant injured ) {
    injured.healSuffer( Suffer.LOCKED );
  }

  @Override
  public void provideHeartbeatMassage( AccidentParticipant injured ) {
    injured.healSuffer( Suffer.NO_HEATBEAT );
  }

  @Override
  public void nurseWounds( AccidentParticipant injured ) {
    injured.healSuffer( Suffer.OPEN_WOUND );
  }

  @Override
  public void stabilizeBrokenBones( AccidentParticipant injured ) {
    injured.healSuffer( Suffer.BROKEN_BONE );
  }

  @Override
  public void placeInfusion( AccidentParticipant injured ) {
    injured.healSuffer( Suffer.NUTRITIONAL_DEFICIENCY );
  }

  @Override
  public void provideFirstAid( AccidentParticipant injured ) {
    free( injured );
    provideHeartbeatMassage( injured );
    placeInfusion( injured );
    nurseWounds( injured );
    stabilizeBrokenBones( injured );
  }

}
