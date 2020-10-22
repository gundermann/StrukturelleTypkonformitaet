package de.fernuni.hagen.ma.gundermann.ejb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import matching.modules.ModuleMatcher;

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

  public <DesiredInterface, ComposedBean extends DesiredInterface> ComposedBean getDesiredBean(
      Class<DesiredInterface> desiredInterface ) {
    Collection<Class<?>> matchingBeanInterfaces = findBeansBySignatureMatching( desiredInterface );
    System.out.println( "Matching Bean-Interfaces of " + desiredInterface.getName() );
    matchingBeanInterfaces.stream().map( Class::getName ).forEach( System.out::println );
    System.out.println( String.format( "count: %d", matchingBeanInterfaces.size() ) );
    return null;
  }

  private Collection<Class<?>> findBeansBySignatureMatching( Class<?> desiredInterface ) {
    ModuleMatcher moduleMatcher = new ModuleMatcher();
    Collection<Class<?>> matchedBeans = new ArrayList<>();
    for ( Class<?> beanInterfaces : containerMap.keySet() ) {
      // Dieser Code ist nur für das Debugging-Analyse notwendig
      // if ( beanInterfaces.equals( ElerFTStammdatenAuskunftService.class ) ) {
      // System.out.println( "BEAN OF INTEREST" );
      // }
      boolean matchesFull = moduleMatcher.matches( beanInterfaces, desiredInterface );
      if ( !matchesFull ) {
        boolean partlyMatches = moduleMatcher.partlyMatches( beanInterfaces, desiredInterface );
        if ( !partlyMatches ) {
          continue;
        }
      }
      matchedBeans.add( beanInterfaces );
    }
    return matchedBeans;
  }

}
