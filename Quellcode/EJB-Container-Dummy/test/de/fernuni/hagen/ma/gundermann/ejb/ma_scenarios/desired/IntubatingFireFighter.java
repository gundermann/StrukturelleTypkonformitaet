package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.tests.IntubatingFireFighterTest;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.FireState;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Injured;
import tester.annotation.RequiredTypeTestReference;

@RequiredTypeTestReference( testClasses = IntubatingFireFighterTest.class )
public interface IntubatingFireFighter {

  public void intubate( Injured injured );

  public FireState extinguishFire( Fire fire );
}
