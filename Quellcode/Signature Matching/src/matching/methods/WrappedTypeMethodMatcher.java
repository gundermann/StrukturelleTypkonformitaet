package matching.methods;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.modules.ModuleMatchingInfo;
import matching.modules.ModuleMatchingInfoFactory;

/**
 * Dieser Matcher beachtet, dass die Typen (Return- und Argumenttypen) einer der beiden Methoden in einem Typ der
 * anderen Methode enthalten sein können (Wrapper).
 */
public class WrappedTypeMethodMatcher implements MethodMatcher {

  // Versuch: Cache der Wrapped-Prüfungen
  // Grund: Bei der Wrapped-Prüfung von boolean und Boolean über den CombinedMethodMatcher kam es zu einem StackOverflow
  // Idee: Die geprüften Kombinationen werden gecached. Sofern eine gecachedte Kombination nochmal geprüft wird, wird
  // die Prüfung einfach übersprungen.
  final Map<Class<?>[], Boolean> cachedWrappedTypeChecks = new HashMap<>();

  private final Supplier<MethodMatcher> innerMethodMatcherSupplier;

  public WrappedTypeMethodMatcher( Supplier<MethodMatcher> innerMethodMatcherSupplier ) {
    this.innerMethodMatcherSupplier = innerMethodMatcherSupplier;
  }

  @Override
  public boolean matches( Method m1, Method m2 ) {
    if ( innerMethodMatcherSupplier.get().matches( m1, m2 ) ) {
      return true;
    }
    MethodStructure ms1 = MethodStructure.createFromDeclaredMethod( m1 );
    MethodStructure ms2 = MethodStructure.createFromDeclaredMethod( m2 );
    return matches( ms1, ms2 );
  }

  private boolean matches( MethodStructure ms1, MethodStructure ms2 ) {

    if ( ms1.getSortedArgumentTypes().length != ms2.getSortedArgumentTypes().length ) {
      return false;
    }
    if ( !matchesType( ms1.getReturnType(), ms2.getReturnType() ) ) {
      return false;
    }
    for ( int i = 0; i < ms1.getSortedArgumentTypes().length; i++ ) {
      if ( !matchesType( ms1.getSortedArgumentTypes()[i], ms2.getSortedArgumentTypes()[i] ) ) {
        return false;
      }
    }
    return true;
  }

  /**
   * Prüft auf Gleichheit oder auf Wrapped
   *
   * @param t1
   * @param t2
   * @return t1 = t2 || t1 > t2 || t2 > t1
   */

  @Override
  public boolean matchesType( Class<?> t1, Class<?> t2 ) {
    return
    // Darf die Gleichheit hier geprüft werden?
    // NEIN: Das ist wenn überhaupt die Aufgabe des inneren Matchers
    // t1.equals( t2 ) ||
    innerMethodMatcherSupplier.get().matchesType( t1, t2 ) ||
        isWrappedIn( t1, t2 )
        || isWrappedIn( t2, t1 );
  }

  /**
   * Prüft auf Gleichheit oder auf Wrapped in eine Richtung
   *
   * @param t1
   * @param t2
   * @return t1 = t2 || t1 in t2
   */
  boolean matchesWrappedOneDirection( Class<?> t1, Class<?> t2 ) {
    return t1.equals( t2 ) || isWrappedIn( t1, t2 );
  }

  private boolean isWrappedIn( Class<?> checkType, Class<?> queryType ) {
    // Hier ist die Frage, ob nur ein Attribut vom Typ des wrappedType im Wrapper vorhanden sein muss, oder nur eine
    // Methode mit den Rückgabewert des wrappedType, oder sogar beides.

    // Erster Versuch: beides (siehe Frage-Antwort-Spiel in containsMethodWithType
    // return containsFieldWithType( wrapperType, wrappedType ) &&
    // containsMethodWithType( wrapperType, wrappedType );

    // Zweiter Versuch: nur Attribute

    // return containsFieldWithType( wrapperType, wrappedType, innerCompareFunction );

    // Dritter Versuch: wie zweiter Versucht nur mit Cache.
    Class<?>[] cacheKey = new Class<?>[] { checkType, queryType };
    if ( isCombinationCached( cacheKey ) ) {
      // false, weil die Überprüfung noch nicht stattgefunden bzw. wenn sie bereits true ermittelt hatte, dann wäre die
      // Überprüfung bereits erfolgreich gewesen
      return getResultFromCache( cacheKey );
    }
    cachedWrappedTypeChecks.put( cacheKey, null );
    boolean result = containsFieldWithType( queryType, checkType );
    cachedWrappedTypeChecks.put( cacheKey, result );
    return result;

  }

  private boolean getResultFromCache( Class<?>[] newCacheKey ) {
    for ( Entry<Class<?>[], Boolean> cacheEntries : cachedWrappedTypeChecks.entrySet() ) {
      Class<?>[] cachedKey = cacheEntries.getKey();
      if ( Objects.equals( cachedKey[0], newCacheKey[0] ) && Objects.equals( cachedKey[1], newCacheKey[1] ) ) {
        return cacheEntries.getValue() == null ? false : cacheEntries.getValue();
      }
    }
    return false;
  }

  private boolean isCombinationCached( Class<?>[] newCacheKey ) {
    for ( Class<?>[] cacheKeys : cachedWrappedTypeChecks.keySet() ) {
      if ( Objects.equals( cacheKeys[0], newCacheKey[0] ) && Objects.equals( cacheKeys[1], newCacheKey[1] ) ) {
        return true;
      }
    }
    return false;
  }

  // @Deprecated
  // private boolean containsMethodWithType( Class<?> checkingClass, Class<?> returnType ) {
  // // Frage: Sollen hier nur auf sichtbare Methoden geprüft werden
  // // Antwort: Ja, denn die Methoden müssen später durch den Glue-Code aufgerufen werden
  // // Anschlussfrage: Warum ist das notwendig? Der Glue-Code verwendet Reflection. Damit interessiert die Sichtbarkeit
  // // der Methoden nicht. Wenn man das weiter denkt, dann sind nicht einmal die Methoden relevant, weil:
  // // 1. Die Attribute per Relflection abgegriffen werden können
  // // 2. Nicht sichergestellt werden kann, dass eine Methode, die den ein Objekt zurückgibt, welches es gleichen Typ,
  // // wie ein Attribut hat, auch genau dieses Attribut zurückggibt.
  // Method[] methodsOfWrapper = checkingClass.getDeclaredMethods();
  // for ( Method method : methodsOfWrapper ) {
  // if ( method.getReturnType().equals( returnType ) ) {
  // return true;
  // }
  // }
  //
  // return false;
  // }

  private boolean containsFieldWithType( Class<?> wrapperClass, Class<?> wrappedType ) {
    Field[] fieldsOfWrapper = filterStaticFields( wrapperClass.getDeclaredFields() );
    for ( Field field : fieldsOfWrapper ) {
      if ( innerMethodMatcherSupplier.get().matchesType( field.getType(), wrappedType ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Set<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod ) {
    if ( !matches( checkMethod, queryMethod ) ) {
      return new HashSet<>();
    }
    MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( checkMethod, queryMethod );
    Collection<ModuleMatchingInfo> returnTypeMatchingInfos = calculateTypeMatchingInfos( queryMethod.getReturnType(),
        checkMethod.getReturnType() );

    Collection<Map<ParamPosition, Collection<ModuleMatchingInfo>>> argumentTypesMatchingInfos = calculateArgumentMatchingInfos(
        checkMethod.getParameterTypes(), queryMethod.getParameterTypes() );
    return factory.createFromTypeMatchingInfos( returnTypeMatchingInfos, argumentTypesMatchingInfos );
  }

  private Collection<Map<ParamPosition, Collection<ModuleMatchingInfo>>> calculateArgumentMatchingInfos(
      Class<?>[] checkATs, Class<?>[] queryATs ) {
    Map<ParamPosition, Collection<ModuleMatchingInfo>> matchingMap = new HashMap<>();
    for ( int i = 0; i < checkATs.length; i++ ) {
      Class<?> checkAT = checkATs[i];
      Class<?> queryAT = queryATs[i];
      Collection<ModuleMatchingInfo> infos = calculateTypeMatchingInfos( checkAT, queryAT );
      matchingMap.put( new ParamPosition( i, i ), infos );
    }
    return Collections.singletonList( matchingMap );
  }

  @Override
  public Collection<ModuleMatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType ) {
    Collection<ModuleMatchingInfo> allMatchingInfos = new ArrayList<>();

    MethodMatcher innerMethodMatcher = innerMethodMatcherSupplier.get();
    if ( innerMethodMatcher.matchesType( checkType, queryType ) ) {
      Collection<ModuleMatchingInfo> matchingInfos = innerMethodMatcher.calculateTypeMatchingInfos( checkType,
          queryType );
      allMatchingInfos.addAll( matchingInfos );
    }
    if ( isWrappedIn( checkType, queryType ) ) {
      Collection<ModuleMatchingInfo> matchingInfos = calculateWrappedTypeMatchingInfos( queryType, checkType, false );
      allMatchingInfos.addAll( matchingInfos );
    }
    if ( isWrappedIn( queryType, checkType ) ) {
      Collection<ModuleMatchingInfo> matchingInfos = calculateWrappedTypeMatchingInfos( checkType, queryType, true );
      allMatchingInfos.addAll( matchingInfos );
    }
    return allMatchingInfos;
  }

  private Collection<ModuleMatchingInfo> calculateWrappedTypeMatchingInfos( Class<?> wrapperClass,
      Class<?> wrappedType, boolean isTargetWrapper ) {
    Collection<ModuleMatchingInfo> allMatchingInfos = new ArrayList<>();
    Field[] fieldsOfWrapper = filterStaticFields( wrapperClass.getDeclaredFields() );
    MethodMatcher innerMethodMatcher = innerMethodMatcherSupplier.get();
    for ( Field field : fieldsOfWrapper ) {
      // TODO hier wird nur auf der ersten Ebene geprüft. Eine tiefere Verschachtelung wird noch nicht ermöglicht.
      // ABER: Der ganze Matcher macht das noch nicht. Auch beim Prüfen des Matchings wird nur auf der obersten Ebene
      // geprüft.
      if ( innerMethodMatcher.matchesType( field.getType(), wrappedType ) ) {
        Collection<ModuleMatchingInfo> infosFromInnerMatcher = innerMethodMatcher
            .calculateTypeMatchingInfos( field.getType(), wrappedType );
        final ModuleMatchingInfoFactory factory;
        if ( isTargetWrapper ) {
          factory = new ModuleMatchingInfoFactory( wrapperClass, field.getName(), wrappedType );
        }
        else {
          factory = new ModuleMatchingInfoFactory( wrappedType, wrapperClass, field.getName() );
        }
        allMatchingInfos.addAll( enhanceInfosWithDelegate( infosFromInnerMatcher, factory ) );
      }
    }
    return allMatchingInfos;
  }

  /**
   * Die statischen Felder müssen herausgefiltert werden.
   *
   * @param declaredFields
   * @return
   */
  private Field[] filterStaticFields( Field[] declaredFields ) {
    return Stream.of( declaredFields ).filter( f -> !Modifier.isStatic( f.getModifiers() ) )
        .collect( Collectors.toList() )
        .toArray( new Field[] {} );
  }

  private Collection<ModuleMatchingInfo> enhanceInfosWithDelegate( Collection<ModuleMatchingInfo> infos,
      ModuleMatchingInfoFactory factory ) {
    Collection<ModuleMatchingInfo> enhancedInfos = new ArrayList<>();
    for ( ModuleMatchingInfo mmi : infos ) {
      ModuleMatchingInfo enhancedInfo = factory.create( mmi.getMethodMatchingInfos() );
      enhancedInfos.add( enhancedInfo );
    }
    return enhancedInfos;
  }

}
