package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.DefaultTypeMatcherHeuristic;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
import glue.CombinationTypeConverter;
import matching.methods.MethodMatchingInfo;
import matching.modules.PartlyTypeMatcher;
import matching.modules.PartlyTypeMatchingInfo;
import matching.modules.TypeMatcher;
import tester.ComponentTester;
import tester.TestResult;
import tester.TestResult.Result;

public class DesiredComponentFinder {

  private PartlyTypeMatcher[] partlyTypeMatcher = DefaultTypeMatcherHeuristic.getPartlyTypeMatcher();

  private final Class<?>[] registeredComponentInterfaces;

  private final Function<Class<?>, Optional<?>> optComponentGetter;

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

    Map<Class<?>, PartlyTypeMatchingInfo> componentInterface2PartlyMatchingInfos = findPartlyMatchingComponentInterfaces(
        desiredInterface, typeMatcher );

    // FIXME INFO OUTPUT
    componentInterface2PartlyMatchingInfos.values().forEach( i -> {
      Logger.toFile( "%f;%s;\n", i.getQualitativeMatchRating(),
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
      Map<Class<?>, PartlyTypeMatchingInfo> compnentInterface2PartlyMatchingInfos ) {
    Logger.info( "create ComponentInfos" );
    BestMatchingComponentCombinationFinder combinationFinder = new BestMatchingComponentCombinationFinder(
        compnentInterface2PartlyMatchingInfos );

    while ( combinationFinder.hasNextCombination() ) {
      CombinationInfo combinationInfos = combinationFinder.getNextCombination();
      DesiredInterface component = getPartlyMatchedTestedComponent( combinationInfos, desiredInterface );
      if ( component != null ) {
        return component;
      }
    }
    return null;
  }

  private <DesiredInterface> DesiredInterface getPartlyMatchedTestedComponent( CombinationInfo combinationInfos,
      Class<DesiredInterface> desiredInterface ) {
    Logger.infoF( "find components for combination: %s",
        combinationInfos.getComponentClasses().stream().map( Class::toString ).collect( Collectors.joining( " + " ) ) );

    if ( combinationInfos.getComponentClasses().stream().map( Class::getSimpleName )
        .anyMatch( n -> n.equals( "StammdatenAuskunftService" ) )
        && combinationInfos.getComponentClasses().stream().map( Class::getSimpleName )
            .anyMatch( n -> n.equals( "ElerFTStammdatenAuskunftService" ) ) ) {
      System.out.println( "hwg" );
    }

    CombinationTypeConverter<DesiredInterface> converter = new CombinationTypeConverter<>(
        desiredInterface );
    ComponentTester<DesiredInterface> componentTester = new ComponentTester<>( desiredInterface );

    Map<Object, Collection<MethodMatchingInfo>> components2MatchingInfo = new HashMap<>();

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

  private Class<?>[] getRegisteredComponentInterfaces() {
    return this.registeredComponentInterfaces;
  }

}
