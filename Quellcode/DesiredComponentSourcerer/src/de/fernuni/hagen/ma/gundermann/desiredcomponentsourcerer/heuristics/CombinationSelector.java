package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
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

/**
 * Selektor für die Kombination von strukturell 100%ig matchenden Komponenten
 */
@Deprecated
public class CombinationSelector implements Selector {

  private final List<PartlyTypeMatchingInfo> infos;

  private int combinatiedComponentCount = 1; // initial 1, damit mit der Kombination aus 2 Komponenten begonnen wird

  private Collection<Collection<CombinationPartInfo>> cachedCalculatedInfos = new ArrayList<>();

  public CombinationSelector( List<PartlyTypeMatchingInfo> infos ) {
    this.infos = infos;
  }

  @Override
  public boolean hasNext() {
    return !cachedCalculatedInfos.isEmpty() || combinatiedComponentCount < infos.size();
  }

  @Override
  public Optional<CombinationInfo> getNext() {
    if ( cachedCalculatedInfos.isEmpty() ) {
      combinatiedComponentCount++;
      Map<Method, Collection<PartlyTypeMatchingInfo>> relevantTypeMatchingInfos = collectRelevantInfosPerMethod();
      fillCachedComponent2MatchingInfo( relevantTypeMatchingInfos );
    }
    return Optional.of( new CombinationInfo( CollectionUtil.pop( cachedCalculatedInfos ) ) );
  }

  private Map<Method, Collection<PartlyTypeMatchingInfo>> collectRelevantInfosPerMethod() {
    Map<Method, Collection<PartlyTypeMatchingInfo>> relevantTypeMatchingInfos = new HashMap<>();
    Collection<Collection<PartlyTypeMatchingInfo>> matchingInfoCombinations = Combinator.generateCombis( infos,
        combinatiedComponentCount );
    for ( Collection<PartlyTypeMatchingInfo> infoCombi : matchingInfoCombinations ) {
      Map<Method, Collection<PartlyTypeMatchingInfo>> matchingInfoPerMethod = getMatchingInfoPerMethod( infoCombi );
      relevantTypeMatchingInfos = CollectionUtil.mergeMapsWithCollectionValue( relevantTypeMatchingInfos,
          matchingInfoPerMethod );
    }
    return relevantTypeMatchingInfos;
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

  @Override
  public void addHigherPotentialType( Class<?> higherPotentialType ) {
    // TODO Auto-generated method stub

  }
}
