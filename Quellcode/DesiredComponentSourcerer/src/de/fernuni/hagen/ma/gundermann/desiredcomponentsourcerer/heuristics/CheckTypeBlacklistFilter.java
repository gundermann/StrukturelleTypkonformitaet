package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.CombinationPartInfo;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.AnalyzationUtils;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
import matching.modules.PartlyTypeMatchingInfo;

/**
 * H: blacklist if no implementation available
 */

// TODO Unit-Test
final class CheckTypeBlacklistFilter {

  private final Collection<Integer> hashCodeBlacklist;

  CheckTypeBlacklistFilter( final Collection<Integer> checkTypeHCBlacklist ) {
    this.hashCodeBlacklist = checkTypeHCBlacklist;
  }

  Collection<PartlyTypeMatchingInfo> filter( final List<PartlyTypeMatchingInfo> infos ) {
    AnalyzationUtils.filterCount = 0;
    Collection<PartlyTypeMatchingInfo> filtered = infos.stream()
        .filter( ptmi -> AnalyzationUtils
            .filterWithAnalyticalCount( !this.hashCodeBlacklist.contains( ptmi.getCheckType().hashCode() ) ) )
        .collect( Collectors.toList() );

    Logger.infoF( "filtered by %s: %d", getClass().getSimpleName(), AnalyzationUtils.filterCount );
    return filtered;
  }

  Collection<Collection<CombinationPartInfo>> filterWithNestedCriteria(
      Collection<Collection<CombinationPartInfo>> infos ) {
    AnalyzationUtils.filterCount = 0;
    Collection<Collection<CombinationPartInfo>> filtered = infos.stream()
        .filter( infoCol -> AnalyzationUtils.filterWithAnalyticalCount( infoCol.stream()
            .map( CombinationPartInfo::getComponentClass )
            .map( Class::hashCode )
            .noneMatch( this.hashCodeBlacklist::contains ) ) )
        .collect( Collectors.toList() );

    Logger.infoF( "filtered by %s: %d", getClass().getSimpleName(), AnalyzationUtils.filterCount );
    return filtered;
  }

}
