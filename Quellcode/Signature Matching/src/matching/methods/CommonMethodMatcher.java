package matching.methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.modules.ModuleMatchingInfo;
import matching.modules.TypeMatcher;

public class CommonMethodMatcher implements MethodMatcher {

  // Anpassung des MatcherBaseRatings bringt nichts, da dieser Matcher nur im Test verwendet wird
  private static final double MATCHER_BASE_RATING = 0;

  private final Supplier<TypeMatcher> typeMatcherSupplier;

  public CommonMethodMatcher( Supplier<TypeMatcher> typeMatcherSupplier ) {
    this.typeMatcherSupplier = typeMatcherSupplier;
  }

  @Override
  public boolean matches( Method checkMethod, Method queryMethod ) {
    MethodStructure cms1 = MethodStructure.createFromDeclaredMethod( checkMethod );
    MethodStructure qms2 = MethodStructure.createFromDeclaredMethod( queryMethod );
    return matches( cms1, qms2 );
  }

  @Override
  public Collection<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod ) {
    if ( !matches( checkMethod, queryMethod ) ) {
      return new ArrayList<>();
    }
    MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( checkMethod, queryMethod );
    MethodStructure checkStruct = MethodStructure.createFromDeclaredMethod( checkMethod );
    MethodStructure queryStruct = MethodStructure.createFromDeclaredMethod( queryMethod );
    Collection<ModuleMatchingInfo> returnTypeMatchingInfos = typeMatcherSupplier.get().calculateTypeMatchingInfos(
        queryStruct.getReturnType(), checkStruct.getReturnType() );
    Map<ParamPosition, Collection<ModuleMatchingInfo>> argumentTypesMatchingInfos = calculateArgumentTypesMatchingInfos(
        checkStruct.getSortedArgumentTypes(), queryStruct.getSortedArgumentTypes() );
    return factory.createFromTypeMatchingInfos( returnTypeMatchingInfos,
        Collections.singletonList( argumentTypesMatchingInfos ) );
  }

  private boolean matches( MethodStructure ms1, MethodStructure ms2 ) {
    if ( ms1.getSortedArgumentTypes().length != ms2.getSortedArgumentTypes().length ) {
      return false;
    }
    if ( !typeMatcherSupplier.get().matchesType( ms1.getReturnType(), ms2.getReturnType() ) ) {
      return false;
    }
    for ( int i = 0; i < ms1.getSortedArgumentTypes().length; i++ ) {
      if ( !typeMatcherSupplier.get().matchesType( ms1.getSortedArgumentTypes()[i],
          ms2.getSortedArgumentTypes()[i] ) ) {
        return false;
      }
    }
    return true;
  }

  private Map<ParamPosition, Collection<ModuleMatchingInfo>> calculateArgumentTypesMatchingInfos(
      Class<?>[] checkATs, Class<?>[] queryATs ) {
    Map<ParamPosition, Collection<ModuleMatchingInfo>> matchingMap = new HashMap<>();
    for ( int i = 0; i < checkATs.length; i++ ) {
      Class<?> checkAT = checkATs[i];
      Class<?> queryAT = queryATs[i];
      Collection<ModuleMatchingInfo> infos = typeMatcherSupplier.get().calculateTypeMatchingInfos( checkAT, queryAT );
      matchingMap.put( new ParamPosition( i, i ), infos );
    }

    return matchingMap;
  }

  @Override
  public double matchesWithRating( Method checkMethod, Method queryMethod ) {
    MethodStructure ms1 = MethodStructure.createFromDeclaredMethod( checkMethod );
    MethodStructure ms2 = MethodStructure.createFromDeclaredMethod( queryMethod );
    return getMatchRating( ms1, ms2 );
  }

  private double getMatchRating( MethodStructure ms1, MethodStructure ms2 ) {
    if ( ms1.getSortedArgumentTypes().length != ms2.getSortedArgumentTypes().length ) {
      return -1;
    }

    double rating = typeMatcherSupplier.get().matchesWithRating( ms1.getReturnType(), ms2.getReturnType() );
    if ( rating < 0 ) {
      return rating;
    }

    for ( int i = 0; i < ms1.getSortedArgumentTypes().length; i++ ) {
      double argRating = typeMatcherSupplier.get().matchesWithRating( ms1.getSortedArgumentTypes()[i],
          ms2.getSortedArgumentTypes()[i] );
      if ( argRating < 0 ) {
        return -1;
      }
      rating += argRating;
    }
    return rating + MATCHER_BASE_RATING;
  }
}
