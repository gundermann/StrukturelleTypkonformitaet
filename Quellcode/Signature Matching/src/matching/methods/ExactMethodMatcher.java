package matching.methods;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Objects;

public class ExactMethodMatcher implements MethodMatcher, Comparator<MethodStructure> {

  @Override
  public boolean matches( Method m1, Method m2 ) {
    MethodStructure ms1 = MethodStructure.createFromDeclaredMethod( m1 );
    MethodStructure ms2 = MethodStructure.createFromDeclaredMethod( m2 );
    return Objects.compare( ms1, ms2, this ) == 0;
  }

  @Override
  public int compare( MethodStructure o1, MethodStructure o2 ) {
    if ( !matchesType( o1.getReturnType(), o2.getReturnType() ) ) {
      return 1;
    }
    if ( o1.getSortedArgumentTypes().length != o2.getSortedArgumentTypes().length ) {
      return 1;
    }
    for ( int i = 0; i < o1.getSortedArgumentTypes().length; i++ ) {
      if ( o1.getSortedArgumentTypes()[i] != o2.getSortedArgumentTypes()[i] ) {
        return 1;
      }
    }
    return 0;
  }

  boolean matchesType( Class<?> t1, Class<?> t2 ) {
    return t1.equals( t2 );
  }

}
