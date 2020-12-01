package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.MethodMatcherHeuristic;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
import glue.SignatureMatchingTypeConverter;
import matching.modules.ModuleMatchingInfo;
import matching.modules.TypeMatcher;
import tester.ComponentTester;
import tester.TestResult;
import tester.TestResult.Result;

public class DesiredComponentFinder {

  private static final TypeMatcher[] TYPE_MATCHERS = MethodMatcherHeuristic.getTypeMatcher();

  private final Class<?>[] registeredComponentInterfaces;

  private final Function<Class<?>, Optional<?>> optComponentGetter;

  public DesiredComponentFinder( Class<?>[] registeredComponentInterfaces,
      Function<Class<?>, Optional<?>> optComponentGetter ) {
    this.registeredComponentInterfaces = Stream.of( registeredComponentInterfaces ).distinct()
        .collect( Collectors.toList() )
        .toArray( new Class[] {} );
    this.optComponentGetter = optComponentGetter;

  }

  private Optional<?> getComponent( Class<?> componentClass ) {
    return optComponentGetter.apply( componentClass );
  }

  public <DesiredInterface> DesiredInterface getDesiredComponent(
      Class<DesiredInterface> desiredInterface ) {

    // TODO Matching-Method Heuristik: Zwischen den Matching-Methoden gibt es eine Rangfolge:
    for ( int i = 0; i < TYPE_MATCHERS.length; i++ ) {
      Optional<DesiredInterface> optDesiredBean = findDesiredComponentByMatcher( desiredInterface,
          TYPE_MATCHERS[i] );
      if ( optDesiredBean.isPresent() ) {
        Logger.info( "component found" );
        return optDesiredBean.get();
      }
      Logger.info( "component not found" );
    }
    return null;

    // Das ist dasselbe wie findDesiredComponentByMatcher( desiredInterface, new CombinedMethodMatcher());
    // Collection<Class<?>> matchingBeanInterfaces = findComponentsBySignatureMatching( desiredInterface );
    // Logger.info( "Matching Bean-Interfaces of " + desiredInterface.getName() );
    // // matchingBeanInterfaces.stream().map( Class::getName ).forEach( System.out::println );
    // Logger.info( String.format( "count: %d", matchingBeanInterfaces.size() ) );

    // Hier können weitere Filter und Heuristiken eingebaut werden

    // return getComposedComponent( desiredInterface, matchingBeanInterfaces );
  }

  private <DesiredInterface> Optional<DesiredInterface> findDesiredComponentByMatcher(
      Class<DesiredInterface> desiredInterface, TypeMatcher typeMatcher ) {
    Logger.infoF( "start search with matcher: %s", typeMatcher.getClass().getSimpleName() );
    Collection<Class<?>> matchingBeanInterfaces = findMatchingComponentInterfaces( desiredInterface, typeMatcher );
    Optional<DesiredInterface> result = Optional
        .ofNullable( getComposedComponent( desiredInterface, matchingBeanInterfaces, typeMatcher ) );
    Logger.infoF( "finish search with matcher: %s", typeMatcher.getClass().getSimpleName() );
    return result;
  }

  private <DesiredInterface> Collection<Class<?>> findMatchingComponentInterfaces(
      Class<DesiredInterface> desiredInterface,
      TypeMatcher typeMatcher ) {
    Collection<Class<?>> matchedBeans = new ArrayList<>();
    for ( Class<?> beanInterface : getRegisteredComponentInterfaces() ) {
      // Dieser Code ist nur für das Debugging-Analyse notwendig
      // if ( Arrays.asList( beanInterfaces ).contains( ElerFTStammdatenAuskunftService.class ) ) {
      // Logger.info( "BEAN OF INTEREST" );
      // }
      boolean matchesFull = typeMatcher.matchesType( beanInterface, desiredInterface );
      if ( !matchesFull ) {
        // boolean partlyMatches = moduleMatcher.partlyMatches( beanInterfaces );
        // if ( !partlyMatches ) {
        continue;
        // }
      }
      matchedBeans.add( beanInterface );
    }
    return matchedBeans;
  }

  private <DesiredInterface> DesiredInterface getComposedComponent(
      Class<DesiredInterface> desiredInterface, Collection<Class<?>> matchingBeanInterfaces,
      TypeMatcher typeMatcher ) {
    Logger.info( "create ComponentInfos" );
    Set<ComponentInfos> rankedComponentInfos = getSortedModuleMatchingInfos( desiredInterface,
        matchingBeanInterfaces, typeMatcher );
    Logger.info( String.format( "ranking of relevant components" ) );
    rankedComponentInfos.stream().forEach(
        c -> Logger.info( String.format( "rank: %d component: %s", c.getRank(), c.getComponentClass().getName() ) ) );
    List<ComponentInfos> fullMatchedComponents = rankedComponentInfos.stream()
        .filter( c -> c.getRank() >= 100 // !!! Achtung: Das Ranking muss angepasst werden !!!
        ).collect( Collectors.toList() );
    if ( !fullMatchedComponents.isEmpty() ) {
      DesiredInterface component = getFullMatchedTestedComponent( fullMatchedComponents, desiredInterface );
      if ( component != null ) {
        return component;
      }
    }

    return null;
  }

  private <DesiredInterface> DesiredInterface getFullMatchedTestedComponent(
      List<ComponentInfos> fullMatchedComponents, Class<DesiredInterface> desiredInterface ) {
    SignatureMatchingTypeConverter<DesiredInterface> converter = new SignatureMatchingTypeConverter<>(
        desiredInterface );
    ComponentTester<DesiredInterface> componentTester = new ComponentTester<>( desiredInterface );

    for ( ComponentInfos componentInfo : fullMatchedComponents ) {
      Class<?> componentClass = componentInfo.getComponentClass();
      Optional<?> optComponent = getComponent( componentClass );

      if ( !optComponent.isPresent() ) {
        continue;
      }
      Object component = optComponent.get();
      // TODO hier wäre eine Heuristik angebracht, welche die MatchingInfos der ComponentInfo in eine Reihenfolge
      // bringt.
      for ( ModuleMatchingInfo matchingInfo : componentInfo.getMatchingInfos() ) {
        Logger.infoF( "test component: %s", component.getClass().getName() );
        DesiredInterface convertedComponent = converter.convert( component, matchingInfo );
        TestResult testResult = componentTester.testComponent( convertedComponent );
        Logger.infoF( "passed tests: %d/%d", testResult.getPassedTests(), testResult.getTestCount() );
        if ( testResult.getResult() == Result.PASSED ) {
          return convertedComponent;
        }
      }
    }
    return null;

  }

  private <DesiredInterface> Set<ComponentInfos> getSortedModuleMatchingInfos(
      Class<DesiredInterface> desiredInterface, Collection<Class<?>> matchingBeanInterfaces,
      TypeMatcher typeMatcher ) {
    List<ComponentInfos> componentInfoSet = new ArrayList<>();
    for ( Class<?> matchingBeanInterface : matchingBeanInterfaces ) {
      // Logger.info( String.format( "collect ModuleMatchingInfo: %s", matchingBeanInterface.getName() ) );
      Collection<ModuleMatchingInfo> matchingInfos = typeMatcher
          .calculateTypeMatchingInfos( matchingBeanInterface, desiredInterface );
      ComponentInfos componentInfos = new ComponentInfos( matchingBeanInterface );
      componentInfos.setModuleMatchingInfos( matchingInfos );
      componentInfos.addContext( matchingBeanInterface.getName() );
      componentInfoSet.add( componentInfos );
    }
    Logger.info( String.format( "ComponentInfos created: %d", componentInfoSet.size() ) );
    Logger.info( "sort ComponentInfos" );
    Collections.sort( componentInfoSet, ( c1, c2 ) -> Integer.compare( c1.getRank(), c2.getRank() ) );
    return new HashSet<>( componentInfoSet );
  }

  // private <DesiredInterface> Collection<Class<?>> findComponentsBySignatureMatching(
  // Class<DesiredInterface> desiredInterface ) {
  // ModuleMatcher<DesiredInterface> moduleMatcher = new ModuleMatcher<>( desiredInterface );
  // Collection<Class<?>> matchedBeans = new ArrayList<>();
  // for ( Class<?> beanInterface : getRegisteredComponentInterfaces() ) {
  // // Dieser Code ist nur für das Debugging-Analyse notwendig
  // // if ( beanInterfaces.equals( ElerFTStammdatenAuskunftService.class ) ) {
  // // Logger.info( "BEAN OF INTEREST" );
  // // }
  // boolean matchesFull = moduleMatcher.matches( beanInterface );
  // if ( !matchesFull ) {
  // // boolean partlyMatches = moduleMatcher.partlyMatches( beanInterfaces );
  // // if ( !partlyMatches ) {
  // continue;
  // // }
  // }
  // matchedBeans.add( beanInterface );
  // }
  // return matchedBeans;
  // }

  private Class<?>[] getRegisteredComponentInterfaces() {
    return this.registeredComponentInterfaces;
  }

}
