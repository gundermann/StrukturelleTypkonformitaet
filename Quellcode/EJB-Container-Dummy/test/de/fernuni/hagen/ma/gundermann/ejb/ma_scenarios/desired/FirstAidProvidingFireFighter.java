package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired;

import api.RequiredTypeTestReference;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.tests.FirstAidProvidingFireFighterTest;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Injured;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Fire;

@RequiredTypeTestReference( testClasses = FirstAidProvidingFireFighterTest.class )
public interface FirstAidProvidingFireFighter {

  public void provideFirstAid( Injured patient );

  public void extinguishFire( Fire fire );
}
