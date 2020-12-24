package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.AccidentParticipant;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Fire;

public interface FireFighter extends FirstAidProvider {

  public void extinguishFire( Fire fire );

  public void free( AccidentParticipant injured );

  public void provideHeartbeatMassage( AccidentParticipant injured );

  public void nurseWounds( AccidentParticipant injured );

  public void stabilizeBrokenBones( AccidentParticipant injured );

}
