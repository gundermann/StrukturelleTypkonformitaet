package de.fernuni.hagen.ma.gundermann.ejb;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinder;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.QuantitaiveMatchingInfoComparator;
import de.fernuni.hagen.ma.gundermann.ejb.desired.IntubatingFreeing;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.Doctor;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.FireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.impl.EmergencyDoctor;
import de.fernuni.hagen.ma.gundermann.ejb.provided.beans.impl.VolunteerFireFighter;

/**
 * TODO dieser Test benoetigt verhaeltnismaeßig viel Zeit. Vermutlich liegt es daran, dass viele der deg-Bean-Interfaces
 * strutkurell vollstaendig passen und die semantisch passenden Interfaces im Container an das Ende der Liste angefuegt
 * wurden. </br>
 * </br>
 * Loeseung: Die Vorsortierung der Komponenten erfolgt zusätzlich noch über ein qualitatives Kriterium, welches das
 * Verhaeltnis von originalen Methoden der Quell-Komponenten zu den nutzbaren Methoden der Ziel-Komponente sicherstellt:
 * ORI_MET / PUBLIC_MET ==> siehe {@link QuantitaiveMatchingInfoComparator}
 */
public class FindIntubatingFreeingTest {

  /**
   * Hier werden zwei Komponenten verbunden, die das erwartete Interface strukturell jeder für sich, semantisch aber nur
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
