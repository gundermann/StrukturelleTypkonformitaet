package de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces;

import java.util.Collection;
import java.util.Date;

import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Foerderprogramm;
import de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces.tests.MinimalFoerderprogrammProviderTest;
import tester.annotation.QueryTypeTestReference;

@QueryTypeTestReference( testClasses = MinimalFoerderprogrammProviderTest.class )
public interface MinimalFoerderprogrammeProvider {

  // Versuch 1: Nur diese Methode wurde deklariert => 149/885 relevanten Beans
  Collection<String> getAlleFreigegebenenFPs();

  // Versuch 2: diese Methode wurde zusätzlich deklariert => 24/885 relevanten Beans
  Foerderprogramm getFoerderprogramm( String fp, int jahr, Date date );
}
