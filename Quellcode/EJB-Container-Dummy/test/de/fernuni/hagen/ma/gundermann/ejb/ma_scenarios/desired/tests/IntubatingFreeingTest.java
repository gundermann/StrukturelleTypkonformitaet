package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.tests;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.IntubatingFreeing;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Injured;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Suffer;
import spi.PivotMethodTestInfo;
import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;

public class IntubatingFreeingTest implements PivotMethodTestInfo {

  private IntubatingFreeing intubatingFreeing;

  private boolean pivotMethodCallExecuted;

  @Before
  public void before() {
    reset();
  }

  @QueryTypeInstanceSetter
  public void setProvider( IntubatingFreeing intubatingFireFighter ) {
    this.intubatingFreeing = intubatingFireFighter;
  }

  @QueryTypeTest( testedSingleMethod = "free" )
  public void free() {
    Collection<Suffer> suffer = Arrays.asList( Suffer.LOCKED );
    Injured patient = new Injured( suffer );
    intubatingFreeing.free( patient );
    markPivotMethodCallExecuted();
    assertTrue( patient.isStabilized() );
  }

  @QueryTypeTest( testedSingleMethod = "intubate" )
  public void intubate() {
    Collection<Suffer> suffer = Arrays.asList( Suffer.BREATH_PROBLEMS );
    Injured patient = new Injured( suffer );
    intubatingFreeing.intubate( patient );
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
}
