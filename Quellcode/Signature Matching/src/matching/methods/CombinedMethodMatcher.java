package matching.methods;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

import matching.modules.ModuleMatchingInfo;

public class CombinedMethodMatcher implements MethodMatcher {
  ExactMethodMatcher exactMethodMatcher = new ExactMethodMatcher();

  GenSpecMethodMatcher genSpecMethodMatcher = new GenSpecMethodMatcher();

  MethodMatcher combination = new ParamPermMethodMatcher(
      MatcherCombiner.combine( genSpecMethodMatcher, exactMethodMatcher,
          new WrappedTypeMethodMatcher( MatcherCombiner.combine( genSpecMethodMatcher,
              exactMethodMatcher ) ) ) );

  @Override
  public boolean matches( Method checkMethod, Method queryMethod ) {
    // MethodStructure ms1 = MethodStructure.createFromDeclaredMethod( m1 );
    // MethodStructure ms2 = MethodStructure.createFromDeclaredMethod( m2 );
    // return paramtypeMatches( ms1.getReturnType(), ms2.getReturnType() ) && argumentsMatches( ms1, ms2 );
    return combination.matches( checkMethod, queryMethod );
  }

  // private boolean argumentsMatches( MethodStructure ms1, MethodStructure ms2 ) {
  // Class<?>[] sortedArgumentTypes1 = ms1.getSortedArgumentTypes();
  // Class<?>[] sortedArgumentTypes2 = ms2.getSortedArgumentTypes();
  //
  // // Wenn der ContaineredArgumentMatcher hinzukommt, dann muss das hier wieder weg
  // if ( ms1.getSortedArgumentTypes().length != ms2.getSortedArgumentTypes().length ) {
  // return false;
  // }
  // return paramPermTypeMethodMatcher.matchPermutedArguments( sortedArgumentTypes1, sortedArgumentTypes2,
  // this::typesMatches );
  // }
  //
  // private boolean typesMatches( Class<?>[] types1, Class<?>[] types2 ) {
  // for ( int i = 0; i < types1.length; i++ ) {
  // if ( !typeMatches( types1[i], types2[i] ) ) {
  // return false;
  // }
  // }
  // return types1.length == types2.length;
  // }
  //
  // private boolean typeMatches( Class<?> returnType1, Class<?> returnType2 ) {
  // if ( genSpecMethodMatcher.machtesGenSpecType( returnType1, returnType2 ) ) {
  // return true;
  // }
  // return wrappedTypeMethodMatcher.matchesWrapped( returnType1, returnType2,
  // ( t1, t2 ) -> typeMatches( t1, t2 ) );
  // }

  @Override
  public Set<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod ) {
    return combination.calculateMatchingInfos( checkMethod, queryMethod );
  }

  @Override
  public boolean matchesType( Class<?> checkType, Class<?> queryType ) {
    return combination.matchesType( checkType, queryType );
  }

  @Override
  public Collection<ModuleMatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType ) {
    return combination.calculateTypeMatchingInfos( checkType, queryType );
  }

}
