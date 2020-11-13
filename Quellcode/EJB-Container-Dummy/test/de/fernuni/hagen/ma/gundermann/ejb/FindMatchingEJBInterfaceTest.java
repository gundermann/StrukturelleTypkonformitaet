package de.fernuni.hagen.ma.gundermann.ejb;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.ejb.ElerFTStammdatenAuskunftService;
import de.fernuni.hagen.ma.gundermann.ejb.beanimplementations.EftSTDAuskunftImpl;
import de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces.ElerFTFoerderprogrammeProvider;
import de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces.FoerderprogrammeProvider;
import de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces.MinimalFoerderprogrammeProvider;

public class FindMatchingEJBInterfaceTest {

  @Test
  public void findFullMatchingElerFTFoerderprogrammeProvider() {
    Class<ElerFTFoerderprogrammeProvider> desiredInterface = ElerFTFoerderprogrammeProvider.class;
    EJBContainer.CONTAINER.registerBean( ElerFTStammdatenAuskunftService.class, new EftSTDAuskunftImpl() );
    ElerFTFoerderprogrammeProvider desiredBean = EJBContainer.CONTAINER.getDesiredBean( desiredInterface );
    assertThat( desiredBean, notNullValue() );
  }

  @Test
  public void findFullMatchingFoerderprogrammeProvider() {
    Class<FoerderprogrammeProvider> desiredInterface = FoerderprogrammeProvider.class;
    EJBContainer.CONTAINER.registerBean( ElerFTStammdatenAuskunftService.class, new EftSTDAuskunftImpl() );
    FoerderprogrammeProvider desiredBean = EJBContainer.CONTAINER.getDesiredBean( desiredInterface );
    assertThat( desiredBean, notNullValue() );
  }

  @Test
  public void findFullMatchingMinimalFoerderprogrammeProvider() {
    Class<MinimalFoerderprogrammeProvider> desiredInterface = MinimalFoerderprogrammeProvider.class;
    EJBContainer.CONTAINER.registerBean( ElerFTStammdatenAuskunftService.class, new EftSTDAuskunftImpl() );
    MinimalFoerderprogrammeProvider desiredBean = EJBContainer.CONTAINER.getDesiredBean( desiredInterface );
    assertThat( desiredBean, notNullValue() );
  }
}
