package matching.methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import matching.MatcherRate;
import matching.Setting;
import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.modules.ModuleMatchingInfo;
import matching.modules.TypeMatcher;
import util.Permuter;

/**
 * Dieser Matcher beachtet, dass die Argumenttypen der beiden Methoden in unterschiedlicher Reihenfolge angegeben sein
 * koennen.
 */
public class ParamPermMethodMatcher implements MethodMatcher {

  private final Supplier<TypeMatcher> innerTypeMatcherSupplier;

  public ParamPermMethodMatcher( Supplier<TypeMatcher> innerTypeMatcherSupplier ) {
    this.innerTypeMatcherSupplier = innerTypeMatcherSupplier;
  }

  @Override
  public MatcherRate matchesWithRating( Method checkMethod, Method queryMethod ) {
    MethodStructure ms1 = MethodStructure.createFromDeclaredMethod( checkMethod );
    MethodStructure ms2 = MethodStructure.createFromDeclaredMethod( queryMethod );
    return getMatchRating( ms1, ms2 );
  }

  private MatcherRate getMatchRating( MethodStructure ms1, MethodStructure ms2 ) {
    MatcherRate returnTypeRating = innerTypeMatcherSupplier.get().matchesWithRating( ms1.getReturnType(),
        ms2.getReturnType() );
    if ( returnTypeRating == null ) {
      return null;
    }
    if ( ms1.getSortedArgumentTypes().length != ms2.getSortedArgumentTypes().length ) {
      return null;
    }

    Collection<MatcherRate> rates = getMatchRatingWithPermutedArguments( ms1.getSortedArgumentTypes(),
        ms2.getSortedArgumentTypes(),
        this::getMatchRatingWithArgumentTypes );

    rates.add( returnTypeRating );
    MatcherRate resultingRate = new MatcherRate();
    resultingRate.add( this.getClass().getSimpleName(), Setting.PARAM_PERM_METHOD_TYPE_MATCHER_BASE_RATING );
    resultingRate.add( Setting.QUALITATIVE_COMPONENT_METHOD_MATCH_RATE_CUMULATION
        .apply( rates.stream() ) );
    return resultingRate;
  }

  private Collection<MatcherRate> getMatchRatingWithArgumentTypes( Class<?>[] argumentTypes1,
      Class<?>[] argumentTypes2 ) {
    Collection<MatcherRate> rates = new ArrayList<>();
    for ( int i = 0; i < argumentTypes1.length; i++ ) {
      MatcherRate innerMatcherRating = innerTypeMatcherSupplier.get().matchesWithRating( argumentTypes1[i],
          argumentTypes2[i] );
      if ( innerMatcherRating == null ) {
        return null;
      }
      rates.add( innerMatcherRating );
    }
    return rates;
  }

  private Collection<MatcherRate> getMatchRatingWithPermutedArguments( Class<?>[] sortedArgumentTypes1,
      Class<?>[] sortedArgumentTypes2,
      BiFunction<Class<?>[], Class<?>[], Collection<MatcherRate>> matchingFunction ) {
    Collection<Class<?>[]> permutations = permuteAgruments( sortedArgumentTypes1 ).values();
    for ( Class<?>[] combination : permutations ) {
      Collection<MatcherRate> argRating = matchingFunction.apply( combination, sortedArgumentTypes2 );
      if ( argRating != null ) {
        return argRating;
      }
    }
    return new ArrayList<>();
  }

  @Override
  public boolean matches( Method m1, Method m2 ) {
    MethodStructure ms1 = MethodStructure.createFromDeclaredMethod( m1 );
    MethodStructure ms2 = MethodStructure.createFromDeclaredMethod( m2 );
    return matches( ms1, ms2 );
  }

  private boolean matches( MethodStructure ms1, MethodStructure ms2 ) {
    if ( !innerTypeMatcherSupplier.get().matchesType( ms1.getReturnType(), ms2.getReturnType() ) ) {
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
      if ( !innerTypeMatcherSupplier.get().matchesType( argumentTypes1[i], argumentTypes2[i] ) ) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Collection<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod ) {
    if ( !matches( checkMethod, queryMethod ) ) {
      return new ArrayList<>();
    }
    MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( checkMethod, queryMethod );
    Collection<ModuleMatchingInfo> returnTypeMatchingInfos = innerTypeMatcherSupplier.get()
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
            innerTypeMatcherSupplier.get().calculateTypeMatchingInfos( checkParameter, queryParameter ) );
      }
      infos.add( infoMap );
    }
    return infos;
  }

}
