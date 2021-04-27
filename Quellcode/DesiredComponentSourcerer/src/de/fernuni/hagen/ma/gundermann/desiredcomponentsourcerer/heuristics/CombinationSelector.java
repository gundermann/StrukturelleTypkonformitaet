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

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.Selector;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.CombinationFinderUtils;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.CombinationInfo;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.CombinationPartInfo;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.Combinator;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.CollectionUtil;
import matching.methods.MethodMatchingInfo;
import matching.modules.PartlyTypeMatchingInfo;

/**
 * Selektor für die Kombination von Komponenten, sodass alle erwarteten Methoden bedient werden.
 */
public class CombinationSelector implements Selector {

  private final List<PartlyTypeMatchingInfo> infos;

  private int combinatiedComponentCount = 1;

  private Collection<Collection<CombinationPartInfo>> cachedCalculatedInfos = new ArrayList<>();

  private List<Collection<PartlyTypeMatchingInfo>> cachedMatchingInfoCombinations = new ArrayList<>();

  private final Collection<Method> originalMethods;

  private final Collection<Class<?>> higherPotentialTypes = new ArrayList<>();

  // H: blacklist by pivot test calls
  private final Collection<Integer> methodMatchingInfoHCBlacklist = new ArrayList<>();

  // H: blacklist if no implementation available
  private final Collection<Integer> checkTypeHCBlacklist = new ArrayList<>();

  public CombinationSelector( List<PartlyTypeMatchingInfo> infos ) {
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

  private void collectRelevantMatchingInfoCombinations() {
    // H: blacklist if no implementation available
    Collection<PartlyTypeMatchingInfo> relevantInfos = new CheckTypeBlacklistFilter( this.checkTypeHCBlacklist )
        .filter( infos );

    cachedMatchingInfoCombinations = new ArrayList<>( Combinator.generateCombis( relevantInfos,
        combinatiedComponentCount ) );

    // H: combinate low matcher rating first
    // sort by matcher rate
    if ( HeuristicSetting.COMBINE_LOW_MATCHER_RATING_FIRST ) {
      Collections.sort( cachedMatchingInfoCombinations, new AccumulatedMatchingRateComparator() );
    }

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

    this.cachedCalculatedInfos = this.cachedCalculatedInfos.stream()
        .filter( new SelfCombinatedPartFilter() ).collect( Collectors.toList() );
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
    this.higherPotentialTypes.add( higherPotentialType );
  }

  @Override
  public void addToBlacklist( Collection<MethodMatchingInfo> methodMatchingInfo ) {
    // H: blacklist by pivot test calls
    // H: blacklist failed single methods tested
    this.methodMatchingInfoHCBlacklist
        .addAll( methodMatchingInfo.stream().map( MethodMatchingInfo::hashCode ).collect( Collectors.toList() ) );

    cachedCalculatedInfos = new MethodMatchingInfoBlacklistFilter( this.methodMatchingInfoHCBlacklist, "update" )
        .filterWithNestedCriteria( cachedCalculatedInfos );
  }

  @Override
  public void addToBlacklist( Class<?> componentInterface ) {
    // H: blacklist if no implementation available
    this.checkTypeHCBlacklist.add( componentInterface.hashCode() );

    // update cache
    cachedCalculatedInfos = new CheckTypeBlacklistFilter( this.checkTypeHCBlacklist )
        .filterWithNestedCriteria( cachedCalculatedInfos );
  }
}
