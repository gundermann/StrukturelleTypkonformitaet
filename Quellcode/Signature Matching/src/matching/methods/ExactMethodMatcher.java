package matching.methods;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import matching.modules.ModuleMatchingInfo;
import matching.modules.ModuleMatchingInfoFactory;

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

  @Override
  public Set<MethodMatchingInfo> calculateMatchingInfos( Method m1, Method m2 ) {
    if ( !matches( m1, m2 ) ) {
      return new HashSet<>();
    }
    // da es ein exakter Match sein muss, darf hier nur eine MethodMatchingInfo erzeugt werden
    MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( m1, m2 );
    Collection<ModuleMatchingInfo<?>> returnTypeMatchingInfos = calculateReturnTypeMatchingInfos( m1, m2 );
    Collection<Map<Integer, ModuleMatchingInfo<?>>> argumentTypesMatchingInfos = calculateArgumentMatchingInfos(
        m1, m2 );
    return factory.createFromTypeMatchingInfos( returnTypeMatchingInfos, argumentTypesMatchingInfos );
  }

  Collection<Map<Integer, ModuleMatchingInfo<?>>> calculateArgumentMatchingInfos( Method source, Method target ) {
    Parameter[] sourceParameters = source.getParameters();
    Parameter[] targetParameters = target.getParameters();
    Map<Integer, ModuleMatchingInfo<?>> matchingInfoMap = new HashMap<>();
    for ( int i = 0; i < sourceParameters.length; i++ ) {
      Parameter sourceParameter = sourceParameters[i];
      Parameter targetParameter = targetParameters[i];
      ModuleMatchingInfoFactory<?, ?> factory = new ModuleMatchingInfoFactory<>(
          targetParameter.getType(), sourceParameter.getType() );
      matchingInfoMap.put( i, factory.create() );
    }
    return Collections.singletonList( matchingInfoMap );
  }

  Collection<ModuleMatchingInfo<?>> calculateReturnTypeMatchingInfos( Method source, Method target ) {
    Class<?> sourceReturnType = source.getReturnType();
    Class<?> targetReturnType = target.getReturnType();
    return Collections
        .singletonList( new ModuleMatchingInfoFactory<>( targetReturnType, sourceReturnType ).create() );
  }

}
