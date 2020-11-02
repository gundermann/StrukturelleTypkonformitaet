package de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces;

import java.util.Collection;

import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Foerderprogramm;
import de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces.tests.FoerderprogrammProviderTest;
import tester.annotation.QueryTypeTestReference;

/**
 * Das ist ein sehr einfaches Interface, weshalb auch viele passende Beans über das SigMat gefunden werden (173/885 -
 * immerhin...)
 */
@QueryTypeTestReference( testClasses = FoerderprogrammProviderTest.class )
public interface FoerderprogrammeProvider {

  Collection<Foerderprogramm> getAlleFreigegebenenFPs();
}
