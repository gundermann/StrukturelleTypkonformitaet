package de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.descostest.ComponentContainer;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.desired.IntubatingFireFighter;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.Doctor;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.FireFighter;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.Intubator;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.impl.EmergencyDoctor;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.impl.VolunteerFireFighter;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinder;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinderConfig;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;

public class FindIntubatingFireFighterTest {

	@Before
	public void setup() {
		Logger.setOutputFile("tmp_" + this.getClass().getSimpleName() + ".csv");
		Logger.setLogFile("tmp_" + this.getClass().getSimpleName() + ".log");
	}

	/**
	 * Hier werden zwei Komponenten verbunden, von denen nur eine das erwartete
	 * Interface strukturell vollstaendig erfuellt, semantisch aber nur die
	 * Kombination der beiden Komponenten in Frage konnt.</br>
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
		ComponentContainer.CONTAINER.registerComponent(FireFighter.class, new VolunteerFireFighter());
		ComponentContainer.CONTAINER.registerComponent(Doctor.class, new EmergencyDoctor());
		ComponentContainer.CONTAINER.registerComponent(Intubator.class, new EmergencyDoctor());

		DesiredComponentFinderConfig config = new DesiredComponentFinderConfig.Builder(
				ComponentContainer.CONTAINER.getRegisteredComponentInterfaces(), ComponentContainer.CONTAINER::getOptComponent)//
						.useHeuristicLMF()//
						.useHeuristicBL_NMC()//
						.useHeuristicPTTF()//
						.build();
		IntubatingFireFighter desiredBean = new DesiredComponentFinder(config).getDesiredComponent(desiredInterface);
		assertThat(desiredBean, notNullValue());
	}
}
