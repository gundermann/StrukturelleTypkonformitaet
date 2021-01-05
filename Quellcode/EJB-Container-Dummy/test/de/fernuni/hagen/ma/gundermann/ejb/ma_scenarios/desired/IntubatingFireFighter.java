package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.tests.IntubatingFireFighterTest;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.AccidentParticipant;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Fire;
import tester.annotation.QueryTypeTestReference;

@QueryTypeTestReference( testClasses = IntubatingFireFighterTest.class )
public interface IntubatingFireFighter {

  public void intubate( AccidentParticipant injured );

  public void extinguishFire( Fire fire );
}
