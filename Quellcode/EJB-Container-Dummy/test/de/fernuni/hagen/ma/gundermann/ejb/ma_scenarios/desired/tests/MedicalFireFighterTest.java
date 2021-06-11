package de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Objects;

import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.MedCabinet;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.desired.MedicalFireFighter;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Fire;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Injured;
import de.fernuni.hagen.ma.gundermann.ejb.ma_scenarios.provided.business.Suffer;
import spi.PivotMethodInfoContainer;
import spi.FirstCalledMethodInfo;
import tester.annotation.QueryTypeInstanceSetter;

public class MedicalFireFighterTest implements FirstCalledMethodInfo {

  private MedicalFireFighter medicalFireFighter;

  private PivotMethodInfoContainer pmiContainer = new PivotMethodInfoContainer();

  @QueryTypeInstanceSetter
  public void setMedicalFireFighter( MedicalFireFighter medicalFireFighter ) {
    this.medicalFireFighter = medicalFireFighter;
  }

  public void heal() {
    Injured injured = new Injured( Arrays.asList( Suffer.BREATH_PROBLEMS ) );
    MedCabinet med = new MedCabinet();
    medicalFireFighter.heal( injured, med );
    markPivotMethodCallExecuted();
    assertTrue( injured.getSuffers().isEmpty() );
  }

  public void extinguishFire() {
    Fire fire = new Fire();
    boolean isFireActive = medicalFireFighter.extinguishFire( fire );
    markPivotMethodCallExecuted();
    assertTrue( Objects.equals( isFireActive, fire.isActive() ) );
    assertFalse( isFireActive );
  }

  @Override
  public PivotMethodInfoContainer getPivotMethodInfoContainer() {
    return pmiContainer;
  }

}
