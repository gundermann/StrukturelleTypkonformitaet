package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.tests;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.IntubatingFreeing;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Injured;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Suffer;
import spi.TriedMethodCallsInfo;
import tester.annotation.RequiredTypeInstanceSetter;
import tester.annotation.RequiredTypeTest;

public class IntubatingFreeingTest implements TriedMethodCallsInfo {

	private IntubatingFreeing intubatingFreeing;
	private Collection<Method> calledMethods = new ArrayList<Method>();

	@RequiredTypeInstanceSetter
	public void setProvider(IntubatingFreeing intubatingFireFighter) {
		this.intubatingFreeing = intubatingFireFighter;
	}

	@RequiredTypeTest
	public void free() {
		Collection<Suffer> suffer = Arrays.asList(Suffer.LOCKED);
		Injured patient = new Injured(suffer);
		intubatingFreeing.free(patient);
		addTriedMethodCall(getMethod("free", IntubatingFreeing.class));
		assertTrue(patient.isStabilized());
	}

	@RequiredTypeTest
	public void intubate() {
		Collection<Suffer> suffer = Arrays.asList(Suffer.BREATH_PROBLEMS);
		Injured patient = new Injured(suffer);
		intubatingFreeing.intubate(patient);
		addTriedMethodCall(getMethod("intubate", IntubatingFreeing.class));
		assertTrue(patient.isStabilized());
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
