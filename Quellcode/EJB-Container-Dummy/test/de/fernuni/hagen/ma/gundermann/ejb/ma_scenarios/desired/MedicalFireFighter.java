package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired;

import api.RequiredTypeTestReference;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.tests.MedicalFireFighterTest;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Injured;

@RequiredTypeTestReference( testClasses = MedicalFireFighterTest.class )
public interface MedicalFireFighter {

  void heal( Injured injured, MedCabinet med );

  boolean extinguishFire( Fire fire );
}
