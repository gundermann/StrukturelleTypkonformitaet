package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Objects;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.IntubatingPatientFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.FireState;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.IntubationPartient;
import spi.PivotMethodInfoContainer;
import spi.PivotMethodTestInfo;
import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;

public class IntubatingPatientFireFighterTest implements PivotMethodTestInfo {

  private IntubatingPatientFireFighter intubatingPatientFireFighter;

  private PivotMethodInfoContainer pmiContainer = new PivotMethodInfoContainer();

  @QueryTypeInstanceSetter
  public void setProvider( IntubatingPatientFireFighter intubatingFireFighter ) {
    this.intubatingPatientFireFighter = intubatingFireFighter;
  }

  @QueryTypeTest( testedSingleMethod = "free" )
  public void free() {
    Fire fire = new Fire();
    FireState fireState = intubatingPatientFireFighter.extinguishFire( fire );
    markPivotMethodCallExecuted();
    assertTrue( Objects.equals( fireState.isActive(), fire.isActive() ) );
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
  public PivotMethodInfoContainer getPivotMethodInfoContainer() {
    return pmiContainer;
  }

}
