package de.fernuni.hagen.ma.gundermann.ejb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import glue.SignatureMatchingTypeConverter;
import matching.modules.ModuleMatcher;
import matching.modules.ModuleMatchingInfo;
import tester.ComponentTester;

public enum EJBContainer {
  CONTAINER;

  Map<Class<?>, Collection<Object>> containerMap = new HashMap<>();

  private EJBContainer() {
    try {
      init( Files.readAllLines( Paths.get( "src/de/fernuni/hagen/ma/gundermann/ejb/pool.txt" ) ) );
    }
    catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  private void init( List<String> beans ) {
    for ( String bean : beans ) {
      try {
        Class<?> loadedInterface = ClassLoader.getSystemClassLoader().loadClass( bean );
        registerBean( loadedInterface, null );
      }
      catch ( ClassNotFoundException cnfe ) {
        System.out.println( String.format( "class not found %s", bean ) );
      }
    }
    System.out.println( String.format( "registeres bean interfaces: %d", containerMap.keySet().size() ) );
  }

  public <BI> void registerBean( Class<BI> beanInterface, BI bean ) {
    containerMap.compute( beanInterface, ( key, value ) -> {
      Collection<Object> localValue = new ArrayList<>();
      if ( value != null ) {
        localValue.addAll( value );
      }
      if ( bean != null ) {
        localValue.add( bean );
      }
      return localValue;
    } );
  }

  private Collection<?> getBeans( Class<?> componentClass ) {
    return containerMap.get( componentClass );
  }

  public <DesiredInterface> DesiredInterface getDesiredBean(
      Class<DesiredInterface> desiredInterface ) {
    Collection<Class<?>> matchingBeanInterfaces = findBeansBySignatureMatching( desiredInterface );
    // System.out.println( "Matching Bean-Interfaces of " + desiredInterface.getName() );
    // matchingBeanInterfaces.stream().map( Class::getName ).forEach( System.out::println );
    System.out.println( String.format( "count: %d", matchingBeanInterfaces.size() ) );

    // Hier können weitere Filter und Heuristiken eingebaut werden

    return getComposedBean( desiredInterface, matchingBeanInterfaces );
  }

  private <DesiredInterface> DesiredInterface getComposedBean(
      Class<DesiredInterface> desiredInterface, Collection<Class<?>> matchingBeanInterfaces ) {
    Set<ComponentInfos<DesiredInterface>> rankedComponentInfos = getSortedModuleMatchingInfos( desiredInterface,
        matchingBeanInterfaces );
    System.out.println( String.format( "ranking of relevant components" ) );
    rankedComponentInfos.stream().forEach( c -> System.out
        .println( String.format( "rank: %d component: %s", c.getRank(), c.getComponentClass().getName() ) ) );
    List<ComponentInfos<DesiredInterface>> fullMatchedComponents = rankedComponentInfos.stream()
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
      List<ComponentInfos<DesiredInterface>> fullMatchedComponents, Class<DesiredInterface> desiredInterface ) {
    SignatureMatchingTypeConverter<DesiredInterface> converter = new SignatureMatchingTypeConverter<>(
        desiredInterface );
    ComponentTester<DesiredInterface> componentTester = new ComponentTester<>( desiredInterface );

    for ( ComponentInfos<DesiredInterface> componentInfo : fullMatchedComponents ) {
      Class<?> componentClass = componentInfo.getComponentClass();
      Collection<?> components = getBeans( componentClass );
      for ( ModuleMatchingInfo<DesiredInterface> matchingInfo : componentInfo.getMatchingInfos() ) {
        for ( Object component : components ) {
          DesiredInterface convertedComponent = converter.convert( component, matchingInfo );
          if ( componentTester.testComponent( convertedComponent ) ) {
            return convertedComponent;
          }
        }
      }
    }
    return null;
  }

  private <DesiredInterface> Set<ComponentInfos<DesiredInterface>> getSortedModuleMatchingInfos(
      Class<DesiredInterface> desiredInterface, Collection<Class<?>> matchingBeanInterfaces ) {
    List<ComponentInfos<DesiredInterface>> componentInfoSet = new ArrayList<>();
    ModuleMatcher<DesiredInterface> moduleMatcher = new ModuleMatcher<>( desiredInterface );
    for ( Class<?> matchingBeanInterface : matchingBeanInterfaces ) {
      Set<ModuleMatchingInfo<DesiredInterface>> matchingInfos = moduleMatcher
          .calculateMatchingInfos( matchingBeanInterface );
      ComponentInfos<DesiredInterface> componentInfos = new ComponentInfos<>( matchingBeanInterface );
      componentInfos.setModuleMatchingInfos( matchingInfos );
      componentInfoSet.add( componentInfos );
    }
    Collections.sort( componentInfoSet, ( c1, c2 ) -> Integer.compare( c1.getRank(), c2.getRank() ) );
    return new HashSet<>( componentInfoSet );
  }

  private <DesiredInterface> Collection<Class<?>> findBeansBySignatureMatching(
      Class<DesiredInterface> desiredInterface ) {
    ModuleMatcher<DesiredInterface> moduleMatcher = new ModuleMatcher<>( desiredInterface );
    Collection<Class<?>> matchedBeans = new ArrayList<>();
    for ( Class<?> beanInterfaces : containerMap.keySet() ) {
      // Dieser Code ist nur für das Debugging-Analyse notwendig
      // if ( beanInterfaces.equals( ElerFTStammdatenAuskunftService.class ) ) {
      // System.out.println( "BEAN OF INTEREST" );
      // }
      boolean matchesFull = moduleMatcher.matches( beanInterfaces );
      if ( !matchesFull ) {
        // boolean partlyMatches = moduleMatcher.partlyMatches( beanInterfaces );
        // if ( !partlyMatches ) {
        continue;
        // }
      }
      matchedBeans.add( beanInterfaces );
    }
    return matchedBeans;
  }

}
