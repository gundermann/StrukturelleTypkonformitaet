package de.fernuni.hagen.ma.gundermann.ejb;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinder;
import de.fernuni.hagen.ma.gundermann.ejb.desired.IntubatingFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.FireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.Intubator;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.impl.EmergencyDoctor;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.impl.VolunteerFireFighter;

public class FindIntubatingPatrientFireFighterTest {

  /**
   * Hier werden zwei Komponenten verbunden, die das erwartete Interface sowohl strukturell als auch semantisch nur in
   * Kombination erfuellen.</br>
   * <b>COMP: A, B</b> </br>
   * <b>STRUCT MATCH:</b>
   * <ul>
   * <li>A + B= FULL
   * </ul>
   * </br>
   * <b>SEM MATCH:</b>
   * <ul>
   * <li>A + B= FULL
   * </ul>
   */
  @Test
  public void findCombined() {
    Class<IntubatingFireFighter> desiredInterface = IntubatingFireFighter.class;
    EJBContainer.CONTAINER.reset();
    EJBContainer.CONTAINER.registerBean( FireFighter.class, new VolunteerFireFighter() );
    EJBContainer.CONTAINER.registerBean( Intubator.class, new EmergencyDoctor() );
    IntubatingFireFighter desiredBean = new DesiredComponentFinder(
        EJBContainer.CONTAINER.getRegisteredBeanInterfaces(),
        EJBContainer.CONTAINER::getOptBean ).getDesiredComponent( desiredInterface );
    assertThat( desiredBean, notNullValue() );
  }
}