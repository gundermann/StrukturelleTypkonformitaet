package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.AnalyzationUtils;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
import matching.modules.PartlyTypeMatchingInfo;

public abstract class CombinationFinderUtils {

  private static final double FULL_MATCH = 1.0d;

  private CombinationFinderUtils() {

  }

  public static boolean isFullMatchingComponent( PartlyTypeMatchingInfo matchingInfo ) {
    return matchingInfo.getQuantitaiveMatchRating() == FULL_MATCH;
  }

  public static Map<Method, Collection<CombinationPartInfo>> transformToCombinationPartInfosPerMethod(
      Map<Method, Collection<PartlyTypeMatchingInfo>> method2typeMatchingInfos,
      Collection<Integer> methodMatchingInfoHCBlacklist ) {
    // Logger.infoF( "BLACKLIST: %s",
    // methodMatchingInfoHCBlacklist.stream().map( String::valueOf ).collect( Collectors.joining( "," ) ) );

    AnalyzationUtils.filterCount = 0;
    Map<Method, Collection<CombinationPartInfo>> transformed = new HashMap<>();
    for ( Entry<Method, Collection<PartlyTypeMatchingInfo>> entry : method2typeMatchingInfos.entrySet() ) {
      Method method = entry.getKey();
      Collection<PartlyTypeMatchingInfo> tmpInfos = entry.getValue();

      Collection<CombinationPartInfo> tmpTransformed = tmpInfos.stream()
          .map( Transformator::transformTypeInfo2CombinationPartInfos ).flatMap( Collection::stream )
          .collect( Collectors.toList() );
      // Logger.infoF( "CONTAINED HC: %s",
      // tmpTransformed.stream().map( cpi -> cpi.getMatchingInfo().hashCode() )
      // .map( String::valueOf ).collect( Collectors.joining( "," ) ) );

      List<CombinationPartInfo> combiPartInfos = tmpTransformed.stream()
          .filter( cpi -> Objects.equals( cpi.getSourceMethod(), method ) )
          // filter blacklist items by hashcode
          .filter( cpi -> AnalyzationUtils.filterWithAnalyticalCount(
              !methodMatchingInfoHCBlacklist.contains( cpi.getMatchingInfo().hashCode() ) ) )
          .collect( Collectors.toList() );
      transformed.put( method, combiPartInfos );
    }
    Logger.infoF( "filtered by method matching info blacklist: %d", AnalyzationUtils.filterCount );
    return transformed;
  }
}
