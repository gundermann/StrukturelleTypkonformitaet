package de.fernuni.hagen.ma.gundermann.ejb.desired.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.fernuni.hagen.ma.gundermann.ejb.desired.IntubatingPatientFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.IntubationPartient;
import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;

public class IntubatingPatientFireFighterTest {

  private IntubatingPatientFireFighter intubatingPatientFireFighter;

  @QueryTypeInstanceSetter
  public void setProvider( IntubatingPatientFireFighter intubatingFireFighter ) {
    this.intubatingPatientFireFighter = intubatingFireFighter;
  }

  @QueryTypeTest
  public void free() {
    Fire fire = new Fire();
    intubatingPatientFireFighter.extinguishFire( fire );
    assertFalse( fire.isActive() );
  }

  @QueryTypeTest
  public void intubate() {
    IntubationPartient patient = new IntubationPartient();
    intubatingPatientFireFighter.intubate( patient );
    assertTrue( patient.isIntubated() );
  }

}
