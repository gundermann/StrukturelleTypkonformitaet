package de.fernuni.hagen.ma.gundermann.ejb;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.ejb.desiredinterfaces.FoerderprogrammeProvider;

public class FindMatchingEJBInterfaceTest {

  @Test
  public void findMatchingFoerderprogrammeProvider() {
    Class<FoerderprogrammeProvider> desiredInterface = FoerderprogrammeProvider.class;
    EJBContainer.CONTAINER.getDesiredBean( desiredInterface );
  }
}
