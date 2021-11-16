package de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.descostest.ComponentContainer;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.desired.IntubatingFreeing;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.Doctor;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.FireFighter;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.Intubator;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.impl.EmergencyDoctor;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.impl.VolunteerFireFighter;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinder;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinderConfig;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;

public class FindIntubatingFreeingTest {

	@Before
	public void setup() {
		Logger.setOutputFile("tmp_" + this.getClass().getSimpleName() + ".csv");
		Logger.setLogFile("tmp_" + this.getClass().getSimpleName() + ".log");
	}

	/**
	 * Hier werden zwei Komponenten verbunden, die das erwartete Interface
	 * strukturell jeder f�r sich, semantisch aber nur in Kombination erfuellen.
	 * </br>
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

		ComponentContainer.CONTAINER.registerComponent(FireFighter.class, new VolunteerFireFighter());
		ComponentContainer.CONTAINER.registerComponent(Doctor.class, new EmergencyDoctor());
		ComponentContainer.CONTAINER.registerComponent(Intubator.class, new EmergencyDoctor());

		// Wenn der ParaMedic mit hineingenommen wird, kann auch die Kombination aus
		// ParaMedic und Doctor gefunden werden.
		// EJBContainer.CONTAINER.registerBean( ParaMedic.class, new
		// ProfessionalFireFighter() );
		DesiredComponentFinderConfig config = new DesiredComponentFinderConfig.Builder(
				ComponentContainer.CONTAINER.getRegisteredComponentInterfaces(), ComponentContainer.CONTAINER::getOptComponent)//
						.useHeuristicLMF()//
						.useHeuristicPTTF()//
						.useHeuristicBL_NMC()//
						.build();
		IntubatingFreeing desiredBean = new DesiredComponentFinder(config).getDesiredComponent(desiredInterface);
		assertThat(desiredBean, notNullValue());
	}
}
