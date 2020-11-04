package matching.modules;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import matching.methods.MethodMatchingInfo;

public class ModuleMatchingInfoFactory {

  private final Class<?> targetType;

  private final Class<?> sourceType;

  public ModuleMatchingInfoFactory( Class<?> targetType, Class<?> sourceType ) {
    this.targetType = targetType;
    this.sourceType = sourceType;
  }

  public ModuleMatchingInfo create() {
    return this.create( new HashSet<>() );
  }

  public ModuleMatchingInfo create( Set<MethodMatchingInfo> methodMatchingInfos ) {
    return new ModuleMatchingInfo( sourceType, targetType, methodMatchingInfos );
  }

  public Set<ModuleMatchingInfo> createFromMethodMatchingInfos(
      Map<Method, Set<MethodMatchingInfo>> possibleMethodMatches ) {
    Set<Set<MethodMatchingInfo>> permutedMethodMatches = generateMethodMatchingCombinations(
        possibleMethodMatches );
    return permutedMethodMatches.stream().map( this::create ).collect( Collectors.toSet() );
  }

  private Set<Set<MethodMatchingInfo>> generateMethodMatchingCombinations(
      Map<Method, Set<MethodMatchingInfo>> possibleMethodMatches ) {
    Set<Set<MethodMatchingInfo>> combinations = new HashSet<>();
    // erste Methode holen
    Iterator<Method> iterator = possibleMethodMatches.keySet().iterator();
    if ( !iterator.hasNext() ) {
      // keine Methode mehr übrig
      return combinations;
    }
    Method selectedMethod = iterator.next();
    // possibleMethodMatches abbauen
    // Kopie der ursprünglichen Map erstellen
    Map<Method, Set<MethodMatchingInfo>> localMethodMatches = new HashMap<>( possibleMethodMatches );
    Set<MethodMatchingInfo> selectedMethodMatches = localMethodMatches.remove( selectedMethod );
    if ( selectedMethodMatches.isEmpty() ) {
      return generateMethodMatchingCombinations( localMethodMatches );
    }
    for ( MethodMatchingInfo info : selectedMethodMatches ) {
      Set<Set<MethodMatchingInfo>> otherCombinations = generateMethodMatchingCombinations( localMethodMatches );
      if ( otherCombinations.isEmpty() ) {
        Set<MethodMatchingInfo> singleInfos = new HashSet<>();
        singleInfos.add( info );
        combinations.add( singleInfos );
        continue;
      }
      for ( Set<MethodMatchingInfo> otherInfos : otherCombinations ) {
        otherInfos.add( info );
      }
      combinations.addAll( otherCombinations );
    }
    return combinations;
  }

}
