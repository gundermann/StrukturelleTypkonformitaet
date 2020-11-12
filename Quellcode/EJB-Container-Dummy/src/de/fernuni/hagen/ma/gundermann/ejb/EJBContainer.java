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

import de.fernuni.hagen.ma.gundermann.ejb.util.Logger;
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
        Logger.info( String.format( "class not found %s", bean ) );
      }
    }
    Logger.info( String.format( "registeres bean interfaces: %d", containerMap.keySet().size() ) );
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
    // Logger.info( "Matching Bean-Interfaces of " + desiredInterface.getName() );
    // matchingBeanInterfaces.stream().map( Class::getName ).forEach( System.out::println );
    Logger.info( String.format( "count: %d", matchingBeanInterfaces.size() ) );

    // Hier können weitere Filter und Heuristiken eingebaut werden

    return getComposedBean( desiredInterface, matchingBeanInterfaces );
  }

  private <DesiredInterface> DesiredInterface getComposedBean(
      Class<DesiredInterface> desiredInterface, Collection<Class<?>> matchingBeanInterfaces ) {
    Logger.info( "create ComponentInfos" );
    Set<ComponentInfos> rankedComponentInfos = getSortedModuleMatchingInfos( desiredInterface,
        matchingBeanInterfaces );
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
      Collection<?> components = getBeans( componentClass );

      // TODO hier wäre eine Heuristik angebracht, welche die MatchingInfos der ComponentInfo in eine Reihenfolge
      // bringt.
      for ( ModuleMatchingInfo matchingInfo : componentInfo.getMatchingInfos() ) {
        for ( Object component : components ) {
          Logger.infoF( "test component: %s", component.getClass().getName() );
          DesiredInterface convertedComponent = converter.convert( component, matchingInfo );
          if ( componentTester.testComponent( convertedComponent ) ) {
            return convertedComponent;
          }
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
      Logger.info( String.format( "collect ModuleMatchingInfo: %s", matchingBeanInterface.getName() ) );
      Set<ModuleMatchingInfo> matchingInfos = moduleMatcher
          .calculateMatchingInfos( matchingBeanInterface );
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

  private <DesiredInterface> Collection<Class<?>> findBeansBySignatureMatching(
      Class<DesiredInterface> desiredInterface ) {
    ModuleMatcher<DesiredInterface> moduleMatcher = new ModuleMatcher<>( desiredInterface );
    Collection<Class<?>> matchedBeans = new ArrayList<>();
    for ( Class<?> beanInterfaces : containerMap.keySet() ) {
      // Dieser Code ist nur für das Debugging-Analyse notwendig
      // if ( beanInterfaces.equals( ElerFTStammdatenAuskunftService.class ) ) {
      // Logger.info( "BEAN OF INTEREST" );
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
