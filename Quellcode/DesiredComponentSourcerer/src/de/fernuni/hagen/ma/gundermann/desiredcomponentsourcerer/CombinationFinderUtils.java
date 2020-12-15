package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import matching.modules.PartlyTypeMatchingInfo;

public abstract class CombinationFinderUtils {

  private static final double FULL_MATCH = 1.0d;

  private CombinationFinderUtils() {

  }

  public static boolean isFullMatchingComponent( PartlyTypeMatchingInfo matchingInfo ) {
    return matchingInfo.getQuantitaiveMatchRating() == FULL_MATCH;
  }

  public static Map<Method, Collection<CombinationPartInfo>> transformToCombinationPartInfosPerMethod(
      Map<Method, Collection<PartlyTypeMatchingInfo>> method2typeMatchingInfos ) {
    Map<Method, Collection<CombinationPartInfo>> transformed = new HashMap<>();
    for ( Entry<Method, Collection<PartlyTypeMatchingInfo>> entry : method2typeMatchingInfos.entrySet() ) {
      Method method = entry.getKey();
      Collection<PartlyTypeMatchingInfo> tmpInfos = entry.getValue();
      List<CombinationPartInfo> combiPartInfos = tmpInfos.stream()
          .map( Transformator::transformTypeInfo2CombinationPartInfos )
          .flatMap( Collection::stream )
          .filter( cpi -> Objects.equals( cpi.getSourceMethod(), method ) )
          .collect( Collectors.toList() );
      transformed.put( method, combiPartInfos );
    }
    return transformed;
  }
}
