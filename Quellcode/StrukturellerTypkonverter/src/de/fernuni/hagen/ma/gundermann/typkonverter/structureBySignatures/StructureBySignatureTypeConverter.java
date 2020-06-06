package de.fernuni.hagen.ma.gundermann.typkonverter.structureBySignatures;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fernuni.hagen.ma.gundermann.typkonverter.BehaviorDefinition;
import de.fernuni.hagen.ma.gundermann.typkonverter.StructureDefinition;
import de.fernuni.hagen.ma.gundermann.typkonverter.TypeConverter;

public class StructureBySignatureTypeConverter<T> implements TypeConverter<T>{

  private final Class<T> targetStructure;

  private final ProxyFactory<T> proxyFactory;

  // Was ist mit ENUMs?
  public StructureBySignatureTypeConverter( Class<T> targetStructure, StructureDefinition structureDefinition, BehaviorDefinition behaviorDefinition ) {
    this.targetStructure = targetStructure;
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
    List<MethodStructure> listOfTargetMethodeStructures = Stream.of( declaredTargetMethods )
        .map( MethodStructure::createFromDeclaredMethod ).collect( Collectors.toList() );
    if ( containsStructuralDuplicates( declaredTargetMethods ) ) {
      return false;
    }
    Method[] declaredSourceMethods = source.getDeclaredMethods();
    List<MethodStructure> listOfSourceMethodeStructures = Stream.of( declaredSourceMethods )
        .map( MethodStructure::createFromDeclaredMethod ).collect( Collectors.toList() );

    return listOfSourceMethodeStructures.stream()
        .allMatch( m -> isStructurallyDeclaredInClass( m, listOfTargetMethodeStructures ) );
  }

  private boolean containsStructuralDuplicates( Method[] declaredTargetMethods ) {
    long distinctCount = Stream.of( declaredTargetMethods )
        .map( MethodStructure::createFromDeclaredMethod )
        .distinct()
        .count();
    return distinctCount != declaredTargetMethods.length;
  }

  private boolean isStructurallyDeclaredInClass( MethodStructure m,
      List<MethodStructure> listOfTargetMethodeStructures ) {
    return listOfTargetMethodeStructures.stream().filter( m::equals ).count() == 1;
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

  static class MethodStructure {
    private final Class<?> returnType;

    private final Class<?>[] sortedArgumentTypes;

    static MethodStructure createFromDeclaredMethod( Method method ) {
      return new MethodStructure( method.getReturnType(), method.getParameterTypes() );
    }

    private MethodStructure( Class<?> returnType, Class<?>[] sortedArgumentTypes ) {
      this.returnType = returnType;
      this.sortedArgumentTypes = sortedArgumentTypes != null ? sortedArgumentTypes : new Class<?>[] {};
    }

    @Override
    public int hashCode() {
      int prime = 7;
      int hash = prime * returnType.hashCode();
      for ( int i = 0; i < sortedArgumentTypes.length; i++ ) {
        hash += prime * sortedArgumentTypes[i].hashCode();
      }
      return hash;
    }

    @Override
    public boolean equals( Object obj ) {
      if ( obj instanceof MethodStructure ) {
        MethodStructure other = (MethodStructure) obj;
        if ( returnType != other.returnType ) {
          return false;
        }
        if ( sortedArgumentTypes.length != other.sortedArgumentTypes.length ) {
          return false;
        }
        for ( int i = 0; i < sortedArgumentTypes.length; i++ ) {
          if ( sortedArgumentTypes[i] != other.sortedArgumentTypes[i] ) {
            return false;
          }
        }
        return true;
      }
      return false;
    }

  }
}
