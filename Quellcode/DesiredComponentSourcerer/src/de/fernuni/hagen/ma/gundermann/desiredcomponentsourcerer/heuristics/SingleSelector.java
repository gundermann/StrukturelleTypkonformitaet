package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.CollectionUtil;
import matching.methods.MethodMatchingInfo;
import matching.modules.PartlyTypeMatchingInfo;

/**
 * Selektor f�r strukturell 100%ig matchende Komponenten
 */
public class SingleSelector implements Selector {

  private List<Collection<PartlyTypeMatchingInfo>> infos;

  private int selectedIndex = -1;

  private Collection<Collection<CombinationPartInfo>> cachedCalculatedInfos = new ArrayList<>();

  // H: blacklist by pivot test calls
  private final Collection<Integer> methodMatchingInfoHCBlacklist = new ArrayList<>();

  public SingleSelector( List<PartlyTypeMatchingInfo> infos ) {
    this.infos = infos.stream()
        .filter( CombinationFinderUtils::isFullMatchingComponent )
        .map( i -> Collections.singletonList( i ) )
        .sorted( new AccumulatedMatchingRateComparator() )
        .collect( Collectors.toList() );
  }

  @Override
  public boolean hasNext() {
    return infos.size() > selectedIndex + 1 || !cachedCalculatedInfos.isEmpty();
  }

  @Override
  public Optional<CombinationInfo> getNext() {
    if ( cachedCalculatedInfos.isEmpty() ) {
      selectedIndex++;
      PartlyTypeMatchingInfo info = infos.get( selectedIndex ).iterator().next();
      Map<Method, Collection<PartlyTypeMatchingInfo>> relevantTypeMatchingInfos = getMatchingInfoPerMethod( info );
      fillCachedComponent2MatchingInfo( relevantTypeMatchingInfos );
    }
    return Optional.of( new CombinationInfo( CollectionUtil.pop( cachedCalculatedInfos ) ) );
  }

  private void fillCachedComponent2MatchingInfo( Map<Method, Collection<PartlyTypeMatchingInfo>> typeMatchingInfos ) {
    Map<Method, Collection<CombinationPartInfo>> combiPartInfos = CombinationFinderUtils
        .transformToCombinationPartInfosPerMethod(
            typeMatchingInfos, this.methodMatchingInfoHCBlacklist );
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

  @Override
  public void addHigherPotentialType( Class<?> higherPotentialType ) {
    // irrelevant fuer diesen Selector

  }

  @Override
  public void addToBlacklist( MethodMatchingInfo methodMatchingInfo ) {
    // H: blacklist by pivot test calls
    this.methodMatchingInfoHCBlacklist.add( methodMatchingInfo.hashCode() );

    cachedCalculatedInfos = new MethodMatchingInfoBlacklistFilter( this.methodMatchingInfoHCBlacklist, "update" )
        .filterWithNestedCriteria( cachedCalculatedInfos );
  }

  @Override
  public void addToBlacklist( Class<?> componentInterface ) {
    // H: blacklist if no implementation available
    // update cache
    cachedCalculatedInfos = new CheckTypeBlacklistFilter( Arrays.asList( componentInterface.hashCode() ), "update" )
        .filterWithNestedCriteria( cachedCalculatedInfos );
  }

}
