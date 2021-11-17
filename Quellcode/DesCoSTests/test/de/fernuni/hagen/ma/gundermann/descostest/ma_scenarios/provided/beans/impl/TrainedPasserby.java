package de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.impl;

import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.FirstAidTrainedPasserby;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.business.Injured;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.business.Suffer;

public class TrainedPasserby implements FirstAidTrainedPasserby {
  @Override
  public void provideHeartbeatMassage( Injured injured ) {
    injured.healSuffer( Suffer.NO_HEATBEAT );
  }

  @Override
  public void nurseWounds( Injured injured ) {
    injured.healSuffer( Suffer.OPEN_WOUND );
  }

  @Override
  public void stablilizeBrokenBones( Injured injured ) {
    injured.healSuffer( Suffer.BROKEN_BONE );
  }

  @Override
  public void provideFirstAid( Injured injured ) {
    provideHeartbeatMassage( injured );
    nurseWounds( injured );
    stablilizeBrokenBones( injured );
  }
}
