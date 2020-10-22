package de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces.tests;

import de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces.FoerderprogrammeProvider;
import tester.annotation.QueryTypeInstanceSetter;

public class FoerderprogrammProviderTest {

  private FoerderprogrammeProvider provider;

  @QueryTypeInstanceSetter
  public void setProvider( FoerderprogrammeProvider provider ) {
    this.provider = provider;

  }
}
