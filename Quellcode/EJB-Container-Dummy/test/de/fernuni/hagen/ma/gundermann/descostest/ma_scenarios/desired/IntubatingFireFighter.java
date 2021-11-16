package de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.desired;

import api.RequiredTypeTestReference;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.desired.tests.IntubatingFireFighterTest;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.FireState;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.business.Injured;

@RequiredTypeTestReference( testClasses = IntubatingFireFighterTest.class )
public interface IntubatingFireFighter {

  public void intubate( Injured injured );

  public FireState extinguishFire( Fire fire );
}