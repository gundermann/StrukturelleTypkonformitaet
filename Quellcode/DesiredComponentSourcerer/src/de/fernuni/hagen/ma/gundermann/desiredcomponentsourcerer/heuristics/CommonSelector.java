package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.CombinationFinderUtils;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.CombinationInfo;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.CombinationPartInfo;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.Combinator;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.Selector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.AnalyzationUtils;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.CollectionUtil;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
import matching.methods.MethodMatchingInfo;
import matching.modules.PartlyTypeMatchingInfo;

/**
 * Selektor für die Kombination von Komponenten, sodass alle erwarteten Methoden bedient werden.
 */
public class CommonSelector implements Selector {

  private final List<PartlyTypeMatchingInfo> infos;

  private int combinatiedComponentCount = 0;

  private Collection<Collection<CombinationPartInfo>> cachedCalculatedInfos = new ArrayList<>();

  private List<Collection<PartlyTypeMatchingInfo>> cachedMatchingInfoCombinations = new ArrayList<>();

  private final Collection<Method> originalMethods;

  private final Collection<Class<?>> higherPotentialTypes = new ArrayList<>();

  // H: blacklist by pivot test calls
  private final Collection<Integer> methodMatchingInfoHCBlacklist = new ArrayList<>();

  // H: blacklist if no implementation available
  private final Collection<Integer> checkTypeHCBlacklist = new ArrayList<>();

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
        return getNext();
      }

      Map<Method, Collection<PartlyTypeMatchingInfo>> relevantTypeMatchingInfos = collectRelevantInfosPerMethod();
      if ( !relevantTypeMatchingInfos.isEmpty() ) {
        fillCachedComponent2MatchingInfo( relevantTypeMatchingInfos );
        return getNext();
      }
    }
    return Optional.of( new CombinationInfo( CollectionUtil.pop( cachedCalculatedInfos ) ) );
  }

  @Override
  public void addHigherPotentialType( Class<?> higherPotentialType ) {
    this.higherPotentialTypes.add( higherPotentialType );
  }

  private void collectRelevantMatchingInfoCombinations() {
    // H: blacklist if no implementation available
    AnalyzationUtils.filterCount = 0;
    Collection<PartlyTypeMatchingInfo> relevantInfos = infos.stream()
        .filter( ptmi -> AnalyzationUtils
            .filterWithAnalyticalCount( !this.checkTypeHCBlacklist.contains( ptmi.getCheckType().hashCode() ) ) )
        .collect( Collectors.toList() );

    Logger.infoF( "filtered by component blacklist: %d", AnalyzationUtils.filterCount );

    // sonderlocke fuer combinatiedComponentCount == 1
    // TODO auslagern in SingleSelector
    if ( combinatiedComponentCount == 1 ) {
      relevantInfos = relevantInfos.stream()
          .filter( CombinationFinderUtils::isFullMatchingComponent )
          .collect( Collectors.toList() );
    }

    cachedMatchingInfoCombinations = new ArrayList<>( Combinator.generateCombis( relevantInfos,
        combinatiedComponentCount ) );

    // H: combinate low matcher rating first
    // sort by matcher rate
    Collections.sort( cachedMatchingInfoCombinations, new AccumulatedMatchingRateComparator() );

    // H: combinate passed tests components first
    // re-organize cache with respect to search optimization
    Collections.sort( cachedMatchingInfoCombinations, new HigherPotentialTypesFirstComparator(
        higherPotentialTypes ) );
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
            typeMatchingInfos, this.methodMatchingInfoHCBlacklist );
    this.cachedCalculatedInfos = new Combinator<Method, CombinationPartInfo>().generateCombis( combiPartInfos );

    if ( combinatiedComponentCount > 1 ) {
      this.cachedCalculatedInfos = this.cachedCalculatedInfos.stream()
          .filter( new SeldCombinatedPartFilter() ).collect( Collectors.toList() );
    }
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
  public void addToBlacklist( MethodMatchingInfo methodMatchingInfo ) {
    // H: blacklist by pivot test calls
    this.methodMatchingInfoHCBlacklist.add( methodMatchingInfo.hashCode() );

    AnalyzationUtils.filterCount = 0;
    cachedCalculatedInfos = cachedCalculatedInfos.stream()
        .filter( cpis -> cpis.stream()
            .noneMatch( cpi -> AnalyzationUtils.filterWithAnalyticalCount(
                methodMatchingInfoHCBlacklist.contains( cpi.getMatchingInfo().hashCode() ) ) ) )
        .collect( Collectors.toList() );
    Logger.infoF( "filtered by method matching info blacklist: %d", AnalyzationUtils.filterCount );
  }

  @Override
  public void addToBlacklist( Class<?> componentInterface ) {
    // H: blacklist if no implementation available
    this.checkTypeHCBlacklist.add( componentInterface.hashCode() );

    // update cache
    AnalyzationUtils.filterCount = 0;
    cachedCalculatedInfos = cachedCalculatedInfos.stream()
        .filter( infoCol -> AnalyzationUtils.filterWithAnalyticalCount( infoCol.stream()
            .map( CombinationPartInfo::getComponentClass )
            .map( Class::hashCode )
            .noneMatch( this.checkTypeHCBlacklist::contains ) ) )
        .collect( Collectors.toList() );

    Logger.infoF( "filtered by component blacklist: %d", AnalyzationUtils.filterCount );
  }
}
