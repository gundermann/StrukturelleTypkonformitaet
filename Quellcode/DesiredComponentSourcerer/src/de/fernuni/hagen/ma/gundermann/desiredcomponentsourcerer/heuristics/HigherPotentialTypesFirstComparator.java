package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.util.Collection;
import java.util.Comparator;

import matching.modules.PartlyTypeMatchingInfo;

public class HigherPotentialTypesFirstComparator implements Comparator<Collection<PartlyTypeMatchingInfo>> {

  private final Collection<Class<?>> higherPotentialTypes;

  public HigherPotentialTypesFirstComparator( Collection<Class<?>> specificElements ) {
    this.higherPotentialTypes = specificElements;
  }

  @Override
  public int compare( Collection<PartlyTypeMatchingInfo> col1, Collection<PartlyTypeMatchingInfo> col2 ) {
    if ( col1 == null ) {
      return col2 == null ? 0 : 1;
    }
    if ( col2 == null ) {
      return -1;
    }
    boolean col1Contains = col1.stream().map( PartlyTypeMatchingInfo::getCheckType )
        .anyMatch( this.higherPotentialTypes::contains );
    boolean col2Contains = col2.stream().map( PartlyTypeMatchingInfo::getCheckType )
        .anyMatch( this.higherPotentialTypes::contains );

    if ( col1Contains && !col2Contains ) {
      return -1;
    }
    if ( !col1Contains && col2Contains ) {
      return 1;
    }

    return 0;
  }

}
