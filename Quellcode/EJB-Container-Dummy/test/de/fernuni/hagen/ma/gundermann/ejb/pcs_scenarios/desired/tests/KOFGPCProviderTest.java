package de.fernuni.hagen.ma.gundermann.ejb.pcs_scenarios.desired.tests;


import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import DE.data_experts.allg.LandesVariante;
import DE.data_experts.profi.profilcs.antrag.dv.business.DvFoerdergegenstand;
import DE.data_experts.profi.profilcs.antrag2015.eler.ft.stammdaten.business.ElerFTKoFoerdergegenstand;
import DE.data_experts.profi.profilcs.antrag2015.stammdaten.business.impl.Produktcode;
import DE.data_experts.profi.profilcs.elerft.migration.business.sigmang.KOFGPCProvider;
import DE.data_experts.profi.util.DvAntragsJahr;
import DE.data_experts.profi.util.allg.DvFoerderprogramm;
import spi.TriedMethodCallsInfo;
import api.RequiredTypeInstanceSetter;
import api.RequiredTypeTest;

public class KOFGPCProviderTest implements TriedMethodCallsInfo {

  private KOFGPCProvider provider;

  private Collection<Method> calledMethods = new ArrayList<Method>();

  @RequiredTypeInstanceSetter
  public void setProvider( KOFGPCProvider provider ) {
    this.provider = provider;
  }

  @RequiredTypeTest
  public void testKOFGsCollection() {
    if ( LandesVariante.get().equals( "MV" ) ) {
      // Easymock darf hier gar nicht verwendet werden, weil die equals-Methode ansonsten nicht funktioniert.
      DvFoerderprogramm fp = DvFoerderprogramm.Factory.valueOf( DvFoerderprogramm.FP508 );
      addTriedMethodCall( getMethod( "getKOFGsVonFP", KOFGPCProvider.class ) );
      Collection<ElerFTKoFoerdergegenstand> kofGsVonFP = provider.getKOFGsVonFP( fp );
      assertThat( kofGsVonFP, notNullValue() );
      assertThat( kofGsVonFP.isEmpty(), equalTo( false ) );
      assertThat( kofGsVonFP.stream().anyMatch( fg -> fg.getCode().equals( "KO508" ) ), equalTo( true ) );
    }
  }

  @RequiredTypeTest
  public void testPCsCollection() {
    if ( LandesVariante.get().equals( "MV" ) ) {
      DvFoerdergegenstand fg = DvFoerdergegenstand.Factory.valueOf( 20155080025L );
      addTriedMethodCall( getMethod( "getPCsZuKOFG", KOFGPCProvider.class ) );
      Collection<Produktcode> pcs = provider.getPCsZuKOFG( fg, DvAntragsJahr.AJ2020 );
      assertThat( pcs, notNullValue() );
      assertThat( pcs.isEmpty(), equalTo( false ) );
    }
  }

  @Override
  public void addTriedMethodCall( Method m ) {
    this.calledMethods.add( m );
  }

  @Override
  public Collection<Method> getTriedMethodCalls() {
    return calledMethods;
  }

}
