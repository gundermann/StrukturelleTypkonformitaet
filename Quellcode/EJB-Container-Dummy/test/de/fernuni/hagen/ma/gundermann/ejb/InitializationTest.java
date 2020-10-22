package de.fernuni.hagen.ma.gundermann.ejb;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class InitializationTest {

  @Test
  public void testInitialization() {
    EJBContainer container = EJBContainer.CONTAINER;
    assertFalse( container.containerMap.isEmpty() );
  }

}
