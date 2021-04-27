package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.BestMatchingComponentCombinationFinder;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.CombinationInfo;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.DefaultTypeMatcherHeuristic;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.HeuristicSetting;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
import glue.TypeConverter;
import matching.methods.MethodMatchingInfo;
import matching.modules.PartlyTypeMatcher;
import matching.modules.PartlyTypeMatchingInfo;
import matching.modules.TypeMatcher;
import tester.ComponentTester;
import tester.TestResult;
import tester.TestType;

public class DesiredComponentFinder {

  private PartlyTypeMatcher[] partlyTypeMatcher = DefaultTypeMatcherHeuristic.getPartlyTypeMatcher();

  private final Class<?>[] registeredComponentInterfaces;

  private final Function<Class<?>, Optional<?>> optComponentGetter;

  private int testedComponentVariations = 0;

  public DesiredComponentFinder( Class<?>[] registeredComponentInterfaces,
      Function<Class<?>, Optional<?>> optComponentGetter ) {
    this.registeredComponentInterfaces = Stream.of( registeredComponentInterfaces ).distinct()
        .collect( Collectors.toList() )
        .toArray( new Class[] {} );
    this.optComponentGetter = optComponentGetter;

  }

  public void setFullTypeMatcher( TypeMatcher[] fullTypeMatcher ) {
    this.partlyTypeMatcher = DefaultTypeMatcherHeuristic.createTypeMatcher( fullTypeMatcher );
  }

  private Optional<?> getComponent( Class<?> componentClass ) {
    return optComponentGetter.apply( componentClass );
  }

  public <DesiredInterface> DesiredInterface getDesiredComponent(
      Class<DesiredInterface> desiredInterface ) {
    Logger.info( "search component by partly match: " + desiredInterface.getName() );
    for ( int i = 0; i < partlyTypeMatcher.length; i++ ) {
      Optional<DesiredInterface> optDesiredBean = findDesiredComponentByPartlyMatcher( desiredInterface,
          partlyTypeMatcher[i] );
      if ( optDesiredBean.isPresent() ) {
        Logger.info( "component found" );
        Logger.infoF( "Tested Components variations: %d", testedComponentVariations );
        return optDesiredBean.get();
      }
      Logger.info( "component not found" );
    }

    Logger.infoF( "Tested Components variations: %d", testedComponentVariations );
    return null;
  }

  private <DesiredInterface> Optional<DesiredInterface> findDesiredComponentByPartlyMatcher(
      Class<DesiredInterface> desiredInterface,
      PartlyTypeMatcher typeMatcher ) {
    Logger.infoF( "start search with matcher: %s", typeMatcher.getClass().getSimpleName() );

    Map<Class<?>, PartlyTypeMatchingInfo> componentInterface2PartlyMatchingInfos = findPartlyMatchingComponentInterfaces(
        desiredInterface, typeMatcher );

    // INFO OUTPUT
    componentInterface2PartlyMatchingInfos.values().forEach( i -> {
      Logger.toFile( "%f;%s;%f;%s;", i.getQualitativeMatchRating().getMatcherRating(),
          i.getQualitativeMatchRating().toString(), i.getQuantitaiveMatchRating(),
          i.getCheckType().getSimpleName() );
    } );

    Optional<DesiredInterface> result = Optional
        .ofNullable( getCombinedMatchingComponent( desiredInterface, componentInterface2PartlyMatchingInfos ) );
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

  private <DesiredInterface> DesiredInterface getCombinedMatchingComponent(
      Class<DesiredInterface> desiredInterface,
      Map<Class<?>, PartlyTypeMatchingInfo> componentInterface2PartlyMatchingInfos ) {
    Logger.info( "create ComponentInfos" );
    BestMatchingComponentCombinationFinder combinationFinder = new BestMatchingComponentCombinationFinder(
        componentInterface2PartlyMatchingInfos );

    while ( combinationFinder.hasNextCombination() ) {
      CombinationInfo combinationInfos = combinationFinder.getNextCombination();
      try {
        TestedComponent<DesiredInterface> testedComponent = getPartlyMatchedTestedComponent( combinationInfos,
            desiredInterface );
        if ( testedComponent != null ) {
          if ( testedComponent.allTestsPassed() ) {
            return testedComponent.getComponent();
          }
          if ( HeuristicSetting.COMBINE_COMPOMENTS_WITH_PASSED_TESTS_FIRST && testedComponent.anyTestPassed() ) {
            // H: combinate passed tests components first
            combinationFinder.optimizeForCurrentCombination();
          }
          if ( testedComponent.isOutsortedMatchingInfoFound() ) {
            // H: blacklist by pivot test calls
            // H: blacklist failed single methods tested
            combinationFinder.optimizeMatchingInfoBlacklist( testedComponent.getOutsortedMatchingInfos() );
          }
        }
      }
      catch ( NoComponentImplementationFoundException e ) {
        if ( HeuristicSetting.BLACKLIST_NO_IMPLEMENTATION_AVAILABLE ) {
          // H: blacklist if no implementation available
          combinationFinder.optimizeCheckTypeBlacklist( e.getComponentInterface() );
        }
      }
    }
    return null;
  }

  private <DesiredInterface> TestedComponent<DesiredInterface> getPartlyMatchedTestedComponent(
      CombinationInfo combinationInfos,
      Class<DesiredInterface> desiredInterface ) throws NoComponentImplementationFoundException {
    Logger.infoF( "find components for combination: %s",
        combinationInfos.getComponentClasses().stream().map( Class::toString ).collect( Collectors.joining( " + " ) ) );

    testedComponentVariations++;
    TypeConverter<DesiredInterface> converter = new TypeConverter<>(
        desiredInterface );
    ComponentTester<DesiredInterface> componentTester = new ComponentTester<>( desiredInterface );

    Map<Object, Collection<MethodMatchingInfo>> components2MatchingInfo = new HashMap<>();

    for ( Class<?> componentClass : combinationInfos.getComponentClasses() ) {
      Optional<?> optComponent = getComponent( componentClass );
      if ( !optComponent.isPresent() ) {
        throw new NoComponentImplementationFoundException( componentClass );
      }
      components2MatchingInfo.put( optComponent.get(), combinationInfos.getModuleMatchingInfo( componentClass ) );
    }

    Logger.infoF( "test component: %s",
        combinationInfos.getComponentClasses().stream().map( Class::getName ).collect( Collectors.joining( "," ) ) );

    DesiredInterface convertedComponent = converter.convert( components2MatchingInfo );
    TestResult testResult = componentTester.testComponent( convertedComponent );
    logTestResult( testResult );
    TestedComponent<DesiredInterface> testedComponent = new TestedComponent<>( convertedComponent, testResult );

    // H: blacklist by pivot test calls
    if ( HeuristicSetting.BLACKLIST_PIVOT_METHOD_CALL ) {
      Collection<Method> pivotMethodCalls = testResult.getPivotMethodCalls();
      if ( pivotMethodCalls != null && !pivotMethodCalls.isEmpty() ) {
        Logger.infoF( "outsource by pivot method: %s",
            pivotMethodCalls.stream().map( Method::getName ).collect( Collectors.joining( ", " ) ) );
        components2MatchingInfo.values().stream()
            .flatMap( Collection::stream )
            .filter( mmi -> pivotMethodCalls.contains( mmi.getSource() ) )
            .forEach( testedComponent::addOutsortedMatchingInfo );
      }
    }

    // H: blacklist failed single methods tested
    if ( HeuristicSetting.BLACKLIST_SINGLE_METHOD_TEST_FAILED ) {
      Collection<String> failedSingleMethods = testResult.getFailedSingleMethods();
      if ( failedSingleMethods != null && !failedSingleMethods.isEmpty() ) {
        Logger.infoF( "outsource by failed single method: %s",
            failedSingleMethods.stream().collect( Collectors.joining( ", " ) ) );
        components2MatchingInfo.values().stream()
            .flatMap( Collection::stream )
            .filter( mmi -> failedSingleMethods.contains( mmi.getSource().getName() ) )
            .forEach( testedComponent::addOutsortedMatchingInfo );
      }
    }
    return testedComponent;
  }

  private void logTestResult( TestResult testResult ) {
    Logger.infoF( "used test type: %s", testResult.getTestType().name() );
    Logger.infoF( "passed tests: %d/%d", testResult.getPassedTests(), testResult.getTestCount() );
    switch ( testResult.getResult() ) {
      case CANCELED:
        Logger.infoF( "test canceled: %s\n%s", testResult.getException().getMessage(),
            Stream.of( testResult.getException().getStackTrace() ).map( StackTraceElement::toString )
                .map( s -> "\t\t\t" + s )
                .collect( Collectors.joining( "\n" ) ) );
        break;
      case FAILED:
        if ( testResult.getTestType() == TestType.COMMON ) {
          Logger.infoF( "test failed: %s => %s", testResult.getPivotTestName(),
              testResult.getException().getMessage() );
        }
        else if ( testResult.getTestType() == TestType.SINGLE_METHOD ) {
          Logger.infoF( "single test methods failed by assertion: %s",
              testResult.getFailedSingleMethods().stream().collect( Collectors.joining( ", " ) ) );
          Logger.infoF( "failed pivot methods: %s",
              testResult.getPivotMethodCalls().stream().map( mmi -> mmi.getName() )
                  .collect( Collectors.joining( ", " ) ) );
        }
        break;
      default:
        break;
    }
  }

  private Class<?>[] getRegisteredComponentInterfaces() {
    return this.registeredComponentInterfaces;
  }

}
