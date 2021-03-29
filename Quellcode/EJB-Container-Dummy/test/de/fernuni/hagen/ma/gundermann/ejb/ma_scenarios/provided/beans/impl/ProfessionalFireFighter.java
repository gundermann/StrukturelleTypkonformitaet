package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.impl;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.FireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.ParaMedic;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Injured;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Suffer;

public class ProfessionalFireFighter implements ParaMedic, FireFighter {

  @Override
  public void extinguishFire( Fire fire ) {
    fire.extinguish();
  }

  @Override
  public void free( Injured injured ) {
    injured.healSuffer( Suffer.LOCKED );
  }

  @Override
  public void provideHeartbeatMassage( Injured injured ) {
    injured.healSuffer( Suffer.NO_HEATBEAT );
  }

  @Override
  public void nurseWounds( Injured injured ) {
    injured.healSuffer( Suffer.OPEN_WOUND );
  }

  @Override
  public void stabilizeBrokenBones( Injured injured ) {
    injured.healSuffer( Suffer.BROKEN_BONE );
  }

  @Override
  public void placeInfusion( Injured injured ) {
    injured.healSuffer( Suffer.NUTRITIONAL_DEFICIENCY );
  }

  @Override
  public void provideFirstAid( Injured injured ) {
    free( injured );
    provideHeartbeatMassage( injured );
    placeInfusion( injured );
    nurseWounds( injured );
    stabilizeBrokenBones( injured );
  }

}
