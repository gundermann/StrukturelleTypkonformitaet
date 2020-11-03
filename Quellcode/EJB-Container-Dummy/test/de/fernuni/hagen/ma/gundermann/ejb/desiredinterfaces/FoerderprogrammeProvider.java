package de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces;

import java.util.Collection;
import java.util.Date;

import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Foerderprogramm;
import DE.data_experts.profi.util.DvAntragsJahr;
import DE.data_experts.profi.util.allg.DvFoerderprogramm;
import de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces.tests.FoerderprogrammProviderTest;
import tester.annotation.QueryTypeTestReference;

@QueryTypeTestReference( testClasses = FoerderprogrammProviderTest.class )
public interface FoerderprogrammeProvider {

  // Versuch 1: Nur diese Methode wurde deklariert => 149/885 relevanten Beans
  Collection<Foerderprogramm> getAlleFreigegebenenFPs();

  // Versuch 2: diese Methode wurde zusätzlich deklariert => 3/885 relevanten Beans
  Foerderprogramm getFoerderprogramm( DvAntragsJahr jahr, DvFoerderprogramm fp, Date date );
}
