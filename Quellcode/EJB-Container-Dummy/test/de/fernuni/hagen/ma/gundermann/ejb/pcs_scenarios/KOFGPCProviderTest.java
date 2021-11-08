package de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Before;
import org.junit.Test;

import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.ejb.ElerFTStammdatenAuskunftService;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.ejb.StammdatenAuskunftService;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinder;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.DesiredComponentFinderConfig;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
import de.fernuni.hagen.ma.gundermann.ejb.EJBContainer;
import de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired.MinimalFoerderprogrammeProvider;
import de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.provided.beans.impl.EftSTDAuskunftImpl;
import de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.provided.beans.impl.STDAuskunftImpl;

public class KOFGPCProviderTest {

	@Before
	public void setup() {
		Logger.setOutputFile("tmp_" + this.getClass().getSimpleName() + ".csv");
		Logger.setLogFile("tmp_" + this.getClass().getSimpleName() + ".log");
		EJBContainer.CONTAINER.reInit();
	}

	@Test
	public void findFullMatchingMinimalFoerderprogrammeProvider() {
		Class<MinimalFoerderprogrammeProvider> desiredInterface = MinimalFoerderprogrammeProvider.class;
		EJBContainer.CONTAINER.registerBean(ElerFTStammdatenAuskunftService.class, new EftSTDAuskunftImpl());
		EJBContainer.CONTAINER.registerBean(StammdatenAuskunftService.class, new STDAuskunftImpl());
		DesiredComponentFinderConfig config = new DesiredComponentFinderConfig.Builder(
				EJBContainer.CONTAINER.getRegisteredBeanInterfaces(), EJBContainer.CONTAINER::getOptBean)//
						.useHeuristicLMF()//
						.useHeuristicPTTF()//
						.useHeuristicBL_NMC()//
						.build();
		MinimalFoerderprogrammeProvider desiredBean = new DesiredComponentFinder(config)
				.getDesiredComponent(desiredInterface);
		assertThat(desiredBean, notNullValue());
	}
}
