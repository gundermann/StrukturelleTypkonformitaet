package de.fernuni.hagen.ma.gundermann.ejb;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinder;
import de.fernuni.hagen.ma.gundermann.ejb.desired.IntubatingFreeing;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.Doctor;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.FireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.impl.EmergencyDoctor;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.impl.VolunteerFireFighter;

public class FindIntubatingFreeingTest {

  /**
   * Hier werden zwei Komponenten verbunden, die das erwartete Interface strukturell jeder f�r sich, semantisch aber nur
   * in Kombination erfuellen. </br>
   * <b>COMP: A, B</b> </br>
   * <b>STRUCT MATCH:</b>
   * <ul>
   * <li>A = FULL
   * <li>B = FULL
   * </ul>
   * </br>
   * <b>SEM MATCH:</b>
   * <ul>
   * <li>A + B= FULL
   * </ul>
   */
  @Test
  public void findCombined() {
    Class<IntubatingFreeing> desiredInterface = IntubatingFreeing.class;
    // EJBContainer.CONTAINER.reset();

    EJBContainer.CONTAINER.registerBean( FireFighter.class, new VolunteerFireFighter() );
    EJBContainer.CONTAINER.registerBean( Doctor.class, new EmergencyDoctor() );
    // EJBContainer.CONTAINER.registerBean( FirstAidTrainedPasserby.class, new TrainedPasserby() );
    // EJBContainer.CONTAINER.registerBean( FireFighter.class, new ProfessionalFireFighter() );

    // Wenn der ParaMedic mit hineingenommen wird, kann auch die Kombination aus ParaMedic und Doctor gefunden werden.
    // EJBContainer.CONTAINER.registerBean( ParaMedic.class, new ProfessionalFireFighter() );
    IntubatingFreeing desiredBean = new DesiredComponentFinder(
        EJBContainer.CONTAINER.getRegisteredBeanInterfaces(),
        EJBContainer.CONTAINER::getOptBean ).getDesiredComponent( desiredInterface );
    assertThat( desiredBean, notNullValue() );
  }
}
