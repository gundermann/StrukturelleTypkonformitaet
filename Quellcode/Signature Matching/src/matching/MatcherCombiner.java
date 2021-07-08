package matching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import matching.types.CombinableTypeMatcher;
import matching.types.TypeMatcher;

public final class MatcherCombiner {
  private MatcherCombiner() {
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
      public Collection<MatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType ) {
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
