package de.fernuni.hagen.ma.gundermann.ejb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.ejb.ElerFTStammdatenAuskunftService;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinder;
import de.fernuni.hagen.ma.gundermann.ejb.beanimplementations.EftSTDAuskunftImpl;
import de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces.MinimalFoerderprogrammeProvider;

public class FindMinimalFoerderprogrammeProviderTest {

  @Test
  public void findFullMatchingMinimalFoerderprogrammeProvider() {
    Class<MinimalFoerderprogrammeProvider> desiredInterface = MinimalFoerderprogrammeProvider.class;
    EJBContainer.CONTAINER.registerBean( ElerFTStammdatenAuskunftService.class, new EftSTDAuskunftImpl() );
    MinimalFoerderprogrammeProvider desiredBean = new DesiredComponentFinder(
        EJBContainer.CONTAINER.getRegisteredBeanInterfaces(), EJBContainer.CONTAINER::getOptBean )
            .getDesiredComponent( desiredInterface );
    assertThat( desiredBean, notNullValue() );
  }
}
