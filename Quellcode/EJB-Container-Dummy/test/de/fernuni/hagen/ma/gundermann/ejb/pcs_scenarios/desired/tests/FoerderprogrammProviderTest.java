package de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired.tests;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collection;

import org.junit.Before;

import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Foerderprogramm;
import de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired.FoerderprogrammeProvider;
import spi.PivotMethodTestInfo;
import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;

public class FoerderprogrammProviderTest implements PivotMethodTestInfo {

  private FoerderprogrammeProvider provider;

  private boolean pivotMethodCallExecuted;

  @Before
  public void before() {
    reset();
  }

  @QueryTypeInstanceSetter
  public void setProvider( FoerderprogrammeProvider provider ) {
    this.provider = provider;
  }

  @QueryTypeTest( testedSingleMethod = "getAlleFreigegebenenFPs" )
  public void testEmptyCollection() {
    Collection<Foerderprogramm> alleFreigegebenenFPs = provider.getAlleFreigegebenenFPs();
    markPivotMethodCallExecuted();
    assertThat( alleFreigegebenenFPs, notNullValue() );
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
