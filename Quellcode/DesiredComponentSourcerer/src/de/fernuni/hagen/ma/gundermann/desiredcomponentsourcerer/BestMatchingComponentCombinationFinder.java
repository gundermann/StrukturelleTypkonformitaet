package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.CollectionUtil;
import matching.methods.MethodMatchingInfo;
import matching.modules.PartlyTypeMatchingInfo;

// Auskommentierte Teile gehoeren zu Heuristiken, die ich begonnen hatte umzusetzten, aber nicht zuende gedacht hatte.
public class BestMatchingComponentCombinationFinder {

  private final Map<Class<?>, PartlyTypeMatchingInfo> componentInterface2PartlyMatchingInfos;

  private final List<PartlyTypeMatchingInfo> quantitativeSortedInfos;

  private Collection<MethodMatchingInfo> cachedCalculatedInfos = new ArrayList<>();

  // erster Versuch (primitiv)
  private int mainIndex = -1;

  // private int currentTypeMatchingInfoIndizes[] = new int[] {};
  //
  // // Dieses Array enthaelt semantisch betrachtet das Array currentTypeMatchingInfoIndizes als erste Dimension
  // private int currentMethodMatchingInfoIndizes[][] = new int[][] {};

  // 1. Dim: Komponente
  // 2. Dim: MethodMatchingInfo
  // private MethodMatchingInfo[][] calculatedMethodMatchingInfosOfCurrentComponents = new MethodMatchingInfo[][] {};

  private Optional<CombinationInfo> nextCombinationInfo = Optional.empty();

  BestMatchingComponentCombinationFinder(
      Map<Class<?>, PartlyTypeMatchingInfo> componentInterface2PartlyMatchingInfos ) {
    this.componentInterface2PartlyMatchingInfos = componentInterface2PartlyMatchingInfos;
    quantitativeSortedInfos = new ArrayList<>(
        componentInterface2PartlyMatchingInfos.values() );
    Collections.sort( quantitativeSortedInfos, new QuantitaiveMatchRankingComparator() );
    new CombinationInfo( new HashMap<>() );
  }

  public boolean hasNextCombination() {
    if (
    // quantitativeSortedInfos.size() == 0
    // || currentTypeMatchingInfoIndizes.length > 0
    // && currentTypeMatchingInfoIndizes[0] >= quantitativeSortedInfos.size()
    mainIndex + 1 >= quantitativeSortedInfos.size() ) {
      return false;
    }
    generateNextCombination();
    return nextCombinationInfo.isPresent();
  }

  public CombinationInfo getNextCombination() {
    return nextCombinationInfo.get();
  }

  private void generateNextCombination() {
    // Neue Methoden-Kombination in aktuellen Komponenten suchen.

    // Neue Komponente suchen
    // int lastMainIndex = releaseLastMainIndex();
    mainIndex++;
    PartlyTypeMatchingInfo mainComponent = quantitativeSortedInfos.get( mainIndex );
    // PartlyTypeMatchingInfo lastUsedComponent = quantitativeSortedInfos.get( lastMainIndex );
    // Collection<Method> uncoveragedMethods = getUncoveragedMethods();
    // findNextComponentForMethods( lastUsedComponent );
    findNextComponentForMethods( mainComponent );
  }

  private void findNextComponentForMethods( PartlyTypeMatchingInfo lastUsedComponent ) {
    Collection<Method> methodsWithoutMatchingInfo = lastUsedComponent.getOriginalMethods();
    Map<Method, Collection<PartlyTypeMatchingInfo>> relevantInfos = collectRelevantMatchingInfos(
        methodsWithoutMatchingInfo );
    Map<Class<?>, Collection<MethodMatchingInfo>> component2MatchingInfo = new HashMap<>();
    for ( Entry<Method, Collection<PartlyTypeMatchingInfo>> e : relevantInfos.entrySet() ) {
      Optional<PartlyTypeMatchingInfo> optInfo = CollectionUtil.get( e.getValue(), 0 );
      if ( optInfo.isPresent() ) {
        PartlyTypeMatchingInfo info = optInfo.get();
        Collection<MethodMatchingInfo> methodMatchingInfos = info.getMethodMatchingInfoSupplier().get( e.getKey() )
            .get();
        Optional<MethodMatchingInfo> optMethodMatchingInfo = CollectionUtil.get( methodMatchingInfos, 0 );
        optMethodMatchingInfo.ifPresent( i -> component2MatchingInfo.compute( info.getCheckType(),
            CollectionUtil.remapping_addToValueCollection( i ) ) );
      }
    }

    this.nextCombinationInfo = Optional.of( new CombinationInfo( component2MatchingInfo ) );

  }

  private Map<Method, Collection<PartlyTypeMatchingInfo>> collectRelevantMatchingInfos(
      Collection<Method> originalMethods ) {
    Collection<Method> originalMethodsTmp = new ArrayList<>( originalMethods );
    Map<Method, Collection<PartlyTypeMatchingInfo>> relevantInfos = new HashMap<>();
    int infoIndex = mainIndex;
    while ( !originalMethodsTmp.isEmpty() && infoIndex < quantitativeSortedInfos.size() ) {
      PartlyTypeMatchingInfo info = quantitativeSortedInfos.get( infoIndex );
      Collection<Method> methodsWithMatchingInfo = info.getMethodMatchingInfoSupplier().keySet();
      originalMethodsTmp = originalMethods.stream()
          .filter( m -> !methodsWithMatchingInfo.contains( m ) ).collect( Collectors.toList() );
      for ( Method m : methodsWithMatchingInfo ) {
        Collection<PartlyTypeMatchingInfo> infos = new ArrayList<>();
        if ( relevantInfos.containsKey( m ) ) {
          infos = relevantInfos.get( m );
        }
        infos.add( info );
        relevantInfos.put( m, infos );
      }
      infoIndex++;
    }
    return relevantInfos;
  }

  // private Collection<Method> getUncoveragedMethods() {
  // Collection<Method> coveragedMethods = new ArrayList<>();
  // for ( int c_index = 0; c_index < currentMethodMatchingInfoIndizes.length; c_index++ ) {
  // for ( int m_index = 0; m_index < currentMethodMatchingInfoIndizes[c_index].length; m_index++ ) {
  // int mmiIndex = currentMethodMatchingInfoIndizes[c_index][m_index];
  // MethodMatchingInfo methodMatchingInfo = calculatedMethodMatchingInfosOfCurrentComponents[c_index][mmiIndex];
  // coveragedMethods.add( methodMatchingInfo.getSource() );
  // }
  // }
  //
  // Collection<Method> uncoveragedMethods = new ArrayList<>();
  // for ( Method oriMethod : getOriginalMethods() ) {
  // if ( !coveragedMethods.contains( oriMethod ) ) {
  // uncoveragedMethods.add( oriMethod );
  // }
  // }
  // return uncoveragedMethods;
  // }
  //
  // private Collection<Method> getOriginalMethods() {
  // return quantitativeSortedInfos.get( 0 ).getOriginalMethods();
  // }
  //
  // private boolean isCurrentComponentClass( Class<?> lastComponentClass ) {
  // for ( int i = 0; i < currentTypeMatchingInfoIndizes.length; i++ ) {
  // int index = currentTypeMatchingInfoIndizes[i];
  // if ( quantitativeSortedInfos.get( index ).getCheckType().equals( lastComponentClass ) ) {
  // return true;
  // }
  // }
  // return false;
  // }
  //
  // /**
  // * Den Index der zuletzt verwendeten Komponente entfernen und zurueckgeben.
  // */
  // private int releaseLastMainIndex() {
  // if ( currentTypeMatchingInfoIndizes.length == 0 ) {
  // return -1;
  // }
  // int[] tempIndizes = new int[] {};
  // int releasedIndex = currentTypeMatchingInfoIndizes[currentTypeMatchingInfoIndizes.length - 1];
  // for ( int i = 0; i < currentTypeMatchingInfoIndizes.length - 1; i++ ) {
  // tempIndizes[i] = currentTypeMatchingInfoIndizes[i];
  // }
  // currentTypeMatchingInfoIndizes = tempIndizes;
  //
  // int[][] tempMethodIndizes = new int[][] {};
  // for ( int i = 0; i < currentMethodMatchingInfoIndizes.length - 1; i++ ) {
  // tempMethodIndizes[i] = currentMethodMatchingInfoIndizes[i];
  // }
  // currentMethodMatchingInfoIndizes = tempMethodIndizes;
  //
  // return releasedIndex;
  // }

  private static class QuantitaiveMatchRankingComparator implements Comparator<PartlyTypeMatchingInfo> {

    // Der mit dem hoechsten Ranking soll vorne stehen
    @Override
    public int compare( PartlyTypeMatchingInfo o1, PartlyTypeMatchingInfo o2 ) {
      Double ranking1 = Optional.ofNullable( o1 ).map( PartlyTypeMatchingInfo::getQuantitaiveMatchRating ).orElse( 0d );
      Double ranking2 = Optional.ofNullable( o2 ).map( PartlyTypeMatchingInfo::getQuantitaiveMatchRating ).orElse( 0d );
      return Double.compare( ranking2, ranking1 );
    }

  }

}
