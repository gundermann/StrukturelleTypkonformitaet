package de.fernuni.hagen.ma.gundermann.ejb.desired.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.fernuni.hagen.ma.gundermann.ejb.desired.IntubatinFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.Patient;
import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;

public class IntubatingFireFighterTest {

  private IntubatinFireFighter intubatingFireFighter;

  @QueryTypeInstanceSetter
  public void setProvider( IntubatinFireFighter intubatingFireFighter ) {
    this.intubatingFireFighter = intubatingFireFighter;
  }

  @QueryTypeTest
  public void extinguishFire() {
    Fire fire = new Fire();
    intubatingFireFighter.extinguishFire();
    assertFalse( fire.isActive() );
  }

  @QueryTypeTest
  public void intubate() {
    Patient patient = new Patient();
    intubatingFireFighter.intubate( patient );
    assertTrue( patient.isIntubated() );
  }

  @QueryTypeTest
  public void provideFirstAid() {
    Patient patient = new Patient();
    intubatingFireFighter.provideFirstAid( patient );
    assertTrue( patient.isFirstAidProvided() );
  }
}
