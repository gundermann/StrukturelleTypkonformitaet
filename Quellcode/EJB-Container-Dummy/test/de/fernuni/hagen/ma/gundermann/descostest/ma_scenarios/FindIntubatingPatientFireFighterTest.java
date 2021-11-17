package de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.descostest.ComponentContainer;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.desired.IntubatingPatientFireFighter;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.Doctor;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.FireFighter;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.FirstAidTrainedPasserby;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.Intubator;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.ParaMedic;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.impl.EmergencyDoctor;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.impl.ProfessionalFireFighter;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.impl.TrainedPasserby;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.impl.VolunteerFireFighter;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinder;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinderConfig;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;

public class FindIntubatingPatientFireFighterTest {

	@Before
	public void setup() {
		Logger.setOutputFile("tmp_" + this.getClass().getSimpleName() + ".csv");
		Logger.setLogFile("tmp_" + this.getClass().getSimpleName() + ".log");
	}

	@Test
	public void findCombined() {
		Class<IntubatingPatientFireFighter> desiredInterface = IntubatingPatientFireFighter.class;

		ComponentContainer.CONTAINER.registerComponent(FirstAidTrainedPasserby.class, new TrainedPasserby());
		ComponentContainer.CONTAINER.registerComponent(FireFighter.class, new VolunteerFireFighter());
		ComponentContainer.CONTAINER.registerComponent(Doctor.class, new EmergencyDoctor());
		ComponentContainer.CONTAINER.registerComponent(Intubator.class, new EmergencyDoctor());
		ComponentContainer.CONTAINER.registerComponent(ParaMedic.class, new ProfessionalFireFighter());

		DesiredComponentFinderConfig config = new DesiredComponentFinderConfig.Builder(
				ComponentContainer.CONTAINER.getRegisteredComponentInterfaces(),
				ComponentContainer.CONTAINER::getOptComponent)//
						.useHeuristicLMF() //
						.useHeuristicPTTF()//
						.useHeuristicBL_NMC()//
						.build();
		IntubatingPatientFireFighter desiredBean = new DesiredComponentFinder(config)
				.getDesiredComponent(desiredInterface);
		assertThat(desiredBean, notNullValue());
	}
}
