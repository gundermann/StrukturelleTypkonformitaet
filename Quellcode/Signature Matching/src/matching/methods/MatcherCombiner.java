package matching.methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import matching.modules.ModuleMatchingInfo;

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
      public Set<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod ) {
        for ( MethodMatcher m : matcher ) {
          if ( m.matches( checkMethod, queryMethod ) ) {
            return m.calculateMatchingInfos( checkMethod, queryMethod );
          }
        }
        return new HashSet<>();
      }

      @Override
      public boolean matchesType( Class<?> checkType, Class<?> queryType ) {
        for ( MethodMatcher m : matcher ) {
          if ( m.matchesType( checkType, queryType ) ) {
            return true;
          }
        }
        return false;
      }

      @Override
      public Collection<ModuleMatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType ) {
        for ( MethodMatcher m : matcher ) {
          if ( m.matchesType( checkType, queryType ) ) {
            return m.calculateTypeMatchingInfos( checkType, queryType );
          }
        }
        return new ArrayList<>();
      }
    };
  }

}