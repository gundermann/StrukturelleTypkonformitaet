package de.fernuni.hagen.ma.gundermann.ejb.desired;

import java.util.Collection;
import java.util.Date;

import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.ElerFTFoerderprogramm;
import DE.data_experts.profi.util.DvAntragsJahr;
import DE.data_experts.profi.util.allg.DvFoerderprogramm;
import de.fernuni.hagen.ma.gundermann.ejb.desired.tests.ElerFTFoerderprogrammProviderTest;
import tester.annotation.QueryTypeTestReference;

@QueryTypeTestReference( testClasses = ElerFTFoerderprogrammProviderTest.class )
public interface ElerFTFoerderprogrammeProvider {

  // Versuch 1: Nur diese Methode wurde deklariert => 173/885 relevanten Beans
  Collection<ElerFTFoerderprogramm> getAlleFreigegebenenFPs();

  // Versuch 2: diese Methode wurde zusätzlich deklariert => 3/885 relevanten Beans
  ElerFTFoerderprogramm getElerFTFoerderprogramm( DvAntragsJahr jahr, DvFoerderprogramm fp, Date date );
}
