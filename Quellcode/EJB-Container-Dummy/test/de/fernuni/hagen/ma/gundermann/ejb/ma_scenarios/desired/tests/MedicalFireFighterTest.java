package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.MedCabinet;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.MedicalFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Injured;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Suffer;
import spi.CalledMethodInfo;
import tester.annotation.RequiredTypeInstanceSetter;
import tester.annotation.RequiredTypeTest;

public class MedicalFireFighterTest implements CalledMethodInfo {

	private MedicalFireFighter medicalFireFighter;

	private Collection<Method> calledMethods = new ArrayList<Method>();

	@RequiredTypeInstanceSetter
	public void setMedicalFireFighter(MedicalFireFighter medicalFireFighter) {
		this.medicalFireFighter = medicalFireFighter;
	}

	@RequiredTypeTest
	public void heal() {
		Injured injured = new Injured(Arrays.asList(Suffer.BREATH_PROBLEMS));
		MedCabinet med = new MedCabinet();
		medicalFireFighter.heal(injured, med);
		addCalledMethod(getMethod("heal", MedicalFireFighter.class));
		assertTrue(injured.getSuffers().isEmpty());
	}

	@RequiredTypeTest
	public void extinguishFire() {
		Fire fire = new Fire();
		boolean isFireActive = medicalFireFighter.extinguishFire(fire);
		addCalledMethod(getMethod("extinguishFire", MedicalFireFighter.class));
		assertTrue(Objects.equals(isFireActive, fire.isActive()));
		assertFalse(isFireActive);
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
