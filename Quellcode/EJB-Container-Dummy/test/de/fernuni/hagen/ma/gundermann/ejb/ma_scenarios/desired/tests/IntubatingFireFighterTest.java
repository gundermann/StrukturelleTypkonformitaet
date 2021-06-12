package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.IntubatingFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans.FireState;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Injured;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Suffer;
import spi.CalledMethodInfo;
import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;

public class IntubatingFireFighterTest implements CalledMethodInfo {

	private IntubatingFireFighter intubatingFireFighter;
	private Collection<Method> calledMethods = new ArrayList<Method>();

	@QueryTypeInstanceSetter
	public void setProvider(IntubatingFireFighter intubatingFireFighter) {
		this.intubatingFireFighter = intubatingFireFighter;
	}

	@QueryTypeTest(testedSingleMethod = "extinguishFire")
	public void free() {
		Fire fire = new Fire();
		FireState fireState = intubatingFireFighter.extinguishFire(fire);
		addCalledMethod(getMethod("extinguishFire", IntubatingFireFighter.class));
		assertTrue(Objects.equals(fireState.isActive(), fire.isActive()));
		assertFalse(fire.isActive());
	}

	@QueryTypeTest(testedSingleMethod = "intubate")
	public void intubate() {
		Collection<Suffer> suffer = Arrays.asList(Suffer.BREATH_PROBLEMS);
		Injured patient = new Injured(suffer);
		intubatingFireFighter.intubate(patient);
		addCalledMethod(getMethod("intubate", IntubatingFireFighter.class));
		assertTrue(patient.isStabilized());
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
