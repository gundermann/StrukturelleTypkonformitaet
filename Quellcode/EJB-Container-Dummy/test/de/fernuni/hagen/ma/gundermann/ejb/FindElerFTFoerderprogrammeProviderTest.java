package de.fernuni.hagen.ma.gundermann.ejb;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.ejb.ElerFTStammdatenAuskunftService;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinder;
import de.fernuni.hagen.ma.gundermann.ejb.desired.ElerFTFoerderprogrammeProvider;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.impl.EftSTDAuskunftImpl;

public class FindElerFTFoerderprogrammeProviderTest {

  @Test
  public void findFullMatchingElerFTFoerderprogrammeProvider() {
    Class<ElerFTFoerderprogrammeProvider> desiredInterface = ElerFTFoerderprogrammeProvider.class;
    EJBContainer.CONTAINER.registerBean( ElerFTStammdatenAuskunftService.class, new EftSTDAuskunftImpl() );
    ElerFTFoerderprogrammeProvider desiredBean = new DesiredComponentFinder(
        EJBContainer.CONTAINER.getRegisteredBeanInterfaces(), EJBContainer.CONTAINER::getOptBean )
            .getDesiredComponent( desiredInterface );
    assertThat( desiredBean, notNullValue() );
  }

}