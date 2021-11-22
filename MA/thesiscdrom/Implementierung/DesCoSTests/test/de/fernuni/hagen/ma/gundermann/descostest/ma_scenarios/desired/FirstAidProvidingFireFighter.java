package de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.desired;

import api.RequiredTypeTestReference;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.desired.tests.FirstAidProvidingFireFighterTest;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.business.Injured;

@RequiredTypeTestReference( testClasses = FirstAidProvidingFireFighterTest.class )
public interface FirstAidProvidingFireFighter {

  public void provideFirstAid( Injured patient );

  public void extinguishFire( Fire fire );
}
