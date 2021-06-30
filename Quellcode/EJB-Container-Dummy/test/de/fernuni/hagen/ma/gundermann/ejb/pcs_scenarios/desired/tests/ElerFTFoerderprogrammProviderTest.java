package de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired.tests;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.ElerFTFoerderprogramm;
import api.RequiredTypeInstanceSetter;
import api.RequiredTypeTest;
import de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired.ElerFTFoerderprogrammeProvider;
import spi.TriedMethodCallsInfo;

public class ElerFTFoerderprogrammProviderTest implements TriedMethodCallsInfo {

	private ElerFTFoerderprogrammeProvider provider;
	private Collection<Method> calledMethods = new ArrayList<Method>();

	@RequiredTypeInstanceSetter
	public void setProvider(ElerFTFoerderprogrammeProvider provider) {
		this.provider = provider;
	}

	// @Test - ja man k�nnte diese Annotation zur Indentifikation der
	// Test-Methoden verwenden. Allerdings werden sie
	// dann
	// auch automatisch bei JUnit-Tests ausgef�hrt.
	@RequiredTypeTest
	public void testEmptyCollection() {
		addTriedMethodCall(getMethod("getAlleFreigegebenenFPs", ElerFTFoerderprogrammeProvider.class));
		Collection<ElerFTFoerderprogramm> alleFreigegebenenFPs = provider.getAlleFreigegebenenFPs();
		assertThat(alleFreigegebenenFPs, notNullValue());
	}

	@Override
	public void addTriedMethodCall(Method m) {
		calledMethods.add(m);
	}

	@Override
	public Collection<Method> getTriedMethodCalls() {
		return calledMethods;
	}

	// Dass dieser Test nicht funktioniert, liegt vermutlich an den DVs
	// @QueryTypeTest
	// public void testMockedFPCollection() {
	// DvFoerderprogramm fp = EasyMock.createNiceMock( DvFoerderprogramm.class );
	// EasyMock.expect( fp.getNummer() ).andReturn( 800L ).anyTimes();
	// EasyMock.expect( fp.isDefined() ).andReturn( true ).anyTimes();
	// EasyMock.replay( fp );
	// ElerFTFoerderprogramm alleFreigegebenenFPs =
	// provider.getElerFTFoerderprogramm( DvAntragsJahr.AJ2020,
	// fp, new Date() );
	// assertThat( alleFreigegebenenFPs, notNullValue() );
	// }

}
