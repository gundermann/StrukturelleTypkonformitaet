package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import api.RequiredTypeInstanceSetter;
import api.RequiredTypeTest;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.FirstAidProvidingFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Injured;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Suffer;
import spi.TriedMethodCallsInfo;

public class FirstAidProvidingFireFighterTest implements TriedMethodCallsInfo {

	private FirstAidProvidingFireFighter firstAidProvidingFireFighter;
	private Collection<Method> calledMethods = new ArrayList<Method>();

	@RequiredTypeInstanceSetter
	public void setProvider(FirstAidProvidingFireFighter intubatingFireFighter) {
		this.firstAidProvidingFireFighter = intubatingFireFighter;
	}

	@RequiredTypeTest
	public void extinguishFire() {
		Fire fire = new Fire();
		addTriedMethodCall(getMethod("extinguishFire", FirstAidProvidingFireFighter.class));
		firstAidProvidingFireFighter.extinguishFire(fire);
		assertFalse(fire.isActive());
	}

	@RequiredTypeTest
	public void provideFirstAid() {
		Collection<Suffer> suffer = Arrays.asList(Suffer.LOCKED, Suffer.BREATH_PROBLEMS, Suffer.OPEN_WOUND);
		Injured patient = new Injured(suffer);
		addTriedMethodCall(getMethod("provideFirstAid", FirstAidProvidingFireFighter.class));
		firstAidProvidingFireFighter.provideFirstAid(patient);
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
