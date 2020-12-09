package de.fernuni.hagen.ma.gundermann.ejb.desired;

import de.fernuni.hagen.ma.gundermann.ejb.desired.tests.IntubatingFireFighterTest;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.Patient;
import tester.annotation.QueryTypeTestReference;

@QueryTypeTestReference( testClasses = IntubatingFireFighterTest.class )
public interface IntubatinFireFighter {

  public void intubate( Patient patient );

  public void provideFirstAid( Patient patient );

  public void extinguishFire();
}
