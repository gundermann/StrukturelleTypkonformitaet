package de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.ejb.ElerFTStammdatenAuskunftService;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinder;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
import de.fernuni.hagen.ma.gundermann.ejb.EJBContainer;
import de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired.FoerderprogrammeProvider;
import de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.provided.beans.impl.EftSTDAuskunftImpl;

public class FindFoerderprogrammeProviderTest {

  @Before
  public void setup() {
    Logger.setOutputFile(
        "tmp_" + this.getClass().getSimpleName() + ".csv" );
  }

  @Test
  public void findFullMatchingFoerderprogrammeProvider() {
    Class<FoerderprogrammeProvider> desiredInterface = FoerderprogrammeProvider.class;
    EJBContainer.CONTAINER.registerBean( ElerFTStammdatenAuskunftService.class, new EftSTDAuskunftImpl() );
    FoerderprogrammeProvider desiredBean = new DesiredComponentFinder(
        EJBContainer.CONTAINER.getRegisteredBeanInterfaces(),
        EJBContainer.CONTAINER::getOptBean ).getDesiredComponent( desiredInterface );
    assertThat( desiredBean, notNullValue() );
  }

}
