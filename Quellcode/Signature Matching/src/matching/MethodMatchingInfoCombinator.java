package matching;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import matching.methods.MethodMatchingInfo;

public class MethodMatchingInfoCombinator {

  public Collection<Collection<MethodMatchingInfo>> generateMethodMatchingCombinations(
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
