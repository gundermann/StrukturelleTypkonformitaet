package de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.desired.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import api.RequiredTypeInstanceSetter;
import api.RequiredTypeTest;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.desired.IntubatingPatientFireFighter;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.FireState;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.business.IntubationPartient;
import spi.TriedMethodCallsInfo;

public class IntubatingPatientFireFighterTest implements TriedMethodCallsInfo {

	private IntubatingPatientFireFighter intubatingPatientFireFighter;

	private Collection<Method> calledMethods = new ArrayList<Method>();

	@RequiredTypeInstanceSetter
	public void setProvider(IntubatingPatientFireFighter intubatingFireFighter) {
		this.intubatingPatientFireFighter = intubatingFireFighter;
	}

	@RequiredTypeTest
	public void extinguishFire() {
		Fire fire = new Fire();
		addTriedMethodCall(getMethod("extinguishFire", IntubatingPatientFireFighter.class));
		FireState fireState = intubatingPatientFireFighter.extinguishFire(fire);
		assertTrue(Objects.equals(fireState.isActive(), fire.isActive()));
		assertFalse(fire.isActive());
	}

	@RequiredTypeTest
	public void intubate() {
		IntubationPartient patient = new IntubationPartient();
		addTriedMethodCall(getMethod("intubate", IntubatingPatientFireFighter.class));
		intubatingPatientFireFighter.intubate(patient);
		assertTrue(patient.isIntubated());
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
