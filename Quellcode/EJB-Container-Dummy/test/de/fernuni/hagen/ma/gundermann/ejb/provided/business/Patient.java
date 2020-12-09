package de.fernuni.hagen.ma.gundermann.ejb.provided.business;

public class Patient {

  private boolean intubated = false;

  private boolean insured = true;

  private boolean firstAidProvided = false;

  public boolean isIntubated() {
    return intubated;
  }

  public void setIntubated( boolean intubated ) {
    this.intubated = intubated;
  }

  public boolean isInsured() {
    return insured;
  }

  public boolean isFirstAidProvided() {
    return firstAidProvided;
  }

  public void setFirstAidProvided( boolean firstAidProvided ) {
    this.firstAidProvided = firstAidProvided;
  }

}
