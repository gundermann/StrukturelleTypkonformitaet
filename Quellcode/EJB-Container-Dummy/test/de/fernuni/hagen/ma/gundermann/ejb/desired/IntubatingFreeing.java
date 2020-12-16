package de.fernuni.hagen.ma.gundermann.ejb.desired;

import de.fernuni.hagen.ma.gundermann.ejb.desired.tests.IntubatingFreeingTest;
import de.fernuni.hagen.ma.gundermann.ejb.provided.business.AccidentParticipant;
import tester.annotation.QueryTypeTestReference;

@QueryTypeTestReference( testClasses = IntubatingFreeingTest.class )
public interface IntubatingFreeing {

  public void intubate( AccidentParticipant injured );

  public void free( AccidentParticipant injured );
}
