package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinder;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinderConfig;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
import de.fernuni.hagen.ma.gundermann.ejb.EJBContainer;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.IntubatingPatientFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.FireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.Intubator;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.impl.EmergencyDoctor;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.impl.VolunteerFireFighter;

/**
 * TODO Auch dieser Test benoetigt relativ viel Zeit. (Siehe Kommentar in
 * {@link FindIntubatingFreeingTest})
 */
public class FindIntubatingPatientFireFighterTest {

	@Before
	public void setup() {
		Logger.setOutputFile("tmp_" + this.getClass().getSimpleName() + ".csv");
		Logger.setLogFile("tmp_" + this.getClass().getSimpleName() + ".log");
	}

	/**
	 * Hier werden zwei Komponenten verbunden, die das erwartete Interface sowohl
	 * strukturell als auch semantisch nur in Kombination erfuellen.</br>
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
		Class<IntubatingPatientFireFighter> desiredInterface = IntubatingPatientFireFighter.class;
		// EJBContainer.CONTAINER.reset();
		EJBContainer.CONTAINER.registerBean(FireFighter.class, new VolunteerFireFighter());
		EJBContainer.CONTAINER.registerBean(Intubator.class, new EmergencyDoctor());
		DesiredComponentFinderConfig config = new DesiredComponentFinderConfig.Builder(
				EJBContainer.CONTAINER.getRegisteredBeanInterfaces(), EJBContainer.CONTAINER::getOptBean)
						.useHeuristicBL_NMC().useHeuristicLMF().useHeuristicPTTF().build();
		IntubatingPatientFireFighter desiredBean = new DesiredComponentFinder(config)
				.getDesiredComponent(desiredInterface);
		assertThat(desiredBean, notNullValue());
	}
}
