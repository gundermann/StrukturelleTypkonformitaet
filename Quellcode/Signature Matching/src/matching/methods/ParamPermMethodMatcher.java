package matching.methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import matching.modules.ModuleMatchingInfo;
import util.Permuter;

/**
 * Dieser Matcher beachtet, dass die Argumenttypen der beiden Methoden in unterschiedlicher Reihenfolge angegeben sein
 * können.
 */
public class ParamPermMethodMatcher implements MethodMatcher {

  private final Supplier<MethodMatcher> innerMethodMatcherSupplier;

  public ParamPermMethodMatcher( Supplier<MethodMatcher> innerMethodMatcherSupplier ) {
    this.innerMethodMatcherSupplier = innerMethodMatcherSupplier;
  }

  @Override
  public boolean matches( Method m1, Method m2 ) {
    MethodStructure ms1 = MethodStructure.createFromDeclaredMethod( m1 );
    MethodStructure ms2 = MethodStructure.createFromDeclaredMethod( m2 );
    return matches( ms1, ms2 );
  }

  private boolean matches( MethodStructure ms1, MethodStructure ms2 ) {
    if ( !matchesType( ms1.getReturnType(), ms2.getReturnType() ) ) {
      return false;
    }
    if ( ms1.getSortedArgumentTypes().length != ms2.getSortedArgumentTypes().length ) {
      return false;
    }

    return matchPermutedArguments( ms1.getSortedArgumentTypes(), ms2.getSortedArgumentTypes(),
        this::matchesArgumentTypes );
  }

  private boolean matchPermutedArguments( Class<?>[] sortedArgumentTypes1, Class<?>[] sortedArgumentTypes2,
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
      if ( !matchesType( argumentTypes1[i], argumentTypes2[i] ) ) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Set<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod ) {
    if ( !matches( checkMethod, queryMethod ) ) {
      return new HashSet<>();
    }
    MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( checkMethod, queryMethod );
    Collection<ModuleMatchingInfo> returnTypeMatchingInfos = innerMethodMatcherSupplier.get()
        .calculateTypeMatchingInfos(
            queryMethod.getReturnType(), checkMethod.getReturnType() );

    Map<Integer, Collection<ModuleMatchingInfo>> argumentTypesMatchingInfos = calculateArgumentMatchingInfos(
        checkMethod.getParameterTypes(), queryMethod.getParameterTypes() );
    return factory.createFromTypeMatchingInfos( returnTypeMatchingInfos, argumentTypesMatchingInfos );
  }

  private Map<Integer, Collection<ModuleMatchingInfo>> calculateArgumentMatchingInfos( Class<?>[] checkArgs,
      Class<?>[] queryArgs ) {

    Map<Integer, Collection<ModuleMatchingInfo>> infos = new HashMap<>();
    Collection<Class<?>[]> permutations = permuteAgruments( checkArgs );
    if ( permutations.isEmpty() ) {
      return infos;
    }
    for ( Class<?>[] combination : permutations ) {
      for ( int i = 0; i < combination.length; i++ ) {
        Class<?> checkParameter = combination[i];
        Class<?> queryParameter = queryArgs[i];
        infos.put( i, calculateTypeMatchingInfos( checkParameter, queryParameter ) );
      }
    }
    return infos;
  }

  @Override
  public boolean matchesType( Class<?> checkType, Class<?> queryType ) {
    return innerMethodMatcherSupplier.get().matchesType( checkType, queryType );
  }

  @Override
  public Collection<ModuleMatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType ) {
    return innerMethodMatcherSupplier.get().calculateTypeMatchingInfos( checkType, queryType );
  }

}
