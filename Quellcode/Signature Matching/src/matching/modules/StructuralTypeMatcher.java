package matching.modules;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import matching.MatcherRate;
import matching.methods.MatchingMethod;
import matching.methods.MethodMatcher;
import matching.methods.MethodMatchingInfo;
import matching.methods.ParamPermMethodMatcher;
import util.Logger;

public class StructuralTypeMatcher implements PartlyTypeMatcher {

  private static final double MATCHER_BASE_RATING = 400d;

  private final MethodMatcher methodMatcher;

  public StructuralTypeMatcher( final Supplier<TypeMatcher> innerMethodMatcherSupplier ) {
    this.methodMatcher = new ParamPermMethodMatcher( innerMethodMatcherSupplier );
  }

  @Override
  public boolean matchesType( Class<?> checkType, Class<?> queryType ) {
    Logger.info( String.format( "%s MATCH? %s", checkType.getSimpleName(), queryType.getSimpleName() ) );
    Method[] queryMethods = getQueryMethods( queryType );
    Map<Method, Collection<Method>> possibleMatches = convertMethod2MethodCollection(
        collectPossibleMatches( queryMethods, checkType.getMethods() ) );
    printPossibleMatches( possibleMatches );
    return possibleMatches.values().stream().noneMatch( Collection::isEmpty );
  }

  @Override
  public boolean matchesTypePartly( Class<?> checkType, Class<?> queryType ) {
    Logger.info( String.format( "%s MATCH? %s", checkType.getSimpleName(), queryType.getSimpleName() ) );
    Method[] queryMethods = getQueryMethods( queryType );
    Map<Method, Collection<Method>> possibleMatches = convertMethod2MethodCollection(
        collectPossibleMatches( queryMethods, checkType.getMethods() ) );
    printPossibleMatches( possibleMatches );
    return possibleMatches.values().stream().anyMatch( l -> !l.isEmpty() );
  }

  @Override
  public Collection<ModuleMatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType ) {
    ModuleMatchingInfoFactory factory = new ModuleMatchingInfoFactory( checkType, queryType );
    if ( queryType.equals( Object.class ) ) {
      // Dieser Spezialfall fuehrt ohne diese Sonderregelung in einen Stackoverflow, da Object als Typ immer wieder
      // auftaucht. Es ist also eine Abbruchbedingung.
      Set<ModuleMatchingInfo> singleResult = new HashSet<>();
      singleResult.add( factory.create() );
      return singleResult;
    }
    Method[] queryMethods = getQueryMethods( queryType );
    Logger.infoF( "QueryMethods: %s",
        Stream.of( queryMethods ).map( m -> m.getName() ).collect( Collectors.joining( ", " ) ) );
    Map<Method, Collection<Method>> possibleMatches = convertMethod2MethodCollection(
        collectPossibleMatches( queryMethods, checkType.getMethods() ) );
    Map<Method, Collection<MethodMatchingInfo>> possibleMethodMatches = collectMethodMatchingInfos( queryMethods,
        possibleMatches );
    possibleMatches.entrySet()
        .forEach( e -> Logger.infoF( "MethodMatchingInfos collected - Method: %s | Info count: %d",
            e.getKey().getName(), e.getValue().size() ) );

    return factory.createFromMethodMatchingInfos( possibleMethodMatches );
  }

  private Map<Method, Collection<MethodMatchingInfo>> collectMethodMatchingInfos( Method[] queryMethods,
      Map<Method, Collection<Method>> possibleMatches ) {
    Map<Method, Collection<MethodMatchingInfo>> matches = new HashMap<>();
    for ( Method queryMethod : queryMethods ) {
      Collection<MethodMatchingInfo> matchingInfosOfQueryMethod = new ArrayList<>();
      if ( !possibleMatches.containsKey( queryMethod ) ) {
        continue;
      }
      for ( Method checkMethod : possibleMatches.get( queryMethod ) ) {
        Collection<MethodMatchingInfo> matchingInfos = methodMatcher.calculateMatchingInfos( checkMethod, queryMethod );
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

  private Map<Method, Collection<MatchingMethod>> collectPossibleMatches( Method[] queryMethods,
      Method[] checkMethods ) {
    Map<Method, Collection<MatchingMethod>> matches = new HashMap<>();
    for ( Method queryMethod : queryMethods ) {
      Collection<MatchingMethod> queryMethodMatches = new ArrayList<>();
      for ( Method checkMethod : checkMethods ) {
        MatcherRate rate = methodMatcher.matchesWithRating( checkMethod, queryMethod );
        if ( rate != null ) {
          queryMethodMatches.add( new MatchingMethod( checkMethod, rate ) );
        }
      }
      if ( !queryMethodMatches.isEmpty() ) {
        matches.put( queryMethod, queryMethodMatches );
      }
    }
    return matches;
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

  // Hier muessen alle Methoden der Klasse Object herausgefiltert werden, weil:
  // 1. ohnehin alle Objekte mit diesen Methoden umgehen koenne
  // 2. ein Interface die dazu passenden Methoden-Signaturen nicht ausweist

  // Einsicht: Das Matching mit Klassen oder Enums als Query-Typ ist etwas kompliziert. Ich beschraenke mich erst einmal
  // nur auf Interfaces als Query-Typ. Das ist auch hinsichtlich meines Anwendungsfalls eher relevant.

  // Weiteres Problem: primitive Typen haben keine Methoden!!!

  // Weiteres Problem: was ist mit package-Sichtbarkeit?
  private Method[] getQueryMethods( Class<?> queryType ) {
    Method[] queryMethods = queryType.getMethods();
    return queryMethods;
  }

  @Override
  public PartlyTypeMatchingInfo calculatePartlyTypeMatchingInfos( Class<?> checkType, Class<?> queryType ) {
    PartlyTypeMatchingInfoFactory factory = new PartlyTypeMatchingInfoFactory( checkType );
    if ( queryType.equals( Object.class ) ) {
      // Dieser Spezialfall fuehrt ohne diese Sonderregelung in einen Stackoverflow, da Object als Typ immer wieder
      // auftaucht. Es ist also eine Abbruchbedingung.
      return factory.create();
    }

    Method[] queryMethods = getQueryMethods( queryType );
    Logger.infoF( "QueryMethods: %s",
        Stream.of( queryMethods ).map( m -> m.getName() ).collect( Collectors.joining( ", " ) ) );

    // gleicht nur die public-Methods ab
    Method[] potentialMethods = checkType.getMethods();
    Map<Method, Collection<MatchingMethod>> possibleMatches = collectPossibleMatches( queryMethods,
        potentialMethods );
    Map<Method, MatchingSupplier> matchingInfoSupplier = new HashMap<>();
    for ( Entry<Method, Collection<MatchingMethod>> qM2tM : possibleMatches.entrySet() ) {
      MatchingSupplier supplier = getSupplierOfMultipleMatchingMethods( qM2tM.getKey(),
          qM2tM.getValue() );
      matchingInfoSupplier.put( qM2tM.getKey(), supplier );
    }
    matchingInfoSupplier.entrySet()
        .forEach( e -> Logger.infoF( "Supplier for MethodMatchingInfos collected - Method: %s",
            e.getKey().getName() ) );
    return factory.create( Arrays.asList( queryMethods ), matchingInfoSupplier, potentialMethods.length );
  }

  private MatchingSupplier getSupplierOfMultipleMatchingMethods( Method queryMethod,
      Collection<MatchingMethod> matchingMethods ) {
    Supplier<Collection<MethodMatchingInfo>> supplier = () -> {
      Collection<MethodMatchingInfo> metMIs = new ArrayList<>();
      for ( MatchingMethod matching : matchingMethods ) {
        metMIs.addAll( methodMatcher.calculateMatchingInfos( matching.getMethod(), queryMethod ) );
      }
      return metMIs;
    };
    return new MatchingSupplier( supplier,
        matchingMethods.stream().map( MatchingMethod::getRate ).min( MatcherRate::compare ).get() );
  }

  private static Map<Method, Collection<Method>> convertMethod2MethodCollection(
      Map<Method, Collection<MatchingMethod>> collectPossibleMatches ) {
    Map<Method, Collection<Method>> method2MethodCollection = new HashMap<>();
    for ( Entry<Method, Collection<MatchingMethod>> entry : collectPossibleMatches.entrySet() ) {
      method2MethodCollection.put( entry.getKey(),
          entry.getValue().stream().map( MatchingMethod::getMethod ).collect( Collectors.toList() ) );
    }

    return method2MethodCollection;
  }

  @Override
  public MatcherRate matchesWithRating( Class<?> checkType, Class<?> queryType ) {
	  if(matchesType( checkType, queryType )) {
		  MatcherRate rate = new MatcherRate();
		  rate.add(this.getClass().getSimpleName(), MATCHER_BASE_RATING);
		  return rate;
	  }
	  return null;
  }

}
