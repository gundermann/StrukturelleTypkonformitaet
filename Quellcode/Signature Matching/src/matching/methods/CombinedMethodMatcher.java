package matching.methods;

import java.lang.reflect.Method;

public class CombinedMethodMatcher implements MethodMatcher {

  GenSpecMethodMatcher genSpecMethodMatcher = new GenSpecMethodMatcher();

  WrappedTypeMethodMatcher wrappedTypeMethodMatcher = new WrappedTypeMethodMatcher();

  @Override
  public boolean matches( Method m1, Method m2 ) {
    MethodStructure ms1 = MethodStructure.createFromDeclaredMethod( m1 );
    MethodStructure ms2 = MethodStructure.createFromDeclaredMethod( m2 );
    return typeMatches( ms1.getReturnType(), ms2.getReturnType() ) && argumentsMatches( ms1, ms2 );
  }

  private boolean argumentsMatches( MethodStructure ms1, MethodStructure ms2 ) {
    Class<?>[] sortedArgumentTypes1 = ms1.getSortedArgumentTypes();
    Class<?>[] sortedArgumentTypes2 = ms2.getSortedArgumentTypes();

    // Wenn der ContaineredArgumentMatcher hinzukommt, dann muss das hier wieder weg
    if ( ms1.getSortedArgumentTypes().length != ms2.getSortedArgumentTypes().length ) {
      return false;
    }
    return new ParamPermMethodMatcher().matchPermutedArguments( sortedArgumentTypes1, sortedArgumentTypes2,
        this::typesMatches );
  }

  private boolean typesMatches( Class<?>[] types1, Class<?>[] types2 ) {
    for ( int i = 0; i < types1.length; i++ ) {
      if ( !typeMatches( types1[i], types2[i] ) ) {
        return false;
      }
    }
    return types1.length == types2.length;
  }

  private boolean typeMatches( Class<?> returnType1, Class<?> returnType2 ) {
    if ( genSpecMethodMatcher.machtesGenSpecType( returnType1, returnType2 ) ) {
      return true;
    }
    return wrappedTypeMethodMatcher.matchesWrapped( returnType1, returnType2,
        ( t1, t2 ) -> typeMatches( t1, t2 ) );
  }

}
