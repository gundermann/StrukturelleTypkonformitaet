package de.fernuni.hagen.ma.gundermann.ejb.beanimplementations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import DE.data_experts.profi.fp.dv.AntragsVorgangsTyp;
import DE.data_experts.profi.profilcs.antrag.dv.business.DvFoerdergegenstand;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.DvEftOekoFoerdergegenstandGruppe;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.ElerFTFoerderprogramm;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.ElerFTKoFoerdergegenstand;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.ElerFTKombiKzFpFoerdergegenstand;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.ElerFTKzFpFoerdergegenstand;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.ElerFTKzFpFoerdergegenstand2Foerderfaehigkeit;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.ElerFTTierFoerdergegenstand;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.ElerFTVorhaben;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.Verpflichtungszeitraum;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.impl.AenderungscodeProperties;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.impl.ElerFTAenderung;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.impl.ElerFTAenderung2ElerFTFP;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.impl.FeststellungsCodeVerpflichtung2FP;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.impl.FeststellungscodeVerpflichtungImpl;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.impl.VerpflichtungsGegenstandImpl;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.ejb.ElerFTStammdatenAuskunftService;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.query.AbstractElerFTFoerdergegenstandQuery;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.query.AenderungscodePropertiesQuery;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.query.ElerFTFoerderprogrammQuery;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.query.FeststellungsCodeVerpflichtung2FPQuery;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.query.FeststellungscodeVerpflichtungImplQuery;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.query.VerpflichtungsGegenstandImplQuery;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Foerdergegenstand;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Landesmassnahme;
import DE.data_experts.profi.profilcs.elerft.vp.schnittstellen.api.kontrollen.FeststellungsCodeVerpflichtung;
import DE.data_experts.profi.profilcs.elerft.vp.schnittstellen.api.verpflichtungen.VerpflichtungsGegenstand;
import DE.data_experts.profi.profilcs.flaechen_dv.business.DvKennzeichenFP;
import DE.data_experts.profi.util.DvAntragsJahr;
import DE.data_experts.profi.util.allg.DvFoerderprogramm;
import DE.data_experts.profi.util.allg.DvUntermassnahme;

public class EftSTDAuskunftImpl implements ElerFTStammdatenAuskunftService {

  @Override
  public Collection<ElerFTTierFoerdergegenstand> getAlleElerFTTierFoerdergegenstaende( DvFoerderprogramm fp,
      DvAntragsJahr jahr, AntragsVorgangsTyp antragsTyp ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ElerFTFoerderprogramm getFoerderprogramm( DvAntragsJahr jahr, DvFoerderprogramm fp, Date date ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<ElerFTFoerderprogramm> getFoerderprogramme( Date date ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<ElerFTFoerderprogramm> getAlleFoerderprogramme() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<ElerFTFoerderprogramm> getAlleFreigegebenenFoerderprogramme() {
    return new ArrayList<>();
  }

  @Override
  public Collection<ElerFTFoerderprogramm> getAlleFreigegebenenFoerderprogramme( AntragsVorgangsTyp typ ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<ElerFTFoerderprogramm> getFoerderprogramme( ElerFTFoerderprogrammQuery query ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ElerFTFoerderprogramm getFoerderprogramm( ElerFTFoerderprogrammQuery query ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T extends Foerdergegenstand, Q extends AbstractElerFTFoerdergegenstandQuery<T, Q>> Collection<T> getFoerdergegenstaende(
      Q query ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<ElerFTKzFpFoerdergegenstand> getElerFTKzFpFoerdergegenstaende( DvFoerderprogramm fp ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<ElerFTKzFpFoerdergegenstand> getElerFTKzFpFoerdergegenstaende( DvFoerderprogramm fp,
      DvAntragsJahr fuerAJ ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<ElerFTKzFpFoerdergegenstand> getElerFTKzFpFoerdergegenstaende( DvFoerderprogramm fp,
      DvUntermassnahme dvUm, DvAntragsJahr fuerAJ ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<ElerFTAenderung2ElerFTFP> getElerFTAenderung2ElerFTFP( ElerFTAenderung aenderung ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<ElerFTAenderung2ElerFTFP> getElerFTAenderung2ElerFTFP( DvFoerderprogramm fp ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ElerFTAenderung2ElerFTFP getElerFTAenderung2ElerFTFP( ElerFTAenderung aenderung, DvFoerderprogramm fp ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<ElerFTKoFoerdergegenstand> getElerFTKoFoerdergegenstaende( DvFoerderprogramm fp ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<ElerFTKoFoerdergegenstand> getElerFTKoFoerdergegenstaende( DvFoerderprogramm fp,
      DvUntermassnahme dvUm, DvAntragsJahr fuerAJ ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<ElerFTKzFpFoerdergegenstand> getAlleElerFTKzFpFoerdergegenstaende() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<ElerFTKombiKzFpFoerdergegenstand> getAlleElerFTKombiKzFpFoerdergegenstaende() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<ElerFTKzFpFoerdergegenstand> getElerFTKzFpFoerdergegenstaende(
      ElerFTKombiKzFpFoerdergegenstand fg ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<ElerFTKoFoerdergegenstand> getAlleElerFTKoFoerdergegenstaende() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Verpflichtungszeitraum getVerpflichtungszeitraum( DvFoerderprogramm dvFp, DvAntragsJahr eaj ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getStandardAnzahlZahlungen( Landesmassnahme lm, DvAntragsJahr eaj ) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getStandardAnzahlZahlungen( DvUntermassnahme untermassnahme, DvAntragsJahr eaj ) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxStandardAnzahlZahlungen( DvFoerderprogramm dvFp, DvAntragsJahr eaj ) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getDifferenzJahrVerpflbeginnEAJ( DvFoerderprogramm dvFp, DvAntragsJahr aj ) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Collection<AenderungscodeProperties> getAenderungscodePropertiesList( AenderungscodePropertiesQuery query ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<VerpflichtungsGegenstand> getVerpflichtungsGegenstandList(
      VerpflichtungsGegenstandImplQuery query ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public VerpflichtungsGegenstandImpl getVerpflichtungsGegenstandImpl( VerpflichtungsGegenstandImplQuery query ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public FeststellungscodeVerpflichtungImpl getFeststellungscodeVerpflichtungImpl(
      FeststellungscodeVerpflichtungImplQuery query ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<FeststellungsCodeVerpflichtung> getFeststellungscodeVerpflichtungList(
      FeststellungscodeVerpflichtungImplQuery query ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public FeststellungsCodeVerpflichtung2FP getFeststellungsCodeVerpflichtung2FP(
      FeststellungsCodeVerpflichtung2FPQuery query ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<DvFoerdergegenstand, Collection<DvKennzeichenFP>> getKzFpJeFg( Collection<DvFoerdergegenstand> fgs,
      DvAntragsJahr jahr ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ElerFTKzFpFoerdergegenstand2Foerderfaehigkeit getElerFTKzFpFoerdergegenstand2Foerderfaehigkeit(
      DvFoerdergegenstand dvFg, DvAntragsJahr aj ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ElerFTVorhaben getVorhaben2Foerdergegenstand( DvFoerdergegenstand dvFg, DvAntragsJahr aj ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<DvFoerdergegenstand> getAlleFg2Vorhaben( ElerFTVorhaben vorhaben, DvAntragsJahr aj ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DvEftOekoFoerdergegenstandGruppe getOekoFgGruppe2Foerdergegenstand( DvFoerdergegenstand dvFg ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<DvFoerdergegenstand> getAlleFg2OekoFgGruppe( DvEftOekoFoerdergegenstandGruppe oekoFgGruppe ) {
    // TODO Auto-generated method stub
    return null;
  }

}
