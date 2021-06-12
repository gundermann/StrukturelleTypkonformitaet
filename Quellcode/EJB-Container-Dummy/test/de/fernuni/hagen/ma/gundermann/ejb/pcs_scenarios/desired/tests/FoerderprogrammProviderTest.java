package de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired.tests;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Foerderprogramm;
import de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired.FoerderprogrammeProvider;
import spi.CalledMethodInfo;
import tester.annotation.RequiredTypeInstanceSetter;
import tester.annotation.RequiredTypeTest;

public class FoerderprogrammProviderTest implements CalledMethodInfo {

	private FoerderprogrammeProvider provider;

	private Collection<Method> calledMethods = new ArrayList<Method>();

	@RequiredTypeInstanceSetter
	public void setProvider(FoerderprogrammeProvider provider) {
		this.provider = provider;
	}

	@RequiredTypeTest
	public void testEmptyCollection() {
		Collection<Foerderprogramm> alleFreigegebenenFPs = provider.getAlleFreigegebenenFPs();
		addCalledMethod(getMethod("getAlleFreigegebenenFPs", FoerderprogrammeProvider.class));
		assertThat(alleFreigegebenenFPs, notNullValue());
	}

	@Override
	public void addCalledMethod(Method m) {
		calledMethods.add(m);
	}

	@Override
	public Collection<Method> getCalledMethods() {
		return calledMethods;
	}
}
