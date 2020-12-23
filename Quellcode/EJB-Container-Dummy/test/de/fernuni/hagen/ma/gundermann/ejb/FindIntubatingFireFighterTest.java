package de.fernuni.hagen.ma.gundermann.ejb;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinder;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
import de.fernuni.hagen.ma.gundermann.ejb.desired.IntubatingFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.Doctor;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.FireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.impl.EmergencyDoctor;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.impl.VolunteerFireFighter;

public class FindIntubatingFireFighterTest {

  @Before
  public void setup() {
    Logger.setOutputFile(
        Paths.get( "C:\\Users\\ngundermann\\Desktop\\tmp_" + this.getClass().getSimpleName() + ".csv" ).toFile() );
  }

  /**
   * Hier werden zwei Komponenten verbunden, von denen nur eine das erwartete Interface strukturell vollstaendig
   * erfuellt, semantisch aber nur die Kombination der beiden Komponenten in Frage konnt.</br>
   * <b>COMP: A, B</b> </br>
   * <b>STRUCT MATCH:</b>
   * <ul>
   * <li>A = FULL
   * <li>B = PARTLY
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
    // EJBContainer.CONTAINER.reset();
    EJBContainer.CONTAINER.registerBean( FireFighter.class, new VolunteerFireFighter() );
    EJBContainer.CONTAINER.registerBean( Doctor.class, new EmergencyDoctor() );
    IntubatingFireFighter desiredBean = new DesiredComponentFinder(
        EJBContainer.CONTAINER.getRegisteredBeanInterfaces(),
        EJBContainer.CONTAINER::getOptBean ).getDesiredComponent( desiredInterface );
    assertThat( desiredBean, notNullValue() );
  }
}
