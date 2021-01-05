package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.IntubatingFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.AccidentParticipant;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Suffer;
import tester.annotation.QueryTypeInstanceSetter;
import tester.annotation.QueryTypeTest;

public class IntubatingFireFighterTest {

  private IntubatingFireFighter intubatingFireFighter;

  @QueryTypeInstanceSetter
  public void setProvider( IntubatingFireFighter intubatingFireFighter ) {
    this.intubatingFireFighter = intubatingFireFighter;
  }

  @QueryTypeTest
  public void free() {
    Fire fire = new Fire();
    intubatingFireFighter.extinguishFire( fire );
    assertFalse( fire.isActive() );
  }

  @QueryTypeTest
  public void intubate() {
    Collection<Suffer> suffer = Arrays.asList( Suffer.BREATH_PROBLEMS );
    AccidentParticipant patient = new AccidentParticipant( suffer );
    intubatingFireFighter.intubate( patient );
    assertTrue( patient.isStabilized() );
  }

}
