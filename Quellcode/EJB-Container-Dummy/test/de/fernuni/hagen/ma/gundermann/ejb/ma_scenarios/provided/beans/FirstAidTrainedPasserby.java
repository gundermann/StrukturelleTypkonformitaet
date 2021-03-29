package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Injured;

public interface FirstAidTrainedPasserby extends FirstAidProvider {

  public void provideHeartbeatMassage( Injured injured );

  public void nurseWounds( Injured injured );

  public void stablilizeBrokenBones( Injured injured );

}
