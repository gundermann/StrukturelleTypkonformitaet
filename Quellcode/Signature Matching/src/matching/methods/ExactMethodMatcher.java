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
  public boolean matches( Method checkMethod, Method queryMethod ) {
    MethodStructure cms1 = MethodStructure.createFromDeclaredMethod( checkMethod );
    MethodStructure qms2 = MethodStructure.createFromDeclaredMethod( queryMethod );
    return Objects.compare( cms1, qms2, this ) == 0;
  }

  @Override
  public int compare( MethodStructure check, MethodStructure query ) {
    if ( !matchesType( check.getReturnType(), query.getReturnType() ) ) {
      return 1;
    }
    if ( check.getSortedArgumentTypes().length != query.getSortedArgumentTypes().length ) {
      return 1;
    }
    for ( int i = 0; i < check.getSortedArgumentTypes().length; i++ ) {
      if ( check.getSortedArgumentTypes()[i] != query.getSortedArgumentTypes()[i] ) {
        return 1;
      }
    }
    return 0;
  }

  @Override
  public boolean matchesType( Class<?> checkType, Class<?> queryType ) {
    return checkType.equals( queryType );
  }

  @Override
  public Set<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod ) {
    if ( !matches( checkMethod, queryMethod ) ) {
      return new HashSet<>();
    }
    // da es ein exakter Match sein muss, darf hier nur eine MethodMatchingInfo erzeugt werden
    MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( checkMethod, queryMethod );
    Collection<ModuleMatchingInfo> returnTypeMatchingInfos = calculateTypeMatchingInfos(
        queryMethod.getReturnType(), checkMethod.getReturnType() );
    Map<Integer, Collection<ModuleMatchingInfo>> argumentTypesMatchingInfos = calculateArgumentMatchingInfos(
        checkMethod, queryMethod );
    return factory.createFromTypeMatchingInfos( returnTypeMatchingInfos, argumentTypesMatchingInfos );
  }

  private Map<Integer, Collection<ModuleMatchingInfo>> calculateArgumentMatchingInfos( Method source, Method target ) {
    Parameter[] sourceParameters = source.getParameters();
    Parameter[] targetParameters = target.getParameters();
    Map<Integer, Collection<ModuleMatchingInfo>> matchingInfoMap = new HashMap<>();
    for ( int i = 0; i < sourceParameters.length; i++ ) {
      Parameter sourceParameter = sourceParameters[i];
      Parameter targetParameter = targetParameters[i];
      matchingInfoMap.put( i, calculateTypeMatchingInfos( targetParameter.getType(), sourceParameter.getType() ) );
    }
    return matchingInfoMap;
  }

  @Override
  public Collection<ModuleMatchingInfo> calculateTypeMatchingInfos( Class<?> targetType,
      Class<?> sourceType ) {
    return Collections
        .singletonList( new ModuleMatchingInfoFactory( targetType, sourceType ).create() );
  }

}
