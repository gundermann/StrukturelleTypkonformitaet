package de.fernuni.hagen.ma.gundermann.descostest;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.descostest.ComponentContainer;

public class InitializationTest {

  @Test
  public void testInitialization() {
    ComponentContainer container = ComponentContainer.CONTAINER;
    assertFalse( container.containerMap.isEmpty() );
  }

}
