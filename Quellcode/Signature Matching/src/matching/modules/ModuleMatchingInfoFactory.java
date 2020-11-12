package matching.modules;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import matching.methods.MethodMatchingInfo;
import util.Logger;

public class ModuleMatchingInfoFactory {

  private final Class<?> targetType;

  private final Class<?> sourceType;

  private final String sourceDelegateAttribute;

  private final String targetDelegateAttribute;

  public ModuleMatchingInfoFactory( Class<?> targetType, Class<?> sourceType ) {
    this.targetType = targetType;
    this.targetDelegateAttribute = null;
    this.sourceType = sourceType;
    this.sourceDelegateAttribute = null;
  }

  public ModuleMatchingInfoFactory( Class<?> targetType, Class<?> sourceType, String sourceDelegateAttribute ) {
    this.targetType = targetType;
    this.targetDelegateAttribute = null;
    this.sourceType = sourceType;
    this.sourceDelegateAttribute = sourceDelegateAttribute;
  }

  public ModuleMatchingInfoFactory( Class<?> targetType, String targetDelegateAttribute, Class<?> sourceType ) {
    this.targetType = targetType;
    this.targetDelegateAttribute = targetDelegateAttribute;
    this.sourceType = sourceType;
    this.sourceDelegateAttribute = null;
  }

  public ModuleMatchingInfo create() {
    return this.create( new ArrayList<>() );
  }

  public ModuleMatchingInfo create( Collection<MethodMatchingInfo> methodMatchingInfos ) {
    if ( sourceDelegateAttribute != null && targetDelegateAttribute == null ) {
      return new ModuleMatchingInfo( sourceType, sourceDelegateAttribute, targetType, methodMatchingInfos );
    }
    else if ( sourceDelegateAttribute == null && targetDelegateAttribute != null ) {
      return new ModuleMatchingInfo( sourceType, targetType, targetDelegateAttribute, methodMatchingInfos );
    }
    else if ( sourceDelegateAttribute == null && targetDelegateAttribute == null ) {
      return new ModuleMatchingInfo( sourceType, targetType, methodMatchingInfos );
    }
    return null;
  }

  public Set<ModuleMatchingInfo> createFromMethodMatchingInfos(
      Map<Method, Collection<MethodMatchingInfo>> possibleMethodMatches ) {
    Logger.infoF( "start permutation of MethodMatchingInfos: %d",
        possibleMethodMatches.values().stream().filter( v -> !v.isEmpty() ).map( v -> v.size() )
            .reduce( ( a, b ) -> a * b ).orElse( 0 ) );
    Collection<Collection<MethodMatchingInfo>> permutedMethodMatches = generateMethodMatchingCombinations(
        possibleMethodMatches );
    Logger.infoF( "MethodMatches permuted: %d", permutedMethodMatches.size() );
    return permutedMethodMatches.stream().map( this::create ).collect( Collectors.toSet() );
  }

  private Collection<Collection<MethodMatchingInfo>> generateMethodMatchingCombinations(
      Map<Method, Collection<MethodMatchingInfo>> possibleMethodMatches ) {
    Collection<Collection<MethodMatchingInfo>> combinations = new ArrayList<>();
    // erste Methode holen
    Iterator<Method> iterator = possibleMethodMatches.keySet().iterator();
    if ( !iterator.hasNext() ) {
      // keine Methode mehr übrig
      return combinations;
    }
    Method selectedMethod = iterator.next();
    // possibleMethodMatches abbauen
    // Kopie der ursprünglichen Map erstellen
    Map<Method, Collection<MethodMatchingInfo>> localMethodMatches = new HashMap<>( possibleMethodMatches );
    Collection<MethodMatchingInfo> selectedMethodMatches = localMethodMatches.remove( selectedMethod );
    if ( selectedMethodMatches.isEmpty() ) {
      return generateMethodMatchingCombinations( localMethodMatches );
    }
    for ( MethodMatchingInfo info : selectedMethodMatches ) {
      Collection<Collection<MethodMatchingInfo>> otherCombinations = generateMethodMatchingCombinations(
          localMethodMatches );
      if ( otherCombinations.isEmpty() ) {
        Collection<MethodMatchingInfo> singleInfos = new ArrayList<>();
        singleInfos.add( info );
        combinations.add( singleInfos );
        continue;
      }
      for ( Collection<MethodMatchingInfo> otherInfos : otherCombinations ) {
        otherInfos.add( info );
      }
      combinations.addAll( otherCombinations );
    }
    return combinations;
  }

}
