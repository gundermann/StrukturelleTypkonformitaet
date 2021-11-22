package de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans;

import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.business.Injured;

public interface FireFighter extends FirstAidProvider {

  public FireState extinguishFire( Fire fire );

  public void free( Injured injured );

  public void provideHeartbeatMassage( Injured injured );

  public void nurseWounds( Injured injured );

  public void stabilizeBrokenBones( Injured injured );

}
