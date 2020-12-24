package de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired;

import java.util.Collection;
import java.util.Date;

import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Foerderprogramm;
import de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired.tests.MinimalFoerderprogrammProviderTest;
import tester.annotation.QueryTypeTestReference;

@QueryTypeTestReference( testClasses = MinimalFoerderprogrammProviderTest.class )
public interface MinimalFoerderprogrammeProvider {

  // Versuch 1: Nur diese Methode wurde deklariert => 149/885 relevanten Beans
  Collection<String> getAlleFreigegebenenFPs();

  // Versuch 2: diese Methode wurde zus�tzlich deklariert => 24/885 relevanten Beans
  Foerderprogramm getFoerderprogramm( String fp, int jahr, Date date );

  // Versuch 3: diese Methode wurde zus�tzlich deklariert => 1/885 relevanten Beans => Aber die Falsche!
  // (FeldBlockZonenDAO)
  // die Annahme, dass die Nummer einens F�rderprogramms als int abgebildet wird, ist falsch.
  // Idee: Matcher, der damit umgehen kann, dass primitive Typen in andere primitive Typen umgewandelt werden.
  // Foerderprogramm getFoerderprogramm( int fp, int jahr, Date date );

  // TODO Hiermit sollte versucht werden, den "whatever"-String in ein Datum umzuwandeln
  // Foerderprogramm getFoerderprogramm( String fp, int jahr, String whatever );

  // TODO auch denkbar, wenn dem Entwickler nicht bewusst ist, dass es eine technische G�ltigkeit gibt. (erg�nzen mit
  // null)
  // Den dazugeh�rigen Matcher habe ich aber noch nicht implementiert.
  // Foerderprogramm getFoerderprogramm( String fp, int jahr );

  // Versuch 4: Das funktioniert noch nicht, sollte aber. Es funktioniert nicht, weil es in Foerderprogramm kein Feld
  // vom Typ DvFoerderprogramm gibt.
  // DvFoerderprogramm getFoerderprogramm( String fp, int jahr, Date date );
}
