package de.fernuni.hagen.ma.gundermann.ejb.desired;

import de.fernuni.hagen.ma.gundermann.ejb.desired.tests.FirstAidProvidingFireFighterTest;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.AccidentParticipant;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.Fire;
import tester.annotation.QueryTypeTestReference;

@QueryTypeTestReference( testClasses = FirstAidProvidingFireFighterTest.class )
public interface FirstAidProvidingFireFighter {

  public void provideFirstAid( AccidentParticipant patient );

  public void extinguishFire( Fire fire );
}
