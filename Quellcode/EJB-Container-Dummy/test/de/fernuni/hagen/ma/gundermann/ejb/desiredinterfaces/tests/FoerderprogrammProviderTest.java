package de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces.tests;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.junit.Test;

import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.ElerFTFoerderprogramm;
import de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces.FoerderprogrammeProvider;
import tester.annotation.QueryTypeInstanceSetter;

public class FoerderprogrammProviderTest {

  private FoerderprogrammeProvider provider;

  @QueryTypeInstanceSetter
  public void setProvider( FoerderprogrammeProvider provider ) {
    this.provider = provider;
  }

  @Test
  public void testEmptyCollection() {
    Collection<ElerFTFoerderprogramm> alleFreigegebenenFPs = provider.getAlleFreigegebenenFPs();
    assertThat( alleFreigegebenenFPs, notNullValue() );
  }
}
