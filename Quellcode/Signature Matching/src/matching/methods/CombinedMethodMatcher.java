package matching.methods;

import java.lang.reflect.Method;

public class CombinedMethodMatcher implements MethodMatcher {

  @Override
  public boolean matches( Method m1, Method m2 ) {
    MethodStructure ms1 = MethodStructure.createFromDeclaredMethod( m1 );
    MethodStructure ms2 = MethodStructure.createFromDeclaredMethod( m2 );
    return returnTypeMatched( ms1.getReturnType(), ms2.getReturnType() ) && argumentsMatches( ms1, ms2 );
  }

  private boolean argumentsMatches( MethodStructure ms1, MethodStructure ms2 ) {
    // TODO Auto-generated method stub
    return false;
  }

  private static boolean returnTypeMatched( Class<?> returnType1, Class<?> returnType2 ) {
    if ( GenSpecMethodMatcher.machtesGenSpecType( returnType1, returnType2 ) ) {
      return true;
    }
    return WrappedTypeMethodMatcher.matchesWrapped( returnType1, returnType2,
        CombinedMethodMatcher::returnTypeMatched );
  }

}
