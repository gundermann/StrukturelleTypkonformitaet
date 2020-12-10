package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
    for ( int i = 1; i < c.size(); i++ ) {
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
}
