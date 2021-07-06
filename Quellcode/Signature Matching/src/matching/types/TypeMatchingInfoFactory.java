package matching.types;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import matching.MethodMatchingInfoCombinator;
import matching.methods.MethodMatchingInfo;
import util.Logger;

public class TypeMatchingInfoFactory {

  private final Class<?> targetType;

  private final Class<?> sourceType;

  private final String sourceDelegateAttribute;

  private final String targetDelegateAttribute;

  public TypeMatchingInfoFactory( Class<?> targetType, Class<?> sourceType ) {
    this.targetType = targetType;
    this.targetDelegateAttribute = null;
    this.sourceType = sourceType;
    this.sourceDelegateAttribute = null;
  }

  public TypeMatchingInfoFactory( Class<?> targetType, Class<?> sourceType, String sourceDelegateAttribute ) {
    this.targetType = targetType;
    this.targetDelegateAttribute = null;
    this.sourceType = sourceType;
    this.sourceDelegateAttribute = sourceDelegateAttribute;
  }

  public TypeMatchingInfoFactory( Class<?> targetType, String targetDelegateAttribute, Class<?> sourceType ) {
    this.targetType = targetType;
    this.targetDelegateAttribute = targetDelegateAttribute;
    this.sourceType = sourceType;
    this.sourceDelegateAttribute = null;
  }

  public TypeMatchingInfo create() {
    return this.create( new ArrayList<>() );
  }

  public TypeMatchingInfo create( Collection<MethodMatchingInfo> methodMatchingInfos ) {
    if ( sourceDelegateAttribute != null && targetDelegateAttribute == null ) {
      return new TypeMatchingInfo( sourceType, sourceDelegateAttribute, targetType, methodMatchingInfos );
    }
    else if ( sourceDelegateAttribute == null && targetDelegateAttribute != null ) {
      return new TypeMatchingInfo( sourceType, targetType, targetDelegateAttribute, methodMatchingInfos );
    }
    else if ( sourceDelegateAttribute == null && targetDelegateAttribute == null ) {
      return new TypeMatchingInfo( sourceType, targetType, methodMatchingInfos );
    }
    return null;
  }

  public Set<TypeMatchingInfo> createFromMethodMatchingInfos(
      Map<Method, Collection<MethodMatchingInfo>> possibleMethodMatches ) {
    if ( possibleMethodMatches.isEmpty() ) {
      Set<TypeMatchingInfo> set = new HashSet<>();
      set.add( create() );
      return set;
    }
    Logger.infoF( "start permutation of MethodMatchingInfos: %d",
        possibleMethodMatches.values().stream().filter( v -> !v.isEmpty() ).map( v -> v.size() )
            .reduce( ( a, b ) -> a * b ).orElse( 0 ) );
    Collection<Collection<MethodMatchingInfo>> permutedMethodMatches = new MethodMatchingInfoCombinator()
        .generateMethodMatchingCombinations( possibleMethodMatches );
    Logger.infoF( "MethodMatches permuted: %d", permutedMethodMatches.size() );
    return permutedMethodMatches.stream().map( this::create ).collect( Collectors.toSet() );
  }

}
