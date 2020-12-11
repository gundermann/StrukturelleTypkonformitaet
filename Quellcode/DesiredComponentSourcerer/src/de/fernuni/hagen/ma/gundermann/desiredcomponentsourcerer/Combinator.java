package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Combinator<K, V> {

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
