package matching.types.matchingfunctions;

import java.util.function.Function;

/**
 * Funktion zur Konvertierung eines speziellen Objekten in ein allgemeineres Objekt.</br>
 * Vielleicht wird diese Funktion gar nicht benötigt.
 */
public class Spec2GenType<T, S extends T> implements Function<S, T> {
  @Override
  public T apply( S source ) {

    return null;
  }

}
