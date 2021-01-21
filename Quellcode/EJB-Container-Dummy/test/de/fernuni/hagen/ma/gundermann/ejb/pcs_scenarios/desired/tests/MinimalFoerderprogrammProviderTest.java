package de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Collection;
import java.util.Date;

import org.junit.Before;

import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Foerderprogramm;
import DE.data_experts.profi.util.allg.DvFoerderprogramm;
import de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired.MinimalFoerderprogrammeProvider;
import spi.PivotMethodTestInfo;
import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;

public class MinimalFoerderprogrammProviderTest implements PivotMethodTestInfo {

  private MinimalFoerderprogrammeProvider provider;

  private boolean pivotMethodCallExecuted;

  @Before
  public void before() {
    reset();
  }

  @QueryTypeInstanceSetter
  public void setProvider( MinimalFoerderprogrammeProvider provider ) {
    this.provider = provider;
  }

  @QueryTypeTest( testedSingleMethod = "getAlleFreigegebenenFPs" )
  public void testEmptyCollection() {
    Collection<String> alleFreigegebenenFPs = provider.getAlleFreigegebenenFPs();
    markPivotMethodCallExecuted();
    assertThat( alleFreigegebenenFPs, notNullValue() );
  }

  @QueryTypeTest( testedSingleMethod = "getAlleFreigegebenenFPs" )
  public void testGetFoerderprogramm() {
    String fpCode = "123";
    Foerderprogramm fp = provider.getFoerderprogramm( fpCode, 2015, new Date() );
    markPivotMethodCallExecuted();
    assertThat( fp, notNullValue() );
    DvFoerderprogramm dvFP = fp.getFoerderprogramm();
    assertThat( dvFP, notNullValue() );

    String code = dvFP.getCode();
    // assertTrue( Objects.equals( fpCode, code ) );
    assertThat( fpCode, equalTo( code ) );
    // WARNING assertThat funktioniert nur, wenn das hamcrest-JAR �ber den JUnit-JAR steht (BuildPath > Export-Order)
    // ERROR: class "org.hamcrest.Matchers"'s signer information does not match signer information of other classes in
    // the same
    // package
    // siehe issue: https://code.google.com/archive/p/hamcrest/issues/128

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
