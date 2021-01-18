package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.util.Collection;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.CombinationPartInfo;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.AnalyzationUtils;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;

/**
 * H: blacklist by pivot test calls
 */

// TODO Unit-Test
public final class MethodMatchingInfoBlacklistFilter {

  private final Collection<Integer> hashCodeBlacklist;

  private final boolean handleAnalyzationUtils;

  public MethodMatchingInfoBlacklistFilter( final Collection<Integer> checkTypeHCBlacklist ) {
    this.hashCodeBlacklist = checkTypeHCBlacklist;
    this.handleAnalyzationUtils = true;
  }

  public MethodMatchingInfoBlacklistFilter( Collection<Integer> methodMatchingInfoHCBlacklist,
      boolean handleAnalyzationUtils ) {
    this.hashCodeBlacklist = methodMatchingInfoHCBlacklist;
    this.handleAnalyzationUtils = handleAnalyzationUtils;
  }

  public Collection<CombinationPartInfo> filter( final Collection<CombinationPartInfo> infos ) {
    if ( handleAnalyzationUtils )
      AnalyzationUtils.filterCount = 0;

    Collection<CombinationPartInfo> filtered = infos.stream()
        .filter( ptmi -> AnalyzationUtils
            .filterWithAnalyticalCount( !this.hashCodeBlacklist.contains( ptmi.getMatchingInfo().hashCode() ) ) )
        .collect( Collectors.toList() );
    if ( handleAnalyzationUtils )
      Logger.infoF( "filtered by %s: %d", getClass().getSimpleName(), AnalyzationUtils.filterCount );
    return filtered;
  }

  public Collection<Collection<CombinationPartInfo>> filterWithNestedCriteria(
      Collection<Collection<CombinationPartInfo>> infos ) {
    if ( handleAnalyzationUtils )
      AnalyzationUtils.filterCount = 0;

    Collection<Collection<CombinationPartInfo>> filtered = infos.stream()
        .filter( cpis -> cpis.stream()
            .noneMatch( cpi -> AnalyzationUtils.filterWithAnalyticalCount(
                this.hashCodeBlacklist.contains( cpi.getMatchingInfo().hashCode() ) ) ) )
        .collect( Collectors.toList() );

    if ( handleAnalyzationUtils )
      Logger.infoF( "filtered by %s: %d", getClass().getSimpleName(), AnalyzationUtils.filterCount );
    return filtered;
  }

}
