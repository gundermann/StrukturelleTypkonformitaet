package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.FirstAidProvidingFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.AccidentParticipant;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Suffer;
import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;

public class FirstAidProvidingFireFighterTest {

  private FirstAidProvidingFireFighter firstAidProvidingFireFighter;

  @QueryTypeInstanceSetter
  public void setProvider( FirstAidProvidingFireFighter intubatingFireFighter ) {
    this.firstAidProvidingFireFighter = intubatingFireFighter;
  }

  @QueryTypeTest
  public void extinguishFire() {
    Fire fire = new Fire();
    firstAidProvidingFireFighter.extinguishFire( fire );
    assertFalse( fire.isActive() );
  }

  @QueryTypeTest
  public void provideFirstAid() {
    Collection<Suffer> suffer = Arrays.asList( Suffer.LOCKED, Suffer.BREATH_PROBLEMS, Suffer.OPEN_WOUND );
    AccidentParticipant patient = new AccidentParticipant( suffer );
    firstAidProvidingFireFighter.provideFirstAid( patient );
    assertTrue( patient.isStabilized() );
  }

}
