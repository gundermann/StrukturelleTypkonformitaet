package matching.modules;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import matching.Logger;
import matching.methods.CombinedMethodMatcher;
import matching.methods.MethodMatcher;

public class ModuleMatcher {

  MethodMatcher methodMatcher = new CombinedMethodMatcher();

  /**
   * @param checkType
   * @param queryType
   *          must be an interface
   * @return checkType >= queryType
   */
  public boolean matches( Class<?> checkType, Class<?> queryType ) {
    if ( !queryType.isInterface() ) {
      throw new RuntimeException( "Query-Type must be an interface" );
    }
    Logger.info( String.format( "%s MATCH? %s", checkType.getSimpleName(), queryType.getSimpleName() ) );
    Method[] queryMethods = getQueryMethods( queryType );
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
  public boolean partlyMatches( Class<?> checkType, Class<?> queryType ) {
    if ( !queryType.isInterface() ) {
      throw new RuntimeException( "Query-Type must be an interface" );
    }
    Logger.info( String.format( "%s MATCH? %s", checkType.getSimpleName(), queryType.getSimpleName() ) );
    Method[] queryMethods = getQueryMethods( queryType );
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
  public Set<ModuleMatchingInfo> calculateMatchingInfos( Class<?> checkType, Class<?> queryType ) {
    return new HashSet<>();
  }

  /**
   * @param checkType
   * @param queryType
   *          must be an interface
   * @return matchende Methoden
   */
  public Map<Method, Collection<Method>> getMatchingMethods( Class<?> checkType, Class<?> queryType ) {
    if ( partlyMatches( checkType, queryType ) ) {
      throw new RuntimeException( "Check-Type does not match Query-Type" );
    }
    Method[] queryMethods = getQueryMethods( queryType );
    Map<Method, Collection<Method>> possibleMatches = collectPossibleMatches( queryMethods, checkType.getMethods() );
    return possibleMatches;
  }

  // Hier müssen alle Methoden der Klasse Object herausgefiltert werden, weil:
  // 1. ohnehin alle Objekte mit diesen Methoden umgehen könne
  // 2. ein Interface die dazu passenden Methoden-Signaturen nicht ausweist

  // Einsicht: Das Matching mit Klassen oder Enums als Query-Typ ist etwas kompliziert. Ich beschränke mich erst einmal
  // nur auf Interfaces als Query-Typ. Das ist auch hinsichtlich meines Anwendungsfalls eher relevant.
  private Method[] getQueryMethods( Class<?> queryType ) {
    Method[] queryMethods = queryType.getMethods();
    return queryMethods;
  }
  // Method[] irrelevantMethods = getIrrelevantMethods( Object.class.getMethods() );
  // int relevantMethodsCount = queryType.length - irrelevantMethods.length;
  // if ( relevantMethodsCount < 0 ) {
  // return queryType;
  // }
  // Method[] relevantMethods = new Method[relevantMethodsCount];
  // int currentIndex = 0;
  // for ( Method m : queryType ) {
  // if ( !containsEqualMethod( irrelevantMethods, m ) ) {
  // relevantMethods[currentIndex] = m;
  // currentIndex++;
  // }
  // }
  // return relevantMethods;
  // }
  //
  // private boolean containsEqualMethod( Method[] irrelevantMethods, Method m ) {
  // for ( Method irrelevantMethod : irrelevantMethods ) {
  // String name = irrelevantMethod.getName();
  // if ( !name.equals( m.getName() ) ) {
  // continue;
  // }
  // Class<?> returnType = irrelevantMethod.getReturnType();
  // if ( returnType.equals( m.getReturnType() ) ) {
  // continue;
  // }
  // Class<?>[] parameterTypes = irrelevantMethod.getParameterTypes();
  // for ( int i = 0; i < parameterTypes.length; i++ ) {
  // if ( !parameterTypes[i].equals( m.getParameterTypes()[i] ) ) {
  // continue;
  // }
  // }
  // return true;
  // }
  // return false;
  // }

  private Map<Method, Collection<Method>> collectPossibleMatches( Method[] queryMethods, Method[] methods ) {
    Map<Method, Collection<Method>> matches = new HashMap<>();
    for ( Method queryMethod : queryMethods ) {
      Collection<Method> queryMethodMatches = new ArrayList<>();
      for ( Method checkMethod : methods ) {
        if ( methodMatcher.matches( checkMethod, queryMethod ) ) {
          queryMethodMatches.add( checkMethod );
        }
      }
      matches.put( queryMethod, queryMethodMatches );
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
