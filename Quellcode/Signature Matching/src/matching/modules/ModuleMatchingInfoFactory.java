package matching.modules;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import matching.methods.MethodMatchingInfo;
import util.Permuter;

public class ModuleMatchingInfoFactory<S, T> {

  private final Class<T> targetType;

  private final Class<S> sourceType;

  public ModuleMatchingInfoFactory( Class<T> targetType, Class<S> sourceType ) {
    this.targetType = targetType;
    this.sourceType = sourceType;
  }

  public ModuleMatchingInfo<S, T> create( Set<MethodMatchingInfo> methodMatchingInfos ) {
    return new ModuleMatchingInfo<>( sourceType, targetType, methodMatchingInfos );
  }

  public Set<ModuleMatchingInfo<S, T>> createFromMethodMatchingInfos(
      Map<Method, Set<MethodMatchingInfo>> possibleMethodMatches ) {
    Set<Set<MethodMatchingInfo>> permutedMethodMatches = generateMethodMatchingCombinations(
        possibleMethodMatches );
    return permutedMethodMatches.stream().map( this::create ).collect( Collectors.toSet() );
  }

  private Set<Set<MethodMatchingInfo>> generateMethodMatchingCombinations(
      Map<Method, Set<MethodMatchingInfo>> possibleMethodMatches ) {
    Set<Set<MethodMatchingInfo>> permutedValues = new HashSet<>();
    // Kopie der ursprünglichen Map erstellen
    Map<Method, Set<MethodMatchingInfo>> localMethodMatches = new HashMap<>( possibleMethodMatches );
    // erste Methode holen
    Iterator<Method> iterator = possibleMethodMatches.keySet().iterator();
    if ( !iterator.hasNext() ) {
      // keine Methode mehr übrig
      return permutedValues;
    }
    Method selectedMethod = iterator.next();
    // possibleMethodMatches abbauen
    Set<MethodMatchingInfo> selectedMethodMatches = localMethodMatches.remove( selectedMethod );
    // selektierte Matches permutieren
    Collection<MethodMatchingInfo[]> permutations = new ArrayList<>();
    int permutationCount = Permuter.fractional( selectedMethodMatches.size() );
    Permuter.permuteRecursive( permutationCount, selectedMethodMatches.toArray( new MethodMatchingInfo[] {} ),
        permutations );

    // ueber Permutationen iterieren
    for ( MethodMatchingInfo[] permutationItem : permutations ) {
      Set<Set<MethodMatchingInfo>> valuePermutationsWithoutSelected = generateMethodMatchingCombinations(
          localMethodMatches );
      for ( Set<MethodMatchingInfo> permutationWithoutSelected : valuePermutationsWithoutSelected ) {
        permutationWithoutSelected.addAll( Arrays.asList( permutationItem ) );
        permutedValues.add( permutationWithoutSelected );
      }
    }
    return permutedValues;
  }
}
