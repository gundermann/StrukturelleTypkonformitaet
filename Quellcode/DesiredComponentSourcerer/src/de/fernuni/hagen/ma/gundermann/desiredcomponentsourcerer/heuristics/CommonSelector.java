package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.CombinationFinderUtils;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.CombinationInfo;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.CombinationPartInfo;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.Combinator;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.Selector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.CollectionUtil;
import matching.modules.PartlyTypeMatchingInfo;

public class CommonSelector implements Selector {

  private final List<PartlyTypeMatchingInfo> infos;

  private int combinatiedComponentCount = 0;

  private Collection<Collection<CombinationPartInfo>> cachedCalculatedInfos = new ArrayList<>();

  private Collection<Collection<PartlyTypeMatchingInfo>> cachedMatchingInfoCombinations = new ArrayList<>();

  private final Collection<Method> originalMethods;

  public CommonSelector( List<PartlyTypeMatchingInfo> infos ) {
    this.infos = infos;
    this.originalMethods = infos.stream().findFirst().map( PartlyTypeMatchingInfo::getOriginalMethods )
        .orElse( Collections.emptyList() );
  }

  @Override
  public boolean hasNext() {
    return !cachedMatchingInfoCombinations.isEmpty() || !cachedCalculatedInfos.isEmpty()
        || combinatiedComponentCount < infos.size();
  }

  @Override
  public Optional<CombinationInfo> getNext() {
    while ( cachedCalculatedInfos.isEmpty() && hasNext() ) {
      if ( cachedMatchingInfoCombinations.isEmpty() ) {
        combinatiedComponentCount++;
        collectRelevantMatchingInfoCombinations();
      }

      Map<Method, Collection<PartlyTypeMatchingInfo>> relevantTypeMatchingInfos = collectRelevantInfosPerMethod();
      if ( !relevantTypeMatchingInfos.isEmpty() ) {
        fillCachedComponent2MatchingInfo( relevantTypeMatchingInfos );
      }
    }
    return Optional.of( new CombinationInfo( CollectionUtil.pop( cachedCalculatedInfos ) ) );
  }

  private void collectRelevantMatchingInfoCombinations() {
    cachedMatchingInfoCombinations = Combinator.generateCombis( infos,
        combinatiedComponentCount );
  }

  private Map<Method, Collection<PartlyTypeMatchingInfo>> collectRelevantInfosPerMethod() {
    Collection<PartlyTypeMatchingInfo> combi = CollectionUtil.pop( cachedMatchingInfoCombinations );
    Map<Method, Collection<PartlyTypeMatchingInfo>> matchingInfoPerMethod = getMatchingInfoPerMethod( combi );
    // Pruefen, ob auch alle erwarteten Methoden erfuellt wurden.
    if ( !matchingInfoPerMethod.keySet().containsAll( originalMethods ) ) {
      return new HashMap<>();
    }
    return matchingInfoPerMethod;
  }

  private void fillCachedComponent2MatchingInfo( Map<Method, Collection<PartlyTypeMatchingInfo>> typeMatchingInfos ) {
    Map<Method, Collection<CombinationPartInfo>> combiPartInfos = CombinationFinderUtils
        .transformToCombinationPartInfosPerMethod(
            typeMatchingInfos );
    this.cachedCalculatedInfos = new Combinator<Method, CombinationPartInfo>().generateCombis( combiPartInfos );
  }

  private Map<Method, Collection<PartlyTypeMatchingInfo>> getMatchingInfoPerMethod(
      Collection<PartlyTypeMatchingInfo> relevantInfos ) {
    Map<Method, Collection<PartlyTypeMatchingInfo>> relevantTypeMatchingInfos = new HashMap<>();
    for ( PartlyTypeMatchingInfo info : relevantInfos ) {
      Collection<Method> methodsWithMatchingInfo = info.getMethodMatchingInfoSupplier().keySet();
      for ( Method m : methodsWithMatchingInfo ) {
        relevantTypeMatchingInfos.compute( m, CollectionUtil.remapping_addToValueCollection( info ) );
      }
    }
    return relevantTypeMatchingInfos;
  }
}
