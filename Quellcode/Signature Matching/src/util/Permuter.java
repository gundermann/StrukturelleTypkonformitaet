package util;

import java.util.Arrays;
import java.util.Collection;

public final class Permuter {

  private Permuter() {

  }

  public static <T> void permuteRecursiveWithOriginalPositionCached(
      int n, T[] arguments, Integer[] positions, Collection<T[]> accumulator,
      Collection<Integer[]> positionAccumulator ) {
    if ( n == 1 ) {
      accumulator.add( arguments );
      positionAccumulator.add( Arrays.asList( positions ).toArray( new Integer[] {} ) );
    }
    else {
      for ( int i = 0; i < n - 1; i++ ) {
        permuteRecursiveWithOriginalPositionCached( n - 1, arguments, positions, accumulator,
            positionAccumulator );
        if ( n % 2 == 0 ) {
          swap( arguments, i, n - 1 );
          Integer tmpPos = positions[i];
          positions[i] = positions[n - 1];
          positions[n - 1] = tmpPos;
        }
        else {
          swap( arguments, 0, n - 1 );
          Integer tmpPos = positions[0];
          positions[0] = positions[n - 1];
          positions[n - 1] = tmpPos;
        }
      }
      permuteRecursiveWithOriginalPositionCached( n - 1, arguments, positions, accumulator,
          positionAccumulator );
    }
  }

  public static <T> void permuteRecursive(
      int n, T[] arguments, Collection<T[]> accumulator ) {
    if ( n == 1 ) {
      accumulator.add( arguments );
    }
    else {
      for ( int i = 0; i < n - 1; i++ ) {
        permuteRecursive( n - 1, arguments, accumulator );
        if ( n % 2 == 0 ) {
          swap( arguments, i, n - 1 );
        }
        else {
          swap( arguments, 0, n - 1 );
        }
      }
      permuteRecursive( n - 1, arguments, accumulator );
    }
  }

  private static <T> void swap( T[] input, int a, int b ) {
    T tmp = input[a];
    input[a] = input[b];
    input[b] = tmp;
  }

  public static int fractional( int argumentCount ) {
    int fractional = 1;
    for ( int i = 1; i <= argumentCount; i++ ) {
      fractional *= i;
    }
    return fractional;
  }

  public static class OriginalPositions {
    private int orignalPos;

    private int permutedPos;
  }
}
