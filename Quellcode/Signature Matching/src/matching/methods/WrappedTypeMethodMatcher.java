package matching.methods;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Dieser Matcher beachtet, dass die Typen (Return- und Argumenttypen) einer der beiden Methoden in einem Typ der
 * anderen Methode enthalten sein k�nnen (Wrapper).
 */
public class WrappedTypeMethodMatcher implements MethodMatcher {

  // Versuch: Cache der Wrapped-Pr�fungen
  // Grund: Bei der Wrapped-Pr�fung von boolean und Boolean �ber den CombinedMethodMatcher kam es zu einem StackOverflow
  // Idee: Die gepr�ften Kombinationen werden gecached. Sofern eine gecachedte Kombination nochmal gepr�ft wird, wird
  // die Pr�fung einfach �bersprungen.
  Map<Class<?>[], Boolean> cachedWrappedTypeChecks = new HashMap<>();

  @Override
  public boolean matches( Method m1, Method m2 ) {
    MethodStructure ms1 = MethodStructure.createFromDeclaredMethod( m1 );
    MethodStructure ms2 = MethodStructure.createFromDeclaredMethod( m2 );
    return matches( ms1, ms2 );
  }

  private boolean matches( MethodStructure ms1, MethodStructure ms2 ) {
    if ( ms1.getSortedArgumentTypes().length != ms2.getSortedArgumentTypes().length ) {
      return false;
    }
    if ( !matchesWrapped( ms1.getReturnType(), ms2.getReturnType(), Objects::equals ) ) {
      return false;
    }
    for ( int i = 0; i < ms1.getSortedArgumentTypes().length; i++ ) {
      if ( !matchesWrapped( ms1.getSortedArgumentTypes()[i], ms2.getSortedArgumentTypes()[i], Objects::equals ) ) {
        return false;
      }
    }
    return true;
  }

  /**
   * Pr�ft auf Gleichheit oder auf Wrapped
   *
   * @param t1
   * @param t2
   * @return t1 = t2 || t1 > t2 || t2 > t1
   */
  boolean matchesWrapped( Class<?> t1, Class<?> t2,
      BiFunction<Class<?>, Class<?>, Boolean> innerCompareFunction ) {
    return t1.equals( t2 ) || isWrappedIn( t1, t2, innerCompareFunction )
        || isWrappedIn( t2, t1, innerCompareFunction );
  }

  /**
   * Pr�ft auf Gleichheit oder auf Wrapped in eine Richtung
   *
   * @param t1
   * @param t2
   * @return t1 = t2 || t1 in t2
   */
  boolean matchesWrappedOneDirection( Class<?> t1, Class<?> t2,
      BiFunction<Class<?>, Class<?>, Boolean> innerCompareFunction ) {
    return t1.equals( t2 ) || isWrappedIn( t1, t2, innerCompareFunction );
  }

  private boolean isWrappedIn( Class<?> wrappedType, Class<?> wrapperType,
      BiFunction<Class<?>, Class<?>, Boolean> innerCompareFunction ) {
    // Hier ist die Frage, ob nur ein Attribut vom Typ des wrappedType im Wrapper vorhanden sein muss, oder nur eine
    // Methode mit den R�ckgabewert des wrappedType, oder sogar beides.

    // Erster Versuch: beides (siehe Frage-Antwort-Spiel in containsMethodWithType
    // return containsFieldWithType( wrapperType, wrappedType ) &&
    // containsMethodWithType( wrapperType, wrappedType );

    // Zweiter Versuch: nur Attribute

    // return containsFieldWithType( wrapperType, wrappedType, innerCompareFunction );

    // Dritter Versuch: wie zweiter Versucht nur mit Cache.
    Class<?>[] cacheKey = new Class<?>[] { wrappedType, wrapperType };
    if ( isCombinationCached( cacheKey ) ) {
      // false, weil die �berpr�fung noch nicht stattgefunden bzw. wenn sie bereits true ermittelt hatte, dann w�re die
      // �berpr�fung bereits erfolgreich gewesen
      return getResultFromCache( cacheKey );
    }
    cachedWrappedTypeChecks.put( cacheKey, null );
    boolean result = containsFieldWithType( wrapperType, wrappedType, innerCompareFunction );
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

  @Deprecated
  private boolean containsMethodWithType( Class<?> checkingClass, Class<?> returnType ) {
    // Frage: Sollen hier nur auf sichtbare Methoden gepr�ft werden
    // Antwort: Ja, denn die Methoden m�ssen sp�ter durch den Glue-Code aufgerufen werden
    // Anschlussfrage: Warum ist das notwendig? Der Glue-Code verwendet Reflection. Damit interessiert die Sichtbarkeit
    // der Methoden nicht. Wenn man das weiter denkt, dann sind nicht einmal die Methoden relevant, weil:
    // 1. Die Attribute per Relflection abgegriffen werden k�nnen
    // 2. Nicht sichergestellt werden kann, dass eine Methode, die den ein Objekt zur�ckgibt, welches es gleichen Typ,
    // wie ein Attribut hat, auch genau dieses Attribut zur�ckggibt.
    Method[] methodsOfWrapper = checkingClass.getDeclaredMethods();
    for ( Method method : methodsOfWrapper ) {
      if ( method.getReturnType().equals( returnType ) ) {
        return true;
      }
    }

    return false;
  }

  private boolean containsFieldWithType( Class<?> checkingClass, Class<?> fieldType,
      BiFunction<Class<?>, Class<?>, Boolean> compareFunction ) {
    Field[] fieldsOfWrapper = checkingClass.getDeclaredFields();
    for ( Field field : fieldsOfWrapper ) {
      if ( compareFunction.apply( field.getType(), fieldType ) ) {
        return true;
      }
    }
    return false;
  }

}
