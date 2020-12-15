package de.fernuni.hagen.ma.gundermann.ejb.desired;

import de.fernuni.hagen.ma.gundermann.ejb.desired.tests.IntubatingFireFighterTest;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.AccidentParticipant;
import tester.annotation.QueryTypeTestReference;

@QueryTypeTestReference( testClasses = IntubatingFireFighterTest.class )
public interface IntubatingFireFighter {

  public void intubate( AccidentParticipant injured );

  public void free( AccidentParticipant injured );
}
