package de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces.tests;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collection;
import java.util.Date;

import org.easymock.EasyMock;

import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.ElerFTFoerderprogramm;
import DE.data_experts.profi.util.DvAntragsJahr;
import DE.data_experts.profi.util.allg.DvFoerderprogramm;
import de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces.ElerFTFoerderprogrammeProvider;
import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;

public class ElerFTFoerderprogrammProviderTest {

  private ElerFTFoerderprogrammeProvider provider;

  @QueryTypeInstanceSetter
  public void setProvider( ElerFTFoerderprogrammeProvider provider ) {
    this.provider = provider;
  }

  // @Test - ja man könnte diese Annotation zur Indentifikation der Test-Methoden verwenden. Allerdings werden sie dann
  // auch automatisch bei JUnit-Tests ausgeführt.
  @QueryTypeTest
  public void testEmptyCollection() {
    Collection<ElerFTFoerderprogramm> alleFreigegebenenFPs = provider.getAlleFreigegebenenFPs();
    assertThat( alleFreigegebenenFPs, notNullValue() );
  }

  @QueryTypeTest
  public void testMockedFPCollection() {
    DvFoerderprogramm fp = EasyMock.createNiceMock( DvFoerderprogramm.class );
    EasyMock.replay( fp );
    ElerFTFoerderprogramm alleFreigegebenenFPs = provider.getElerFTFoerderprogramm( DvAntragsJahr.AJ2020,
        fp, new Date() );
    assertThat( alleFreigegebenenFPs, notNullValue() );
  }
}
