package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.CombinationPartInfo;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.AnalyzationUtils;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
import matching.MatchingInfo;

/**
 * H: blacklist if no implementation available
 */

public final class CheckTypeBlacklistFilter {

  private final Collection<Integer> hashCodeBlacklist;

  private final String info;

  public CheckTypeBlacklistFilter( final Collection<Integer> checkTypeHCBlacklist ) {
    this.hashCodeBlacklist = checkTypeHCBlacklist;
    this.info = "";
  }

  public CheckTypeBlacklistFilter( final Collection<Integer> checkTypeHCBlacklist, String info ) {
    this.hashCodeBlacklist = checkTypeHCBlacklist;
    this.info = " " + info;
  }

  public Collection<MatchingInfo> filter( final List<MatchingInfo> infos ) {
    AnalyzationUtils.filterCount = 0;
    Collection<MatchingInfo> filtered = infos.stream()
        .filter( ptmi -> AnalyzationUtils
            .filterWithAnalyticalCount( !this.hashCodeBlacklist.contains( ptmi.getTarget().hashCode() ) ) )
        .collect( Collectors.toList() );

    Logger.infoF( "filtered by %s%s: %d", getClass().getSimpleName(), info, AnalyzationUtils.filterCount );
    return filtered;
  }

  public Collection<Collection<CombinationPartInfo>> filterWithNestedCriteria(
      Collection<Collection<CombinationPartInfo>> infos ) {
    AnalyzationUtils.filterCount = 0;
    Collection<Collection<CombinationPartInfo>> filtered = infos.stream()
        .filter( infoCol -> AnalyzationUtils.filterWithAnalyticalCount( infoCol.stream()
            .map( CombinationPartInfo::getComponentClass )
            .map( Class::hashCode )
            .noneMatch( this.hashCodeBlacklist::contains ) ) )
        .collect( Collectors.toList() );

    Logger.infoF( "filtered by %s%s: %d", getClass().getSimpleName(), info, AnalyzationUtils.filterCount );
    return filtered;
  }

}
