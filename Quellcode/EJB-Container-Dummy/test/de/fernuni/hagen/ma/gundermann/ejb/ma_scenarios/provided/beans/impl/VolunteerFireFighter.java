package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.impl;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.FireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.AccidentParticipant;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Suffer;

public class VolunteerFireFighter implements FireFighter {

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
  public void provideFirstAid( AccidentParticipant injured ) {
    free( injured );
    provideHeartbeatMassage( injured );
    nurseWounds( injured );
    stabilizeBrokenBones( injured );
  }

}
