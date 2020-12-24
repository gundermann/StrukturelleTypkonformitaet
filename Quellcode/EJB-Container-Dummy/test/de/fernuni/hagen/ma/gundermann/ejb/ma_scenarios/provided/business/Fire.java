package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business;

public class Fire {

  private boolean active = true;

  public void extinguish() {
    this.active = false;
  }

  public boolean isActive() {
    return active;
  }

}
