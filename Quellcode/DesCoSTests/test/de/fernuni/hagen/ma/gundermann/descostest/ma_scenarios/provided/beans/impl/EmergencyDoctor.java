package de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.impl;

import java.util.ArrayList;
import java.util.Collection;

import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.Doctor;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.beans.Intubator;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.business.Injured;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.business.IntubationPartient;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.business.Medicine;
import de.fernuni.hagen.ma.gundermann.descostest.ma_scenarios.provided.business.Suffer;

public class EmergencyDoctor implements Doctor, Intubator {

  @Override
  public void provideHeartbeatMassage( Injured injured ) {
    injured.healSuffer( Suffer.NO_HEATBEAT );
  }

  @Override
  public void nurseWounds( Injured injured ) {
    injured.healSuffer( Suffer.OPEN_WOUND );
  }

  @Override
  public void stablilizeBrokenBones( Injured injured ) {
    injured.healSuffer( Suffer.BROKEN_BONE );
  }

  @Override
  public void placeInfusion( Injured injured ) {
    injured.healSuffer( Suffer.NUTRITIONAL_DEFICIENCY );
  }

  @Override
  public void intubate( Injured injured ) {
    injured.healSuffer( Suffer.BREATH_PROBLEMS );
  }

  @Override
  public void provideFirstAid( Injured injured ) {
    provideHeartbeatMassage( injured );
    intubate( injured );
    placeInfusion( injured );
    nurseWounds( injured );
    stablilizeBrokenBones( injured );
  }

  @Override
  public void intubate( IntubationPartient patient ) {
    patient.setIntubated( true );
  }

  @Override
  public void healWithMed( Injured injured, Medicine med ) {
    Collection<Suffer> suffers = new ArrayList<>(injured.getSuffers());
    for ( Suffer suffer : suffers ) {
      injured.healSuffer( suffer );
    }
  }

}
