package matching.modules;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import matching.methods.CombinedMethodMatcher;
import matching.methods.MethodMatcher;
import matching.methods.MethodMatchingInfo;
import util.Logger;

public class ModuleMatcher<S> {

  private final MethodMatcher methodMatcher;

  private final Class<S> queryType;

  /**
   * Nur für TestSupport
   *
   * @param queryType
   * @param methodMatcher
   */
  public ModuleMatcher( Class<S> queryType, MethodMatcher methodMatcher ) {
    this.queryType = queryType;
    this.methodMatcher = methodMatcher;

  }

  public ModuleMatcher( Class<S> queryType ) {
    this.queryType = queryType;
    this.methodMatcher = new CombinedMethodMatcher();
  }

  /**
   * @param checkType
   * @param queryType
   *          must be an interface
   * @return checkType >= queryType
   */
  public <T> boolean matches( Class<T> checkType ) {
    if ( !queryType.isInterface() ) {
      throw new RuntimeException( "Query-Type must be an interface" );
    }
    Logger.info( String.format( "%s MATCH? %s", checkType.getSimpleName(), queryType.getSimpleName() ) );
    Method[] queryMethods = getQueryMethods();
    Map<Method, Collection<Method>> possibleMatches = collectPossibleMatches( queryMethods, checkType.getMethods() );
    printPossibleMatches( possibleMatches );
    return possibleMatches.values().stream().noneMatch( Collection::isEmpty );
  }

  /**
   * @param checkType
   * @param queryType
   *          must be an interface
   * @return Exists m1 checkType, m2 in queryType: m1 matches m2
   */
  public <T> boolean partlyMatches( Class<T> checkType ) {
    if ( !queryType.isInterface() ) {
      throw new RuntimeException( "Query-Type must be an interface" );
    }
    Logger.info( String.format( "%s MATCH? %s", checkType.getSimpleName(), queryType.getSimpleName() ) );
    Method[] queryMethods = getQueryMethods();
    Map<Method, Collection<Method>> possibleMatches = collectPossibleMatches( queryMethods, checkType.getMethods() );
    printPossibleMatches( possibleMatches );
    return possibleMatches.values().stream().anyMatch( l -> !l.isEmpty() );
  }

  /**
   * Diese Methode stellt alle möglichen Kombinationen von Matches der beiden übergebenen Typen her.
   *
   * @param checkType
   * @param queryType
   * @return
   */
  public Set<ModuleMatchingInfo> calculateMatchingInfos( Class<?> checkType ) {
    ModuleMatchingInfoFactory factory = new ModuleMatchingInfoFactory( checkType, queryType );
    if ( queryType.equals( Object.class ) ) {
      // Dieser Spezialfall führt ohne diese Sonderregelung in einen Stackoverflow, da Object als Typ immer wieder
      // auftaucht. Es ist also eine Abbruchbedingung.
      Set<ModuleMatchingInfo> singleResult = new HashSet<>();
      singleResult.add( factory.create() );
      return singleResult;
    }
    Method[] queryMethods = getQueryMethods();
    Map<Method, Collection<Method>> possibleMatches = collectPossibleMatches( queryMethods, checkType.getMethods() );
    Map<Method, Set<MethodMatchingInfo>> possibleMethodMatches = collectMethodMatchingInfos( queryMethods,
        possibleMatches );
    return factory.createFromMethodMatchingInfos( possibleMethodMatches );

  }

  // /**
  // * @param checkType
  // * @param queryType
  // * must be an interface
  // * @return matchende Methoden
  // */
  // public Map<Method, Collection<Method>> getMatchingMethods( Class<?> checkType ) {
  // if ( partlyMatches( checkType ) ) {
  // throw new RuntimeException( "Check-Type does not match Query-Type" );
  // }
  // Method[] queryMethods = getQueryMethods();
  // Map<Method, Collection<Method>> possibleMatches = collectPossibleMatches( queryMethods, checkType.getMethods() );
  // return possibleMatches;
  // }

  // Hier müssen alle Methoden der Klasse Object herausgefiltert werden, weil:
  // 1. ohnehin alle Objekte mit diesen Methoden umgehen könne
  // 2. ein Interface die dazu passenden Methoden-Signaturen nicht ausweist

  // Einsicht: Das Matching mit Klassen oder Enums als Query-Typ ist etwas kompliziert. Ich beschränke mich erst einmal
  // nur auf Interfaces als Query-Typ. Das ist auch hinsichtlich meines Anwendungsfalls eher relevant.

  // Weiteres Problem: Native Typen haben keine Methoden!!!
  private Method[] getQueryMethods() {
    Method[] queryMethods = queryType.getMethods();
    return queryMethods;
  }

  private Map<Method, Collection<Method>> collectPossibleMatches( Method[] queryMethods, Method[] checkMethods ) {
    Map<Method, Collection<Method>> matches = new HashMap<>();
    for ( Method queryMethod : queryMethods ) {
      Collection<Method> queryMethodMatches = new ArrayList<>();
      for ( Method checkMethod : checkMethods ) {
        if ( methodMatcher.matches( checkMethod, queryMethod ) ) {
          queryMethodMatches.add( checkMethod );
        }
      }
      matches.put( queryMethod, queryMethodMatches );
    }
    return matches;
  }

  private Map<Method, Set<MethodMatchingInfo>> collectMethodMatchingInfos( Method[] queryMethods,
      Map<Method, Collection<Method>> possibleMatches ) {
    Map<Method, Set<MethodMatchingInfo>> matches = new HashMap<>();
    for ( Method queryMethod : queryMethods ) {
      Set<MethodMatchingInfo> matchingInfosOfQueryMethod = new HashSet<>();
      for ( Method checkMethod : possibleMatches.get( queryMethod ) ) {
        Set<MethodMatchingInfo> matchingInfos = methodMatcher.calculateMatchingInfos( checkMethod, queryMethod );
        matchingInfosOfQueryMethod.addAll( matchingInfos );
      }
      matches.put( queryMethod, matchingInfosOfQueryMethod );
    }
    return matches;

  }

  private void printPossibleMatches( Map<Method, Collection<Method>> possibleMatches ) {
    for ( Entry<Method, Collection<Method>> entry : possibleMatches.entrySet() ) {
      Logger.info( String.format( "QUERYM: %s", entry.getKey().getName() ) );
      for ( Method match : entry.getValue() ) {
        Logger.info( String.format( "    MATCHM: %s", match.getName() ) );
      }
    }

  }

}
