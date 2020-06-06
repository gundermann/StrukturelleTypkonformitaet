package de.fernuni.hagen.ma.gundermann.typkonverter.structureByNames;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import de.fernuni.hagen.ma.gundermann.typkonverter.BehaviorDefinition;
import de.fernuni.hagen.ma.gundermann.typkonverter.StructureDefinition;
import de.fernuni.hagen.ma.gundermann.typkonverter.TypeConverter;

public class StructureByNamesTypeConverter<T> implements TypeConverter<T>{

  private final Class<T> targetStructure;

  private final ProxyFactory<T> proxyFactory;

private StructureDefinition structureDefinition;

  // Was ist mit ENUMs?
  public StructureByNamesTypeConverter( Class<T> targetStructure, StructureDefinition structureDefinition, BehaviorDefinition behaviorDefinition ) {
    this.targetStructure = targetStructure;
	this.structureDefinition = structureDefinition;
    // Grundlagen pruefen:
    // Interface oder Klasse
    if ( targetStructure.isInterface() ) {
      proxyFactory = new InterfaceProxyFactory<>( targetStructure );
    }
    else {
      // keine finalisierte Klasse
      // Default-Kontruktor vorhanden
      proxyFactory = new ClassProxyFactory<>( targetStructure );
    }
  }

  // FIXME Strukturelle Typkonformit�t muss rekursiv festgestellt werden!
  @Override
  public boolean matchesStructural( Object convertationObject ) {
    // Wenn es nominal passt, dann passt es auch strukturell
    if ( matchesNominal( convertationObject ) ) {
      return true;
    }
    // Strukturgleichheit definieren und pruefen!
    // Struktur: definiert durch Methodenkoepfe
    // Strukturgleichheit = alle Methodenkoepfe stimmen ueberein
    return checkDeclaredMethods( convertationObject );
  }

  private boolean checkDeclaredMethods( Object convertationObject ) {
    Class<?> source = convertationObject.getClass();
    Method[] declaredTargetMethods = targetStructure.getDeclaredMethods();
    return Stream.of( declaredTargetMethods ).filter(this::definesStructure).allMatch( m -> isStructurallyDeclaredInClass( m, source ) );
  }
  
  private boolean definesStructure(Method method) {
	  if(structureDefinition == StructureDefinition.ALL_METHODS_NECESSARY) {
		  return true;
	  }
	  //hier fehlen noch die ausimplementierten Methoden aus Klassen
	  return !method.isDefault();
  }

  private boolean isStructurallyDeclaredInClass( Method m, Class<?> source ) {
    try {
      Method declaredSourceMethod = source.getDeclaredMethod( m.getName(), m.getParameterTypes() );
      return declaredSourceMethod.getReturnType().equals( m.getReturnType() ) || isStructurallyDeclaredInClass(m, source.getSuperclass());
    }
    catch ( NoSuchMethodException | SecurityException e ) {
      return source.getSuperclass() != null && isStructurallyDeclaredInClass(m, source.getSuperclass());
    }
  }

  private boolean matchesNominal( Object convertationObject ) {
    return convertationObject.getClass().isAssignableFrom( targetStructure );
  }

  /**
   * Requires: convertationObject matches structurally targetStructure
   *
   * @param convertationObject
   * @return
   */
  // FIXME Strukturelle Typkonformit�t muss rekursiv festgestellt werden!
  @Override
  public T convertStructural( Object convertationObject ) {
    T targetInstance = proxyFactory.createProxy( convertationObject );
    return targetInstance;
  }

}
