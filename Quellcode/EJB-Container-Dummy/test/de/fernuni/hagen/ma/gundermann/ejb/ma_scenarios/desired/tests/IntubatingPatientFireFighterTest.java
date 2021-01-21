package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.IntubatingPatientFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.IntubationPartient;
import spi.PivotMethodTestInfo;
import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;

public class IntubatingPatientFireFighterTest implements PivotMethodTestInfo {

  private IntubatingPatientFireFighter intubatingPatientFireFighter;

  private boolean pivotMethodCallExecuted;

  @QueryTypeInstanceSetter
  public void setProvider( IntubatingPatientFireFighter intubatingFireFighter ) {
    this.intubatingPatientFireFighter = intubatingFireFighter;
  }

  @Before
  public void before() {
    reset();
  }

  @QueryTypeTest( testedSingleMethod = "free" )
  public void free() {
    Fire fire = new Fire();
    intubatingPatientFireFighter.extinguishFire( fire );
    markPivotMethodCallExecuted();
    assertFalse( fire.isActive() );
  }

  @QueryTypeTest( testedSingleMethod = "intubate" )
  public void intubate() {
    IntubationPartient patient = new IntubationPartient();
    intubatingPatientFireFighter.intubate( patient );
    markPivotMethodCallExecuted();
    assertTrue( patient.isIntubated() );
  }

  @Override
  public void reset() {
    pivotMethodCallExecuted = false;
  }

  @Override
  public void markPivotMethodCallExecuted() {
    pivotMethodCallExecuted = true;
  }

  @Override
  public boolean pivotMethodCallExecuted() {
    return pivotMethodCallExecuted;
  }

}
