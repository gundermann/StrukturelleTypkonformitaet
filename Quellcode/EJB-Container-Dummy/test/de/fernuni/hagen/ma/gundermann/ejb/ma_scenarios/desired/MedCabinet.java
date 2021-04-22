package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Medicine;

public class MedCabinet {

  private Medicine med;

  public void fill( Medicine newMed ) {
    this.med = newMed;
  }

  public Medicine getMed() {
    return med;
  }

}
