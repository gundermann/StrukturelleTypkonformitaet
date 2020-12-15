package de.fernuni.hagen.ma.gundermann.ejb.provided.beans;

import de.fernuni.hagen.ma.gundermann.ejb.provided.business.AccidentParticipant;

public interface ParaMedic extends FirstAidProvider {

  public void provideHeartbeatMassage( AccidentParticipant injured );

  public void nurseWounds( AccidentParticipant injured );

  public void stabilizeBrokenBones( AccidentParticipant injured );

  public void placeInfusion( AccidentParticipant injured );

}
