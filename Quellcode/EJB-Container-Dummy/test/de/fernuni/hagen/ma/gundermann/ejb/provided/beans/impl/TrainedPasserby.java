package de.fernuni.hagen.ma.gundermann.ejb.provided.beans.impl;

import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.FirstAidTrainedPasserby;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.AccidentParticipant;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.Suffer;

public class TrainedPasserby implements FirstAidTrainedPasserby {
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
  public void provideFirstAid( AccidentParticipant injured ) {
    provideHeartbeatMassage( injured );
    nurseWounds( injured );
    stablilizeBrokenBones( injured );
  }
}
