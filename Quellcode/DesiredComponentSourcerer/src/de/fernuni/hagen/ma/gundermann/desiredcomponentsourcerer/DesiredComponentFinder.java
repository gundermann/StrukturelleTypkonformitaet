package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.TypeMatcherHeuristic;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
import glue.CombinationTypeConverter;
import glue.SingleTypeConverter;
import matching.modules.ModuleMatchingInfo;
import matching.modules.PartlyTypeMatcher;
import matching.modules.PartlyTypeMatchingInfo;
import matching.modules.TypeMatcher;
import tester.ComponentTester;
import tester.TestResult;
import tester.TestResult.Result;

public class DesiredComponentFinder {

  private static final TypeMatcher[] FULL_TYPE_MATCHERS = TypeMatcherHeuristic.getFullTypeMatcher();

  private static final PartlyTypeMatcher[] PARLTY_TYPE_MATCHERS = TypeMatcherHeuristic.getPartlyTypeMatcher();

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

    Logger.info( "search component by full match" );
    // TODO Matching-Method Heuristik: Zwischen den Matching-Methoden gibt es eine Rangfolge:
    for ( int i = 0; i < FULL_TYPE_MATCHERS.length; i++ ) {
      Optional<DesiredInterface> optDesiredBean = findDesiredComponentByFullMatcher( desiredInterface,
          FULL_TYPE_MATCHERS[i] );
      if ( optDesiredBean.isPresent() ) {
        Logger.info( "component found" );
        return optDesiredBean.get();
      }
      Logger.info( "component not found" );
    }

    Logger.info( "search component by partly match" );
    for ( int i = 0; i < PARLTY_TYPE_MATCHERS.length; i++ ) {
      Optional<DesiredInterface> optDesiredBean = findDesiredComponentByPartlyMatcher( desiredInterface,
          PARLTY_TYPE_MATCHERS[i] );
      if ( optDesiredBean.isPresent() ) {
        Logger.info( "component found" );
        return optDesiredBean.get();
      }
      Logger.info( "component not found" );
    }
    return null;
  }

  private <DesiredInterface> Optional<DesiredInterface> findDesiredComponentByPartlyMatcher(
      Class<DesiredInterface> desiredInterface,
      PartlyTypeMatcher typeMatcher ) {
    Logger.infoF( "start search with matcher: %s", typeMatcher.getClass().getSimpleName() );

    Map<Class<?>, PartlyTypeMatchingInfo> compnentInterface2PartlyMatchingInfos = findPartlyMatchingComponentInterfaces(
        desiredInterface, typeMatcher );

    Optional<DesiredInterface> result = Optional
        .ofNullable( getCombinedMatchingComponent( desiredInterface, compnentInterface2PartlyMatchingInfos ) );
    Logger.infoF( "finish search with matcher: %s", typeMatcher.getClass().getSimpleName() );
    return result;
  }

  private <DesiredInterface> Map<Class<?>, PartlyTypeMatchingInfo> findPartlyMatchingComponentInterfaces(
      Class<DesiredInterface> desiredInterface, PartlyTypeMatcher typeMatcher ) {
    Map<Class<?>, PartlyTypeMatchingInfo> matchedBeans = new HashMap<>();
    for ( Class<?> beanInterface : getRegisteredComponentInterfaces() ) {
      boolean matchesPartly = typeMatcher.matchesTypePartly( beanInterface, desiredInterface );
      if ( !matchesPartly ) {
        continue;
      }
      matchedBeans.put( beanInterface,
          typeMatcher.calculatePartlyTypeMatchingInfos( beanInterface, desiredInterface ) );
    }
    return matchedBeans;
  }

  private <DesiredInterface> Optional<DesiredInterface> findDesiredComponentByFullMatcher(
      Class<DesiredInterface> desiredInterface, TypeMatcher typeMatcher ) {
    Logger.infoF( "start search with matcher: %s", typeMatcher.getClass().getSimpleName() );
    Collection<Class<?>> matchingBeanInterfaces = findMatchingComponentInterfaces( desiredInterface, typeMatcher );
    Optional<DesiredInterface> result = Optional
        .ofNullable( getFullMatchingComponent( desiredInterface, matchingBeanInterfaces, typeMatcher ) );
    Logger.infoF( "finish search with matcher: %s", typeMatcher.getClass().getSimpleName() );
    return result;
  }

  private <DesiredInterface> Collection<Class<?>> findMatchingComponentInterfaces(
      Class<DesiredInterface> desiredInterface, TypeMatcher typeMatcher ) {
    Collection<Class<?>> matchedBeans = new ArrayList<>();
    for ( Class<?> beanInterface : getRegisteredComponentInterfaces() ) {
      boolean matchesFull = typeMatcher.matchesType( beanInterface, desiredInterface );
      if ( !matchesFull ) {
        continue;
      }
      matchedBeans.add( beanInterface );
    }
    return matchedBeans;
  }

  private <DesiredInterface> DesiredInterface getCombinedMatchingComponent(
      Class<DesiredInterface> desiredInterface,
      Map<Class<?>, PartlyTypeMatchingInfo> compnentInterface2PartlyMatchingInfos ) {
    Logger.info( "create ComponentInfos" );
    BestMatchingComponentCombinationFinder combinationFinder = new BestMatchingComponentCombinationFinder(
        compnentInterface2PartlyMatchingInfos );

    while ( !combinationFinder.hasNextCombination() ) {
      CombinationInfo combinationInfos = combinationFinder.getNextCombination();
      DesiredInterface component = getPartlyMatchedTestedComponent( combinationInfos, desiredInterface );
      if ( component != null ) {
        return component;
      }
    }

    return null;

  }

  private <DesiredInterface> DesiredInterface getFullMatchingComponent(
      Class<DesiredInterface> desiredInterface, Collection<Class<?>> matchingBeanInterfaces,
      TypeMatcher typeMatcher ) {
    Logger.info( "create ComponentInfos" );
    Collection<ComponentInfos> rankedComponentInfos = getSortedModuleMatchingInfos( desiredInterface,
        matchingBeanInterfaces, typeMatcher );
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

  private <DesiredInterface> DesiredInterface getPartlyMatchedTestedComponent( CombinationInfo combinationInfos,
      Class<DesiredInterface> desiredInterface ) {
    CombinationTypeConverter<DesiredInterface> converter = new CombinationTypeConverter<>(
        desiredInterface );
    ComponentTester<DesiredInterface> componentTester = new ComponentTester<>( desiredInterface );

    Map<Object, ModuleMatchingInfo> components2MatchingInfo = new HashMap<>();
    for ( Class<?> componentClass : combinationInfos.getComponentClasses() ) {
      Optional<?> optComponent = getComponent( componentClass );
      if ( !optComponent.isPresent() ) {
        return null;
      }
      components2MatchingInfo.put( optComponent.get(), combinationInfos.getModuleMatchingInfo( componentClass ) );
    }

    Logger.infoF( "test component: %s",
        combinationInfos.getComponentClasses().stream().map( Class::getName ).collect( Collectors.joining( "," ) ) );
    DesiredInterface convertedComponent = converter.convert( components2MatchingInfo );
    TestResult testResult = componentTester.testComponent( convertedComponent );
    Logger.infoF( "passed tests: %d/%d", testResult.getPassedTests(), testResult.getTestCount() );
    if ( testResult.getResult() == Result.PASSED ) {
      return convertedComponent;
    }
    return null;
  }

  private <DesiredInterface> DesiredInterface getFullMatchedTestedComponent(
      List<ComponentInfos> fullMatchedComponents, Class<DesiredInterface> desiredInterface ) {
    SingleTypeConverter<DesiredInterface> converter = new SingleTypeConverter<>(
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

  private <DesiredInterface> Collection<ComponentInfos> getSortedModuleMatchingInfos(
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
    return componentInfoSet;
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
