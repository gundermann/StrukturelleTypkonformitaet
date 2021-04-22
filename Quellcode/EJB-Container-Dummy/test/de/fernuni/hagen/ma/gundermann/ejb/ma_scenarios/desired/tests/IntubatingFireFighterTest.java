package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.IntubatingFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Injured;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Suffer;
import spi.PivotMethodInfoContainer;
import spi.PivotMethodTestInfo;
import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;

public class IntubatingFireFighterTest implements PivotMethodTestInfo {

  private IntubatingFireFighter intubatingFireFighter;

  private boolean pivotMethodCallExecuted;

  private PivotMethodInfoContainer pmiContainer = new PivotMethodInfoContainer();

  @Before
  public void before() {
    reset();
  }

  @QueryTypeInstanceSetter
  public void setProvider( IntubatingFireFighter intubatingFireFighter ) {
    this.intubatingFireFighter = intubatingFireFighter;
  }

  @QueryTypeTest( testedSingleMethod = "extinguishFire" )
  public void free() {
    Fire fire = new Fire();
    intubatingFireFighter.extinguishFire( fire );
    markPivotMethodCallExecuted();
    assertFalse( fire.isActive() );
  }

  @QueryTypeTest( testedSingleMethod = "intubate" )
  public void intubate() {
    Collection<Suffer> suffer = Arrays.asList( Suffer.BREATH_PROBLEMS );
    Injured patient = new Injured( suffer );
    intubatingFireFighter.intubate( patient );
    markPivotMethodCallExecuted();
    assertTrue( patient.isStabilized() );
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

  @Override
  public PivotMethodInfoContainer getPivotMethodInfoContainer() {
    return pmiContainer;
  }
}
