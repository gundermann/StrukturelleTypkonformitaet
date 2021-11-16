package de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.desired;

import api.RequiredTypeTestReference;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.desired.tests.IntubatingFreeingTest;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.business.Injured;

@RequiredTypeTestReference( testClasses = IntubatingFreeingTest.class )
public interface IntubatingFreeing {

  public void intubate( Injured injured );

  public void free( Injured injured );
}
