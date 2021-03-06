package matching.modules;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import matching.MatcherRate;
import matching.Setting;

/**
 * Dieser Matcher beachtet, dass die Typen (Return- und Argumenttypen) einer der beiden Methoden in einem Typ der
 * anderen Methode enthalten sein k�nnen (Wrapper).
 */
public class WrappedTypeMatcher implements TypeMatcher {

  // Versuch: Cache der Wrapped-Pr�fungen
  // Grund: Bei der Wrapped-Pr�fung von boolean und Boolean �ber den CombinedMethodMatcher kam es zu einem
  // StackOverflow
  // Idee: Die gepr�ften Kombinationen werden gecached. Sofern eine gecachedte Kombination nochmal gepr�ft wird,
  // wird
  // die Pr�fung einfach �bersprungen.
  final Map<Class<?>[], Boolean> cachedWrappedTypeChecks = new HashMap<>();

  private final Supplier<TypeMatcher> innerMethodMatcherSupplier;

  public WrappedTypeMatcher( Supplier<TypeMatcher> innerMethodMatcherSupplier ) {
    this.innerMethodMatcherSupplier = innerMethodMatcherSupplier;
  }

  /**
   * Pr�ft auf Wrapped
   *
   * @param t1
   * @param t2
   * @return t1 in t2 || t2 in t1
   */

  @Override
  public boolean matchesType( Class<?> t1, Class<?> t2 ) {
    return
    // Darf die Gleichheit hier gepr�ft werden?
    // NEIN: Das ist wenn �berhaupt die Aufgabe des inneren Matchers
    // t1.equals( t2 ) ||
    isWrappedIn( t1, t2 )
        || isWrappedIn( t2, t1 );
  }

  /**
   * Pr�ft auf Wrapped in eine Richtung
   *
   * @param t1
   * @param t2
   * @return t1 in t2
   */
  boolean matchesWrappedOneDirection( Class<?> t1, Class<?> t2 ) {
    return isWrappedIn( t1, t2 );
  }

  private boolean isWrappedIn( Class<?> checkType, Class<?> queryType ) {
    // Hier ist die Frage, ob nur ein Attribut vom Typ des wrappedType im Wrapper vorhanden sein muss, oder nur eine
    // Methode mit den R�ckgabewert des wrappedType, oder sogar beides.

    // Erster Versuch: beides (siehe Frage-Antwort-Spiel in containsMethodWithType
    // return containsFieldWithType( wrapperType, wrappedType ) &&
    // containsMethodWithType( wrapperType, wrappedType );

    // Zweiter Versuch: nur Attribute

    // return containsFieldWithType( wrapperType, wrappedType, innerCompareFunction );

    // Dritter Versuch: wie zweiter Versucht nur mit Cache.
    Class<?>[] cacheKey = new Class<?>[] { checkType, queryType };
    if ( isCombinationCached( cacheKey ) ) {
      // false, weil die �berpr�fung noch nicht stattgefunden bzw. wenn sie bereits true ermittelt hatte, dann
      // w�re die
      // �berpr�fung bereits erfolgreich gewesen
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
  // // Frage: Sollen hier nur auf sichtbare Methoden gepr�ft werden
  // // Antwort: Ja, denn die Methoden m�ssen sp�ter durch den Glue-Code aufgerufen werden
  // // Anschlussfrage: Warum ist das notwendig? Der Glue-Code verwendet Reflection. Damit interessiert die Sichtbarkeit
  // // der Methoden nicht. Wenn man das weiter denkt, dann sind nicht einmal die Methoden relevant, weil:
  // // 1. Die Attribute per Relflection abgegriffen werden k�nnen
  // // 2. Nicht sichergestellt werden kann, dass eine Methode, die den ein Objekt zur�ckgibt, welches es gleichen
  // Typ,
  // // wie ein Attribut hat, auch genau dieses Attribut zur�ckggibt.
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
  public Collection<ModuleMatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType ) {
    Collection<ModuleMatchingInfo> allMatchingInfos = new ArrayList<>();

    TypeMatcher innerMethodMatcher = innerMethodMatcherSupplier.get();
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
    TypeMatcher innerMethodMatcher = innerMethodMatcherSupplier.get();
    for ( Field field : fieldsOfWrapper ) {
      // TODO hier wird nur auf der ersten Ebene gepr�ft. Eine tiefere Verschachtelung wird noch nicht erm�glicht.
      // ABER: Der ganze Matcher macht das noch nicht. Auch beim Pr�fen des Matchings wird nur auf der obersten Ebene
      // gepr�ft.
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
   * Die statischen Felder m�ssen herausgefiltert werden.
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

  @Override
  public MatcherRate matchesWithRating( Class<?> checkType, Class<?> queryType ) {
    if ( matchesType( checkType, queryType ) ) {
      MatcherRate rate = new MatcherRate();
      rate.add( this.getClass().getSimpleName(), Setting.WRAPPEN_TYPE_MATCHER_BASE_RATING );
      return rate;
    }
    return null;
  }

}
