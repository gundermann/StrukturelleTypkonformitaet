package matching.methods;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.modules.ExactTypeMatcher;
import matching.modules.ModuleMatchingInfo;
import matching.modules.TypeMatcher;

public class ExactMethodMatcher implements MethodMatcher, Comparator<MethodStructure> {

  private TypeMatcher typeMatcher = new ExactTypeMatcher();

  @Override
  public boolean matches( Method checkMethod, Method queryMethod ) {
    MethodStructure cms1 = MethodStructure.createFromDeclaredMethod( checkMethod );
    MethodStructure qms2 = MethodStructure.createFromDeclaredMethod( queryMethod );
    return Objects.compare( cms1, qms2, this ) == 0;
  }

  @Override
  public int compare( MethodStructure check, MethodStructure query ) {
    if ( !typeMatcher.matchesType( check.getReturnType(), query.getReturnType() ) ) {
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
  public Collection<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod ) {
    if ( !matches( checkMethod, queryMethod ) ) {
      return new ArrayList<>();
    }
    // da es ein exakter Match sein muss, darf hier nur eine MethodMatchingInfo erzeugt werden
    MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( checkMethod, queryMethod );
    Collection<ModuleMatchingInfo> returnTypeMatchingInfos = typeMatcher.calculateTypeMatchingInfos(
        queryMethod.getReturnType(), checkMethod.getReturnType() );
    Map<ParamPosition, Collection<ModuleMatchingInfo>> argumentTypesMatchingInfos = calculateArgumentMatchingInfos(
        checkMethod, queryMethod );
    return factory.createFromTypeMatchingInfos( returnTypeMatchingInfos,
        Collections.singletonList( argumentTypesMatchingInfos ) );
  }

  private Map<ParamPosition, Collection<ModuleMatchingInfo>> calculateArgumentMatchingInfos( Method source,
      Method target ) {
    Parameter[] sourceParameters = source.getParameters();
    Parameter[] targetParameters = target.getParameters();
    Map<ParamPosition, Collection<ModuleMatchingInfo>> matchingInfoMap = new HashMap<>();
    for ( int i = 0; i < sourceParameters.length; i++ ) {
      Parameter sourceParameter = sourceParameters[i];
      Parameter targetParameter = targetParameters[i];
      matchingInfoMap.put( new ParamPosition( i, i ),
          typeMatcher.calculateTypeMatchingInfos( targetParameter.getType(), sourceParameter.getType() ) );
    }
    return matchingInfoMap;
  }

}
