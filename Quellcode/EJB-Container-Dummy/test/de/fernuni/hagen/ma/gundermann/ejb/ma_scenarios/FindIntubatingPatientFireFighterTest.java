package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.ejb.ElerFTStammdatenAuskunftService;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.ejb.StammdatenAuskunftService;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinder;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinderConfig;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
import de.fernuni.hagen.ma.gundermann.ejb.EJBContainer;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.IntubatingPatientFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.Doctor;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.FireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.Intubator;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.impl.EmergencyDoctor;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.impl.VolunteerFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.provided.beans.impl.EftSTDAuskunftImpl;
import de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.provided.beans.impl.STDAuskunftImpl;

public class FindIntubatingPatientFireFighterTest {

	@Before
	public void setup() {
		Logger.setOutputFile("tmp_" + this.getClass().getSimpleName() + ".csv");
		Logger.setLogFile("tmp_" + this.getClass().getSimpleName() + ".log");
		EJBContainer.CONTAINER.reInit();
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
		EJBContainer.CONTAINER.registerBean(ElerFTStammdatenAuskunftService.class, new EftSTDAuskunftImpl());
		EJBContainer.CONTAINER.registerBean(StammdatenAuskunftService.class, new STDAuskunftImpl());
		EJBContainer.CONTAINER.registerBean(FireFighter.class, new VolunteerFireFighter());
		EJBContainer.CONTAINER.registerBean(Doctor.class, new EmergencyDoctor());
		EJBContainer.CONTAINER.registerBean(Intubator.class, new EmergencyDoctor());
		DesiredComponentFinderConfig config = new DesiredComponentFinderConfig.Builder(
				EJBContainer.CONTAINER.getRegisteredBeanInterfaces(), EJBContainer.CONTAINER::getOptBean)//
						.useHeuristicLMF() //
						.useHeuristicPTTF()//
						.useHeuristicBL_NMC()//
						.build();
		IntubatingPatientFireFighter desiredBean = new DesiredComponentFinder(config)
				.getDesiredComponent(desiredInterface);
		assertThat(desiredBean, notNullValue());
	}
}
