package de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired;


import java.util.Collection;

import DE.data_experts.profi.profilcs.antrag.dv.business.DvFoerdergegenstand;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.ElerFTKoFoerdergegenstand;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Produktcode;
import DE.data_experts.profi.util.DvAntragsJahr;
import DE.data_experts.profi.util.allg.DvFoerderprogramm;
import api.RequiredTypeTestReference;
import de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired.tests.KOFGPCProviderTest;

//Shell-Param: DE.data_experts.profi.profilcs.elerft.migration.business.sigmang.KOFGPCProvider
@RequiredTypeTestReference( testClasses = KOFGPCProviderTest.class )
public interface KOFGPCProvider {

  // Ziel: ElerFTStammdatenAuskunftService::
  // getElerFTKoFoerdergegenstaende( DvFoerderprogramm ) : Collection<Foerdergegenstand>
  Collection<ElerFTKoFoerdergegenstand> getKOFGsVonFP( DvFoerderprogramm fp );
  // Hier hatte ich zuerst gedacht, dass ich ein AJ mit übergeben muss.
  // --------------------------------
  // Folgendes geht noch nicht:
  // Collection<DvFoerdergegenstand> getKOFGsVonFP( DvFoerderprogramm fp );
  // siehe EJB-Containter-Dummy: de.fernuni.hagen.ma.gundermann.ejb.desired.MinimalFoerderprogrammeProvider Versuch 4

  // Ziel: StammdatenAuskunftService::getProduktcodes(DvAntragsJahr, DvFoerdergegenstand) : Collection<Produktcode>
  Collection<Produktcode> getPCsZuKOFG( DvFoerdergegenstand fg, DvAntragsJahr aj );
  // Geht das auch mit Collection<DvProduktcode>? Immerhin kann Produktcode einen DvProduktcode liefern.

}
