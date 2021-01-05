package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;

public final class CollectionUtil {
  private CollectionUtil() {

  }

  public static <T> Optional<T> get( Collection<T> c, int index ) {
    if ( index < 0 || c == null || c.size() <= index ) {
      return Optional.empty();
    }
    Iterator<T> iterator = c.iterator();
    for ( int i = 0; i < index; i++ ) {
      iterator.next();
    }
    return Optional.of( iterator.next() );

  }

  public static <K, V> BiFunction<K, Collection<V>, Collection<V>> remapping_addToValueCollection(
      V newElem ) {
    return ( key, value ) -> {
      Collection<V> newValue = new ArrayList<>();
      if ( value != null ) {
        newValue.addAll( value );
      }
      newValue.add( newElem );
      return newValue;
    };
  }

  public static <T> T pop(
      Collection<T> col ) {
    T popped = col.iterator().next();
    col.remove( popped );
    return popped;
  }

  public static <K, V> Map<K, Collection<V>> mergeMapsWithCollectionValue(
      Map<K, Collection<V>> map1,
      Map<K, Collection<V>> map2 ) {
    Map<K, Collection<V>> result = new HashMap<>( map1 );
    for ( Entry<K, Collection<V>> entry2 : map2.entrySet() ) {
      Collection<V> value = new ArrayList<>();
      K key2 = entry2.getKey();
      if ( result.containsKey( key2 ) ) {
        value = result.get( key2 );
      }
      value.addAll( entry2.getValue() );
      result.put( key2, value );
    }
    return result;
  }

}
