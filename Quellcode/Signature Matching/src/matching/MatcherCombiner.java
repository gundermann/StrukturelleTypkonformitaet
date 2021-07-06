package matching;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

import matching.methods.MethodMatcher;
import matching.methods.MethodMatchingInfo;
import matching.types.CombinableTypeMatcher;
import matching.types.TypeMatcher;
import matching.types.TypeMatchingInfo;

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

  public static Supplier<TypeMatcher> combine( CombinableTypeMatcher... matcher ) {
    return () -> new TypeMatcher() {

      @Override
      public boolean matchesType( Class<?> checkType, Class<?> queryType ) {

        for ( CombinableTypeMatcher m : getSortedMatcher() ) {
          if ( m.matchesType( checkType, queryType ) ) {
            return true;
          }
        }
        return false;
      }

      private Collection<CombinableTypeMatcher> getSortedMatcher() {
        List<CombinableTypeMatcher> matcherList = Arrays.asList( matcher );
        Collections.sort( matcherList,
            ( l1, l2 ) -> Double.compare( l1.getTypeMatcherRate(), l2.getTypeMatcherRate() ) );
        return matcherList;
      }

      @Override
      public Collection<TypeMatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType ) {
        for ( CombinableTypeMatcher m : getSortedMatcher() ) {
          if ( m.matchesType( checkType, queryType ) ) {
            return m.calculateTypeMatchingInfos( checkType, queryType );
          }
        }
        return new ArrayList<>();
      }

      @Override
      public MatcherRate matchesWithRating( Class<?> checkType, Class<?> queryType ) {
        for ( CombinableTypeMatcher m : getSortedMatcher() ) {
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
