package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.tests.IntubatingFreeingTest;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Injured;
import tester.annotation.QueryTypeTestReference;

@QueryTypeTestReference( testClasses = IntubatingFreeingTest.class )
public interface IntubatingFreeing {

  public void intubate( Injured injured );

  public void free( Injured injured );
}
