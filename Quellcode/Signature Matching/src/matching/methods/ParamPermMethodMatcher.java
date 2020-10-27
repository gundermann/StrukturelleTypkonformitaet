package matching.methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiFunction;

import util.Permuter;

/**
 * Dieser Matcher beachtet, dass die Argumenttypen der beiden Methoden in unterschiedlicher Reihenfolge angegeben sein
 * können.
 */
public class ParamPermMethodMatcher implements MethodMatcher {

  @Override
  public boolean matches( Method m1, Method m2 ) {
    MethodStructure ms1 = MethodStructure.createFromDeclaredMethod( m1 );
    MethodStructure ms2 = MethodStructure.createFromDeclaredMethod( m2 );
    return matches( ms1, ms2 );
  }

  private boolean matches( MethodStructure ms1, MethodStructure ms2 ) {
    if ( ms1.getReturnType() != ms2.getReturnType() ) {
      return false;
    }
    if ( ms1.getSortedArgumentTypes().length != ms2.getSortedArgumentTypes().length ) {
      return false;
    }

    return matchPermutedArguments( ms1.getSortedArgumentTypes(), ms2.getSortedArgumentTypes(),
        this::matchesArgumentTypes );
  }

  boolean matchPermutedArguments( Class<?>[] sortedArgumentTypes1, Class<?>[] sortedArgumentTypes2,
      BiFunction<Class<?>[], Class<?>[], Boolean> matchingFunction ) {
    Collection<Class<?>[]> permutations = permuteAgruments( sortedArgumentTypes1 );
    if ( permutations.isEmpty() ) {
      return true;
    }
    for ( Class<?>[] combination : permutations ) {
      if ( matchingFunction.apply( combination, sortedArgumentTypes2 ) ) {
        return true;
      }
    }
    return false;
  }

  private Collection<Class<?>[]> permuteAgruments( Class<?>[] originalArgumentTypes ) {
    int argumentCount = originalArgumentTypes.length;
    int permutationCount = Permuter.fractional( argumentCount );
    Collection<Class<?>[]> permutations = new ArrayList<>( permutationCount );
    if ( argumentCount > 0 ) {
      Permuter.permuteRecursive( argumentCount, originalArgumentTypes, permutations );
    }
    return permutations;
  }

  private boolean matchesArgumentTypes( Class<?>[] argumentTypes1, Class<?>[] argumentTypes2 ) {
    for ( int i = 0; i < argumentTypes1.length; i++ ) {
      if ( argumentTypes1[i] != argumentTypes2[i] ) {
        return false;
      }
    }
    return true;
  }

}
