package de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces.tests;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Date;

import org.junit.Test;

import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Foerderprogramm;
import de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces.MinimalFoerderprogrammeProvider;
import tester.annotation.QueryTypeInstanceSetter;

public class MinimalFoerderprogrammProviderTest {

  private MinimalFoerderprogrammeProvider provider;

  @QueryTypeInstanceSetter
  public void setProvider( MinimalFoerderprogrammeProvider provider ) {
    this.provider = provider;
  }

  @Test
  public void testEmptyCollection() {
    Collection<String> alleFreigegebenenFPs = provider.getAlleFreigegebenenFPs();
    assertThat( alleFreigegebenenFPs, notNullValue() );
  }

  @Test
  public void testGetFoerderprogramm() {
    Foerderprogramm alleFreigegebenenFPs = provider.getFoerderprogramm( "123", 2015, new Date() );
    assertThat( alleFreigegebenenFPs, notNullValue() );
  }
}
