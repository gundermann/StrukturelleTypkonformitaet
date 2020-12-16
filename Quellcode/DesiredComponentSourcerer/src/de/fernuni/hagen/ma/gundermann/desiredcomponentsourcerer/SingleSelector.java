package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.CollectionUtil;
import matching.modules.PartlyTypeMatchingInfo;

public class SingleSelector implements Selector {

  private final List<PartlyTypeMatchingInfo> infos;

  private int selectedIndex = -1;

  private Collection<Collection<CombinationPartInfo>> cachedCalculatedInfos = new ArrayList<>();

  public SingleSelector( List<PartlyTypeMatchingInfo> infos ) {
    this.infos = infos;
  }

  @Override
  public boolean hasNext() {
    return infos.size() > selectedIndex + 1 || !cachedCalculatedInfos.isEmpty();
  }

  @Override
  public Optional<CombinationInfo> getNext() {
    if ( cachedCalculatedInfos.isEmpty() ) {
      selectedIndex++;
      PartlyTypeMatchingInfo info = infos.get( selectedIndex );
      Map<Method, Collection<PartlyTypeMatchingInfo>> relevantTypeMatchingInfos = getMatchingInfoPerMethod( info );
      fillCachedComponent2MatchingInfo( relevantTypeMatchingInfos );
    }
    return Optional.of( new CombinationInfo( CollectionUtil.pop( cachedCalculatedInfos ) ) );
  }

  private void fillCachedComponent2MatchingInfo( Map<Method, Collection<PartlyTypeMatchingInfo>> typeMatchingInfos ) {
    Map<Method, Collection<CombinationPartInfo>> combiPartInfos = transformToCombinationPartInfosPerMethod(
        typeMatchingInfos );
    this.cachedCalculatedInfos = new Combinator<Method, CombinationPartInfo>().generateCombis( combiPartInfos );
  }

  private Map<Method, Collection<PartlyTypeMatchingInfo>> getMatchingInfoPerMethod( PartlyTypeMatchingInfo info ) {
    Map<Method, Collection<PartlyTypeMatchingInfo>> relevantTypeMatchingInfos = new HashMap<>();
    Collection<Method> methodsWithMatchingInfo = info.getMethodMatchingInfoSupplier().keySet();
    for ( Method m : methodsWithMatchingInfo ) {
      relevantTypeMatchingInfos.put( m, Collections.singletonList( info ) );
    }
    return relevantTypeMatchingInfos;
  }

  private Map<Method, Collection<CombinationPartInfo>> transformToCombinationPartInfosPerMethod(
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
