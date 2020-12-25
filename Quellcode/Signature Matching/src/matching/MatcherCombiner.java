package matching;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Supplier;

import matching.methods.MethodMatcher;
import matching.methods.MethodMatchingInfo;
import matching.modules.ModuleMatchingInfo;
import matching.modules.TypeMatcher;

public abstract class MatcherCombiner {
  private MatcherCombiner() {
  }

  public static Supplier<MethodMatcher> combine( MethodMatcher... matcher ) {
    return () -> new MethodMatcher() {

      @Override
      public boolean matches( Method checkMethod, Method queryMethod ) {
        for ( MethodMatcher m : matcher ) {
          if ( m.matches( checkMethod, queryMethod ) ) {
            return true;
          }
        }
        return false;
      }

      @Override
      public Collection<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod ) {
        for ( MethodMatcher m : matcher ) {
          if ( m.matches( checkMethod, queryMethod ) ) {
            return m.calculateMatchingInfos( checkMethod, queryMethod );
          }
        }
        return new HashSet<>();
      }

      @Override
      public MatcherRate matchesWithRating( Method checkMethod, Method queryMethod ) {
        for ( MethodMatcher m : matcher ) {
        	MatcherRate rate = m.matchesWithRating( checkMethod, queryMethod );
          if ( rate != null ) {
            return rate;
          }
        }
        return null;
      }

    };
  }

  public static Supplier<TypeMatcher> combine( TypeMatcher... matcher ) {
    return () -> new TypeMatcher() {

      @Override
      public boolean matchesType( Class<?> checkType, Class<?> queryType ) {
        for ( TypeMatcher m : matcher ) {
          if ( m.matchesType( checkType, queryType ) ) {
            return true;
          }
        }
        return false;
      }

      @Override
      public Collection<ModuleMatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType ) {
        for ( TypeMatcher m : matcher ) {
          if ( m.matchesType( checkType, queryType ) ) {
            return m.calculateTypeMatchingInfos( checkType, queryType );
          }
        }
        return new ArrayList<>();
      }

      @Override
      public MatcherRate matchesWithRating( Class<?> checkType, Class<?> queryType ) {
        for ( TypeMatcher m : matcher ) {
          MatcherRate rating = m.matchesWithRating( checkType, queryType );
          if ( rating != null ) {
            return rating;
          }
        }
        return null;
      }

    };
  }
}
