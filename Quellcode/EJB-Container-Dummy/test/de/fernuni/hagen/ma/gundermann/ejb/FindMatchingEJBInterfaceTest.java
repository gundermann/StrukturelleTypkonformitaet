package de.fernuni.hagen.ma.gundermann.ejb;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.ejb.ElerFTStammdatenAuskunftService;
import de.fernuni.hagen.ma.gundermann.ejb.beanimplementations.EftSTDAuskunftImpl;
import de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces.FoerderprogrammeProvider;

public class FindMatchingEJBInterfaceTest {

  @Test
  public void findFullMatchingFoerderprogrammeProvider() {
    Class<FoerderprogrammeProvider> desiredInterface = FoerderprogrammeProvider.class;
    EJBContainer.CONTAINER.registerBean( ElerFTStammdatenAuskunftService.class, new EftSTDAuskunftImpl() );
    FoerderprogrammeProvider desiredBean = EJBContainer.CONTAINER.getDesiredBean( desiredInterface );
    assertThat( desiredBean, notNullValue() );
  }
}
