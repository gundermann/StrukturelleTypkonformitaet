package de.fernuni.hagen.ma.gundermann.ejb.provided.beans;

import de.fernuni.hagen.ma.gundermann.ejb.provided.business.IntubationPartient;

public interface Intubator {

  public void intubate( IntubationPartient patient );
}
