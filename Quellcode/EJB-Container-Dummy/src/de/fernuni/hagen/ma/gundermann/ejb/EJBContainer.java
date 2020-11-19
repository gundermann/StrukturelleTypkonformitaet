package de.fernuni.hagen.ma.gundermann.ejb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fernuni.hagen.ma.gundermann.ejb.util.Logger;

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

  public void reset() {
    containerMap.clear();
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

  public Collection<?> getBeans( Class<?> componentClass ) {
    return containerMap.get( componentClass );
  }

  public Collection<Class<?>> getRegisteredBeanInterfaces() {
    return containerMap.keySet();
  }

}
