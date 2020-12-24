package de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired.tests;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collection;

import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.ElerFTFoerderprogramm;
import de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired.ElerFTFoerderprogrammeProvider;
import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;

public class ElerFTFoerderprogrammProviderTest {

  private ElerFTFoerderprogrammeProvider provider;

  @QueryTypeInstanceSetter
  public void setProvider( ElerFTFoerderprogrammeProvider provider ) {
    this.provider = provider;
  }

  // @Test - ja man k�nnte diese Annotation zur Indentifikation der Test-Methoden verwenden. Allerdings werden sie dann
  // auch automatisch bei JUnit-Tests ausgef�hrt.
  @QueryTypeTest
  public void testEmptyCollection() {
    Collection<ElerFTFoerderprogramm> alleFreigegebenenFPs = provider.getAlleFreigegebenenFPs();
    assertThat( alleFreigegebenenFPs, notNullValue() );
  }

  // Dass dieser Test nicht funktioniert, liegt vermutlich an den DVs
  // @QueryTypeTest
  // public void testMockedFPCollection() {
  // DvFoerderprogramm fp = EasyMock.createNiceMock( DvFoerderprogramm.class );
  // EasyMock.expect( fp.getNummer() ).andReturn( 800L ).anyTimes();
  // EasyMock.expect( fp.isDefined() ).andReturn( true ).anyTimes();
  // EasyMock.replay( fp );
  // ElerFTFoerderprogramm alleFreigegebenenFPs = provider.getElerFTFoerderprogramm( DvAntragsJahr.AJ2020,
  // fp, new Date() );
  // assertThat( alleFreigegebenenFPs, notNullValue() );
  // }
}
