
package de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.provided.beans.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.easymock.EasyMock;

import DE.data_experts.profi.fp.dv.AntragsVorgangsTyp;
import DE.data_experts.profi.profilcs.antrag.dv.business.DvFoerdergegenstand;
import DE.data_experts.profi.profilcs.antrag.dv.business.DvProduktcode;
import DE.data_experts.profi.profilcs.antrag.dv.business.KuerzungsgrundCode;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.ElerFTFoerderprogramm;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Finanzierungskonfiguration;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Finanzierungsschluessel;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Foerdergegenstand;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.FoerdergegenstandGruppe;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Foerderprogramm;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Landesmassnahme;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Landesmassnahme2Foerdergegenstand;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Produktcode;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.ProduktcodeArt;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Vorgang;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.ejb.StammdatenAuskunftService;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.query.FinanzierungskonfigurationQuery;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.query.FinanzierungsschluesselQuery;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.query.FoerdergegenstandQuery;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.query.Landesmassnahme2FoerdergegenstandQuery;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.query.LandesmassnahmeQuery;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.query.ProduktcodeQuery;
import DE.data_experts.profi.util.DvAntragsJahr;
import DE.data_experts.profi.util.allg.DvFoerderprogramm;

public class STDAuskunftImpl implements StammdatenAuskunftService {

  @Override
  public Collection<Foerdergegenstand> getUnterFoerdergegenstaende( DvAntragsJahr jahr,
      Collection<DvFoerdergegenstand> oberFgs ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<FoerdergegenstandGruppe> getFoerdergegenstandGruppenZuFgs( DvAntragsJahr jahr,
      Collection<DvFoerdergegenstand> fgs ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<DvFoerdergegenstand, DvFoerdergegenstand> getOberFgJeUnterFg( DvAntragsJahr jahr ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Foerdergegenstand> getFoerdergegenstaende( DvFoerderprogramm fp ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Foerdergegenstand> getFoerdergegenstaende( FoerdergegenstandQuery query ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Foerdergegenstand> getFoerdergegenstaende( Landesmassnahme lm ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Foerdergegenstand getFoerdergegenstand( FoerdergegenstandQuery query ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Foerderprogramm getFoerderprogramm( DvAntragsJahr jahr, DvFoerderprogramm fp, Date date ) {
    ElerFTFoerderprogramm fpMock = EasyMock.createNiceMock( ElerFTFoerderprogramm.class );
    EasyMock.expect( fpMock.getFoerderprogramm() ).andReturn( fp ).anyTimes();
    EasyMock.replay( fpMock );
    return fpMock;
  }

  @Override
  public Foerderprogramm getFoerderprogramm( Foerdergegenstand fg ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Foerderprogramm> getFoerderprogramme( Date datum ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Landesmassnahme getLandesmassnahme( Long lmId ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Landesmassnahme> getLandesmassnahmen( DvAntragsJahr jahr, DvFoerderprogramm fp ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Landesmassnahme> getLandesmassnahmen( DvAntragsJahr jahr, Foerdergegenstand fg ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Landesmassnahme> getLandesmassnahmen( LandesmassnahmeQuery query ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Vorgang> getVorgaenge() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Vorgang> getVorgaenge( AntragsVorgangsTyp typ ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Vorgang> getVorgaenge( Date date, AntragsVorgangsTyp typ ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Vorgang> getVorgaenge( Date date, DvFoerderprogramm dvFp ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Vorgang getVorgang( DvAntragsJahr jahrFp, DvFoerderprogramm dvFp, Date date, AntragsVorgangsTyp typ,
      DvAntragsJahr jahrVorgang ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Vorgang getVorgang( DvFoerderprogramm dvFp, Date date, AntragsVorgangsTyp typ, DvAntragsJahr jahrVorgang ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Vorgang> getVorgaenge( DvFoerderprogramm dvFp, Date date, AntragsVorgangsTyp typ ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<String> getAblehnungsgrundCodes( Foerderprogramm foerderprogramm, DvAntragsJahr jahr,
      KuerzungsgrundCode kuerzungsgrundCode ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Produktcode> getProduktcodes( ProduktcodeQuery query ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Produktcode> getProduktcodes( DvAntragsJahr jahr, DvFoerderprogramm fp ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Produktcode getProduktcode( ProduktcodeQuery query ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BigDecimal getKappungBetrag( DvFoerdergegenstand fg, DvAntragsJahr aj ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BigDecimal getKappungMenge( DvFoerdergegenstand fg, DvAntragsJahr aj ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BigDecimal getBeihilfesatz( DvAntragsJahr jahr, DvFoerdergegenstand fg, Integer lfdNrVerpflJahr ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Produktcode getProduktcode( DvAntragsJahr jahr, DvFoerdergegenstand fg, ProduktcodeArt pcArt ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Produktcode getProduktcode( DvAntragsJahr arg0, DvFoerdergegenstand arg1, ProduktcodeArt arg2,
      Finanzierungsschluessel arg3 ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Produktcode> getProduktcodes( DvAntragsJahr arg0, Finanzierungsschluessel arg1 ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Produktcode> getProduktcodes( DvAntragsJahr arg0, DvFoerdergegenstand arg1,
      Finanzierungsschluessel arg2 ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<DvFoerdergegenstand, DvProduktcode> getProduktcodesJeFg( DvFoerderprogramm arg0, DvAntragsJahr arg1,
      Collection<DvFoerdergegenstand> arg2, ProduktcodeArt arg3, Finanzierungsschluessel arg4 ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Landesmassnahme2Foerdergegenstand> getLandesmassnahmen2Foerdergegenstaende(
      Landesmassnahme2FoerdergegenstandQuery query ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Finanzierungsschluessel> getFinanzierungsschluessel( FinanzierungsschluesselQuery query ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Finanzierungskonfiguration> getFinanzierungskonfigurationen(
      Collection<Finanzierungsschluessel> fischel, DvAntragsJahr jahr ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Finanzierungskonfiguration> getFinanzierungskonfigurationen( DvAntragsJahr jahr,
      DvFoerderprogramm fp, Long produktcodeExtern ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Finanzierungskonfiguration getFinanzierungskonfigurationen( DvAntragsJahr jahr, DvFoerderprogramm fp,
      DvFoerdergegenstand fg ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Foerdergegenstand> getFoerdergegenstaendeZuFinanzierungsschluessel( DvFoerderprogramm fp,
      Finanzierungsschluessel fisch, DvAntragsJahr jahr ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Produktcode> getProduktcodes( Collection<Finanzierungskonfiguration> finKonfigs ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Produktcode> getProduktcodes( DvAntragsJahr jahr, DvFoerdergegenstand fp ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Finanzierungskonfiguration> getFinanzierungskonfigurationen(
      FinanzierungskonfigurationQuery query ) {
    // TODO Auto-generated method stub
    return null;
  }

}
