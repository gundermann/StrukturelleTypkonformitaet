package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.AccidentParticipant;

public interface Doctor extends FirstAidProvider {

  public void provideHeartbeatMassage( AccidentParticipant injured );

  public void nurseWounds( AccidentParticipant injured );

  public void stablilizeBrokenBones( AccidentParticipant injured );

  public void placeInfusion( AccidentParticipant injured );

  public void intubate( AccidentParticipant injured );
}
