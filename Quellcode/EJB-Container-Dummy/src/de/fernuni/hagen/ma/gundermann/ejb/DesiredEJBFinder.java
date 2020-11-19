package de.fernuni.hagen.ma.gundermann.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import glue.SignatureMatchingTypeConverter;
import matching.modules.ModuleMatcher;
import matching.modules.ModuleMatchingInfo;
import tester.ComponentTester;

public class DesiredEJBFinder {

  private final Class<?>[] beanInterfaces;

  private final Function<Class<?>, Optional<?>> optBeanGetter;

  public DesiredEJBFinder( Class<?>[] beanInterfaces, Function<Class<?>, Optional<?>> optBeanGetter ) {
    this.beanInterfaces = beanInterfaces;
    this.optBeanGetter = optBeanGetter;

  }

  private Optional<?> getBean( Class<?> componentClass ) {
    return optBeanGetter.apply( componentClass );
  }

  public <DesiredInterface> DesiredInterface getDesiredBean(
      Class<DesiredInterface> desiredInterface ) {
    Collection<Class<?>> matchingBeanInterfaces = findBeansBySignatureMatching( desiredInterface );
    // Logger.info( "Matching Bean-Interfaces of " + desiredInterface.getName() );
    // matchingBeanInterfaces.stream().map( Class::getName ).forEach( System.out::println );
    // Logger.info( String.format( "count: %d", matchingBeanInterfaces.size() ) );

    // Hier können weitere Filter und Heuristiken eingebaut werden

    return getComposedBean( desiredInterface, matchingBeanInterfaces );
  }

  private <DesiredInterface> DesiredInterface getComposedBean(
      Class<DesiredInterface> desiredInterface, Collection<Class<?>> matchingBeanInterfaces ) {
    // Logger.info( "create ComponentInfos" );
    Set<ComponentInfos> rankedComponentInfos = getSortedModuleMatchingInfos( desiredInterface,
        matchingBeanInterfaces );
    // Logger.info( String.format( "ranking of relevant components" ) );
    // rankedComponentInfos.stream().forEach(
    // c -> Logger.info( String.format( "rank: %d component: %s", c.getRank(), c.getComponentClass().getName() ) ) );
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
      Optional<?> optComponent = getBean( componentClass );

      if ( !optComponent.isPresent() ) {
        continue;
      }
      Object component = optComponent.get();
      // TODO hier wäre eine Heuristik angebracht, welche die MatchingInfos der ComponentInfo in eine Reihenfolge
      // bringt.
      for ( ModuleMatchingInfo matchingInfo : componentInfo.getMatchingInfos() ) {
        // Logger.infoF( "test component: %s", component.getClass().getName() );
        DesiredInterface convertedComponent = converter.convert( component, matchingInfo );
        if ( componentTester.testComponent( convertedComponent ) ) {
          return convertedComponent;
        }
      }
    }
    return null;

  }

  private <DesiredInterface> Set<ComponentInfos> getSortedModuleMatchingInfos(
      Class<DesiredInterface> desiredInterface, Collection<Class<?>> matchingBeanInterfaces ) {
    List<ComponentInfos> componentInfoSet = new ArrayList<>();
    ModuleMatcher<DesiredInterface> moduleMatcher = new ModuleMatcher<>( desiredInterface );
    for ( Class<?> matchingBeanInterface : matchingBeanInterfaces ) {
      // Logger.info( String.format( "collect ModuleMatchingInfo: %s", matchingBeanInterface.getName() ) );
      Set<ModuleMatchingInfo> matchingInfos = moduleMatcher
          .calculateMatchingInfos( matchingBeanInterface );
      ComponentInfos componentInfos = new ComponentInfos( matchingBeanInterface );
      componentInfos.setModuleMatchingInfos( matchingInfos );
      componentInfos.addContext( matchingBeanInterface.getName() );
      componentInfoSet.add( componentInfos );
    }
    // Logger.info( String.format( "ComponentInfos created: %d", componentInfoSet.size() ) );
    // Logger.info( "sort ComponentInfos" );
    Collections.sort( componentInfoSet, ( c1, c2 ) -> Integer.compare( c1.getRank(), c2.getRank() ) );
    return new HashSet<>( componentInfoSet );
  }

  private <DesiredInterface> Collection<Class<?>> findBeansBySignatureMatching(
      Class<DesiredInterface> desiredInterface ) {
    ModuleMatcher<DesiredInterface> moduleMatcher = new ModuleMatcher<>( desiredInterface );
    Collection<Class<?>> matchedBeans = new ArrayList<>();
    for ( Class<?> beanInterface : getRegisteredBeanInterfaces() ) {
      // Dieser Code ist nur für das Debugging-Analyse notwendig
      // if ( beanInterfaces.equals( ElerFTStammdatenAuskunftService.class ) ) {
      // Logger.info( "BEAN OF INTEREST" );
      // }
      boolean matchesFull = moduleMatcher.matches( beanInterface );
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

  private Class<?>[] getRegisteredBeanInterfaces() {
    return this.beanInterfaces;
  }

}
