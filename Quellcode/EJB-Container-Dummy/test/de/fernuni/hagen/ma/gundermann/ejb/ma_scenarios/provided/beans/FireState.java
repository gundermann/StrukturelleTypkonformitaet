package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.beans;

public class FireState {

  private boolean isActive;

  public FireState( boolean isActive ) {
    this.isActive = isActive;
  }

  public boolean isActive() {
    return isActive;
  }

}