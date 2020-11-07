package matching.methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.modules.ModuleMatchingInfo;
import util.Permuter;

/**
 * Dieser Matcher beachtet, dass die Argumenttypen der beiden Methoden in unterschiedlicher Reihenfolge angegeben sein
 * k�nnen.
 */
public class ParamPermMethodMatcher implements MethodMatcher {

  private final Supplier<MethodMatcher> innerMethodMatcherSupplier;

  public ParamPermMethodMatcher( Supplier<MethodMatcher> innerMethodMatcherSupplier ) {
    this.innerMethodMatcherSupplier = innerMethodMatcherSupplier;
  }

  @Override
  public boolean matches( Method m1, Method m2 ) {
    if ( innerMethodMatcherSupplier.get().matches( m1, m2 ) ) {
      return true;
    }
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
    Collection<Class<?>[]> permutations = permuteAgruments( sortedArgumentTypes1 ).values();
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

  private Map<Integer[], Class<?>[]> permuteAgruments( Class<?>[] originalArgumentTypes ) {
    int argumentCount = originalArgumentTypes.length;
    int permutationCount = Permuter.fractional( argumentCount );
    List<Class<?>[]> permutations = new ArrayList<>( permutationCount );
    List<Integer[]> positions = new ArrayList<>( permutationCount );

    Integer[] orinialPos = new Integer[argumentCount];
    for ( int i = 0; i < argumentCount; i++ ) {
      orinialPos[i] = i;
    }

    if ( argumentCount > 0 ) {
      Permuter.permuteRecursiveWithOriginalPositionCached( argumentCount, originalArgumentTypes,
          orinialPos,
          permutations,
          positions );
    }
    Map<Integer[], Class<?>[]> map = new HashMap<>();
    for ( int i = 0; i < positions.size(); i++ ) {
      map.put( positions.get( i ), permutations.get( i ) );
    }
    return map;
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

    Collection<Map<ParamPosition, Collection<ModuleMatchingInfo>>> argumentTypesMatchingInfos = calculateArgumentMatchingInfos(
        checkMethod.getParameterTypes(), queryMethod.getParameterTypes() );
    return factory.createFromTypeMatchingInfos( returnTypeMatchingInfos, argumentTypesMatchingInfos );
  }

  private Collection<Map<ParamPosition, Collection<ModuleMatchingInfo>>> calculateArgumentMatchingInfos(
      Class<?>[] checkArgs,
      Class<?>[] queryArgs ) {

    Collection<Map<ParamPosition, Collection<ModuleMatchingInfo>>> infos = new ArrayList<>();
    Map<Integer[], Class<?>[]> permutations = permuteAgruments( checkArgs );
    if ( permutations.isEmpty() ) {
      return infos;
    }
    for ( Entry<Integer[], Class<?>[]> combination : permutations.entrySet() ) {
      Map<ParamPosition, Collection<ModuleMatchingInfo>> infoMap = new HashMap<>();
      for ( int i = 0; i < combination.getValue().length; i++ ) {
        Class<?> checkParameter = combination.getValue()[i];
        Class<?> queryParameter = queryArgs[i];
        infoMap.put( new ParamPosition( i, combination.getKey()[i] ),
            calculateTypeMatchingInfos( checkParameter, queryParameter ) );
      }
      infos.add( infoMap );
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
