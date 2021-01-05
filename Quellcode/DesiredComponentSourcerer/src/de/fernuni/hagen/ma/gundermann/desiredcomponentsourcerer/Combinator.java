package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.CollectionUtil;

public class Combinator<K, V> {

  public static <T> Collection<Collection<T>> generateCombis( Collection<T> base, int combinatedElems ) {
    if ( combinatedElems == 1 ) {
      return base.stream().map( Collections::singletonList ).map( ArrayList::new ).collect( Collectors.toList() );
    }

    Collection<T> restBase = new ArrayList<>( base );
    Collection<Collection<T>> result = new ArrayList<>();
    for ( int i1 = 0; i1 < base.size(); i1++ ) {
      T elem = CollectionUtil.get( base, i1 ).get();
      restBase.remove( elem );
      Collection<Collection<T>> generateCombis = new ArrayList<>();
      for ( int combiIteration = combinatedElems - 1; combiIteration > 0; combiIteration-- ) {
        generateCombis.addAll( generateCombis( restBase, combiIteration ) );
      }
      for ( Collection<T> combi : generateCombis ) {
        combi.add( elem );
      }
      result.addAll( generateCombis );
    }
    return result;

  }

  public Collection<Collection<V>> generateCombis(
      Map<K, Collection<V>> possibleMethodMatches ) {
    Collection<Collection<V>> combinations = new ArrayList<>();
    // erste Methode holen
    Iterator<K> iterator = possibleMethodMatches.keySet().iterator();
    if ( !iterator.hasNext() ) {
      // keine Methode mehr übrig
      return combinations;
    }
    K selectedMethod = iterator.next();
    // possibleMethodMatches abbauen
    // Kopie der ursprünglichen Map erstellen
    Map<K, Collection<V>> localMethodMatches = new HashMap<>( possibleMethodMatches );
    Collection<V> selectedMethodMatches = localMethodMatches.remove( selectedMethod );
    if ( selectedMethodMatches.isEmpty() ) {
      return generateCombis( localMethodMatches );
    }
    for ( V info : selectedMethodMatches ) {
      Collection<Collection<V>> otherCombinations = generateCombis( localMethodMatches );
      if ( otherCombinations.isEmpty() ) {
        Collection<V> singleInfos = new ArrayList<>();
        singleInfos.add( info );
        combinations.add( singleInfos );
        continue;
      }
      for ( Collection<V> otherInfos : otherCombinations ) {
        otherInfos.add( info );
      }
      combinations.addAll( otherCombinations );
    }
    return combinations;
  }

}
