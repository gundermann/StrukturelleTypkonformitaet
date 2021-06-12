package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.IntubatingPatientFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.FireState;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.IntubationPartient;
import spi.CalledMethodInfo;
import tester.annotation.RequiredTypeInstanceSetter;
import tester.annotation.RequiredTypeTest;

public class IntubatingPatientFireFighterTest implements CalledMethodInfo {

	private IntubatingPatientFireFighter intubatingPatientFireFighter;

	private Collection<Method> calledMethods = new ArrayList<Method>();

	@RequiredTypeInstanceSetter
	public void setProvider(IntubatingPatientFireFighter intubatingFireFighter) {
		this.intubatingPatientFireFighter = intubatingFireFighter;
	}

	@RequiredTypeTest
	public void free() {
		Fire fire = new Fire();
		FireState fireState = intubatingPatientFireFighter.extinguishFire(fire);
		addCalledMethod(getMethod("free", IntubatingPatientFireFighter.class));
		assertTrue(Objects.equals(fireState.isActive(), fire.isActive()));
		assertFalse(fire.isActive());
	}

	@RequiredTypeTest
	public void intubate() {
		IntubationPartient patient = new IntubationPartient();
		intubatingPatientFireFighter.intubate(patient);
		addCalledMethod(getMethod("intubate", IntubatingPatientFireFighter.class));
		assertTrue(patient.isIntubated());
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
