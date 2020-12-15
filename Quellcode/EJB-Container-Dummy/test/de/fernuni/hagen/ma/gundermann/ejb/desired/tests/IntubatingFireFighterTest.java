package de.fernuni.hagen.ma.gundermann.ejb.desired.tests;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import de.fernuni.hagen.ma.gundermann.ejb.desired.IntubatingFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.AccidentParticipant;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.Suffer;
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
    Collection<Suffer> suffer = Arrays.asList( Suffer.LOCKED );
    AccidentParticipant patient = new AccidentParticipant( suffer );
    intubatingFireFighter.free( patient );
    assertTrue( patient.isStabilized() );
  }

  @QueryTypeTest
  public void intubate() {
    Collection<Suffer> suffer = Arrays.asList( Suffer.BREATH_PROBLEMS );
    AccidentParticipant patient = new AccidentParticipant( suffer );
    intubatingFireFighter.intubate( patient );
    assertTrue( patient.isStabilized() );
  }

}
