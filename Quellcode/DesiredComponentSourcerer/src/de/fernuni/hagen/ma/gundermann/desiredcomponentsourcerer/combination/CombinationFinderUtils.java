package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.Transformator;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.MMICombiBlacklistFilter;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.AnalyzationUtils;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
import matching.MatchingInfo;

public abstract class CombinationFinderUtils {

  private CombinationFinderUtils() {

  }

  public static boolean isFullMatchingComponent( MatchingInfo matchingInfo ) {
    return matchingInfo.isFullMatching() ;
  }

  public static Map<Method, Collection<CombinationPartInfo>> transformToCombinationPartInfosPerMethod(
      Map<Method, Collection<MatchingInfo>> method2typeMatchingInfos,
      Collection<Collection<Integer>> methodMatchingInfoHCBlacklist ) {

    AnalyzationUtils.filterCount = 0;
    Map<Method, Collection<CombinationPartInfo>> transformed = new HashMap<>();
    for ( Entry<Method, Collection<MatchingInfo>> entry : method2typeMatchingInfos.entrySet() ) {
      Method method = entry.getKey();
      Collection<MatchingInfo> tmpInfos = entry.getValue();

      Collection<CombinationPartInfo> tmpTransformed = tmpInfos.stream()
          .map( Transformator::transformTypeInfo2CombinationPartInfos ).flatMap( Collection::stream )
          .collect( Collectors.toList() );

      Collection<CombinationPartInfo> combiPartInfos = tmpTransformed.stream()
          .filter( cpi -> Objects.equals( cpi.getSourceMethod(), method ) )
          .collect( Collectors.toList() );

      // filter blacklist items by hashcode
      combiPartInfos = new MMICombiBlacklistFilter( methodMatchingInfoHCBlacklist, false )
          .filterBlacklistedSingleMMI( combiPartInfos );
      transformed.put( method, combiPartInfos );
    }
    Logger.infoF( "filtered by %s: %d", MMICombiBlacklistFilter.class.getSimpleName(),
        AnalyzationUtils.filterCount );
    return transformed;
  }
}
