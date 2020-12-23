package de.fernuni.hagen.ma.gundermann.ejb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.ejb.ElerFTStammdatenAuskunftService;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinder;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
import de.fernuni.hagen.ma.gundermann.ejb.desired.MinimalFoerderprogrammeProvider;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.impl.EftSTDAuskunftImpl;

public class FindMinimalFoerderprogrammeProviderTest {

  @Before
  public void setup() {
    Logger.setOutputFile(
        Paths.get( "C:\\Users\\ngundermann\\Desktop\\tmp_" + this.getClass().getSimpleName() + ".csv" ).toFile() );
  }

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
