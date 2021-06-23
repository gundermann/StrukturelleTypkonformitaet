package de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Foerderprogramm;
import DE.data_experts.profi.util.allg.DvFoerderprogramm;
import de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired.MinimalFoerderprogrammeProvider;
import spi.TriedMethodCallsInfo;
import tester.annotation.RequiredTypeInstanceSetter;
import tester.annotation.RequiredTypeTest;

public class MinimalFoerderprogrammProviderTest implements TriedMethodCallsInfo {

	private MinimalFoerderprogrammeProvider provider;

	private Collection<Method> calledMethods = new ArrayList<Method>();

	@RequiredTypeInstanceSetter
	public void setProvider(MinimalFoerderprogrammeProvider provider) {
		this.provider = provider;
	}

	@RequiredTypeTest
	public void testEmptyCollection() {
		Collection<String> alleFreigegebenenFPs = provider.getAlleFreigegebenenFPs();
		addTriedMethodCall(getMethod("getAlleFreigegebenenFPs", MinimalFoerderprogrammeProvider.class));
		assertThat(alleFreigegebenenFPs, notNullValue());
	}

	@RequiredTypeTest
	public void testGetFoerderprogramm() {
		String fpCode = "123";
		Foerderprogramm fp = provider.getFoerderprogramm(fpCode, 2015, new Date());
		addTriedMethodCall(getMethod("getFoerderprogramm", MinimalFoerderprogrammeProvider.class));
		assertThat(fp, notNullValue());
		DvFoerderprogramm dvFP = fp.getFoerderprogramm();
		assertThat(dvFP, notNullValue());

		String code = dvFP.getCode();
		// assertTrue( Objects.equals( fpCode, code ) );
		assertThat(fpCode, equalTo(code));
		// WARNING assertThat funktioniert nur, wenn das hamcrest-JAR �ber den
		// JUnit-JAR steht (BuildPath > Export-Order)
		// ERROR: class "org.hamcrest.Matchers"'s signer information does not match
		// signer information of other classes in
		// the same
		// package
		// siehe issue: https://code.google.com/archive/p/hamcrest/issues/128

	}

	@Override
	public void addTriedMethodCall(Method m) {
		calledMethods.add(m);
	}

	@Override
	public Collection<Method> getTriedMethodCalls() {
		return calledMethods;
	}

}
