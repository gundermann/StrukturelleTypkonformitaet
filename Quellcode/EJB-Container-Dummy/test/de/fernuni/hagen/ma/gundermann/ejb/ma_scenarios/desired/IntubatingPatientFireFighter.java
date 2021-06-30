package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired;

import api.RequiredTypeTestReference;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.tests.IntubatingPatientFireFighterTest;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.FireState;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.IntubationPartient;

@RequiredTypeTestReference( testClasses = IntubatingPatientFireFighterTest.class )
public interface IntubatingPatientFireFighter {

  public void intubate( IntubationPartient patient );

  public FireState extinguishFire( Fire fire );
}
