package de.fernuni.hagen.ma.gundermann.ejb;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinder;
import de.fernuni.hagen.ma.gundermann.ejb.desired.IntubatingFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.Doctor;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.FireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.impl.EmergencyDoctor;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.impl.VolunteerFireFighter;

public class FindIntubatingFireFighterTest {

  @Test
  public void findCombined() {
    Class<IntubatingFireFighter> desiredInterface = IntubatingFireFighter.class;
    EJBContainer.CONTAINER.reset();
    EJBContainer.CONTAINER.registerBean( FireFighter.class, new VolunteerFireFighter() );
    EJBContainer.CONTAINER.registerBean( Doctor.class, new EmergencyDoctor() );
    IntubatingFireFighter desiredBean = new DesiredComponentFinder(
        EJBContainer.CONTAINER.getRegisteredBeanInterfaces(),
        EJBContainer.CONTAINER::getOptBean ).getDesiredComponent( desiredInterface );
    assertThat( desiredBean, notNullValue() );
  }
}
