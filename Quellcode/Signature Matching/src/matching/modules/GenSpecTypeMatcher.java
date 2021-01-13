package matching.modules;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import matching.Setting;
import matching.methods.MethodMatchingInfo;
import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.methods.MethodMatchingInfoFactory;
import util.Logger;

/**
 * Dieser Matcher achtet darauf, dass die Typen (Return- und Argumenttypen) der beiden Methoden auch Generelisierungen
 * bzw. Spezialisierungen von einander sein k�nnen.
 */

// TODO QUERSTION: Dieser Matcher geh�rt doch eigentlich auch in die Kategorie "PartlyTypeMatcher" oder?
// Zumindest, wenn ich Gen2Spec matchen m�chte. Dann kann es passieren, dass der Gen nur teilweise die Methoden des
// Spec
// erf�llt.
public class GenSpecTypeMatcher implements CombinableTypeMatcher {

  static int counter = 0;

  // Versuch: Cache der Wrapped-Pr�fungen
  // Grund: Im WrappedTypeMethodMatcher wird auch ein Cache verwendet und es ist sicherlich aus Performance-Sicht
  // sinnvoll auch hier einen Cache aufzubauen.
  Map<Class<?>[], Boolean> cachedGenSpecTypesChecks = new HashMap<>();

  @Override
  public boolean matchesType( Class<?> checkType, Class<?> queryType ) {
    // Versuch 1: �ber die Methode isAssignableFrom feststellen, ob die Typen voneinander erben.
    // Problem: native Typen erben nicht von Object
    // #####################Frage: Ist das wirklich ein Problem, oder ist das so korrekt????????#####################
    // return t1.isAssignableFrom( t2 ) || t2.isAssignableFrom( t1 );

    // Versuche 2: wie Versuch 1 mit zus�tzlicher Equals-Pr�fung, aufgrund des in Versuch 1 erw�hnten Problems
    // return t1.equals( Object.class ) || t2.equals( Object.class ) || t1.isAssignableFrom( t2 )
    // || t2.isAssignableFrom( t1 );

    // Versuch 3: wie Versuch 2 mit Cache
    Class<?>[] cacheKey = new Class<?>[] { checkType, queryType };
    if ( isCombinationCached( cacheKey ) ) {
      // false, weil die �berpr�fung noch nicht stattgefunden bzw. wenn sie bereits true ermittelt hatte, dann
      // w�re die
      // �berpr�fung bereits erfolgreich gewesen
      return getResultFromCache( cacheKey );
    }
    cachedGenSpecTypesChecks.put( cacheKey, null );
    boolean result = checkType.equals( Object.class ) || queryType.equals( Object.class )
        || checkType.isAssignableFrom( queryType )
        || queryType.isAssignableFrom( checkType );
    cachedGenSpecTypesChecks.put( cacheKey, result );
    return result;
  }

  private boolean isCombinationCached( Class<?>[] newCacheKey ) {
    // Hier ist die Richtung der gepr�ften Typen egal. Also
    for ( Class<?>[] cacheKey : cachedGenSpecTypesChecks.keySet() ) {
      if ( Objects.equals( cacheKey[0], newCacheKey[0] ) && Objects.equals( cacheKey[1], newCacheKey[1] ) ||
          Objects.equals( cacheKey[0], newCacheKey[1] ) && Objects.equals( cacheKey[1], newCacheKey[0] ) ) {
        return true;
      }
    }
    return false;
  }

  private boolean getResultFromCache( Class<?>[] newCacheKey ) {
    for ( Entry<Class<?>[], Boolean> cacheEntries : cachedGenSpecTypesChecks.entrySet() ) {
      Class<?>[] cachedKey = cacheEntries.getKey();
      if ( Objects.equals( cachedKey[0], newCacheKey[0] ) && Objects.equals( cachedKey[1], newCacheKey[1] ) ||
          Objects.equals( cachedKey[0], newCacheKey[1] ) && Objects.equals( cachedKey[1], newCacheKey[0] ) ) {
        return cacheEntries.getValue() == null ? false : cacheEntries.getValue();
      }
    }
    return false;
  }

  @Override
  public Collection<ModuleMatchingInfo> calculateTypeMatchingInfos( Class<?> checkType,
      Class<?> queryType ) {
    Logger.infoF( "calculate TypeMatchingInfos: %s -> %s", queryType, checkType );
    int c = ++counter;
    Logger.infoF( "start calculation: %d", c );
    Collection<ModuleMatchingInfo> result = new ArrayList<>();
    ModuleMatchingInfoFactory factory = new ModuleMatchingInfoFactory( checkType, queryType );
    if ( checkType.equals( queryType ) ) {
      Logger.infoF( "assumtion: %s == %s", queryType, checkType );
      Logger.infoF( "finish calculation: %d", c );
      result = Collections.singletonList( factory.create() );
    }
    // f�r primitive Typen, die auch als Object verwendet werden k�nnen
    else if ( ( checkType.isPrimitive() && queryType.equals( Object.class ) )
        || ( queryType.isPrimitive() && checkType.equals( Object.class ) ) ) {
      Logger.infoF( "assumtion: Object > primitiv" );
      Logger.infoF( "finish calculation: %d", c );
      result = Collections.singletonList( factory.create() );
    }
    else if ( queryType.isAssignableFrom( checkType )
    // Wurde nur f�r native Typen gemacht
    // || queryType.equals( Object.class )
    ) {
      // queryType > checkType
      // Gen: queryType
      // Spec: checkType

      // Versuch 1
      // TODO ich bin mir unsicher, ob die �bergabe von this an dieser Stelle korrekt ist, oder ob die
      // ModuleMatchingInfos an dieser Stelle nicht auch durch den kombinierten Matcher ermittelt werden m�ssen. Das
      // dauert aber sehr lange.
      // return new ModuleMatcher<>( queryType, this ).calculateMatchingInfos( checkType );

      // Versuch 2
      Logger.infoF( "assumtion: %s > %s", queryType, checkType );
      Map<Method, Collection<MethodMatchingInfo>> methodMatchingInfos = createMethodMatchingInfoForGen2SpecMapping(
          queryType,
          checkType );
      result = factory.createFromMethodMatchingInfos( methodMatchingInfos );
    }
    else if ( checkType.isAssignableFrom( queryType )

    // Wurde nur f�r native Typen gemacht
    // || checkType.equals( Object.class )
    ) {
      // queryType < checkType
      // Gen: checkType
      // Spec: queryType

      Logger.infoF( "assumtion: %s < %s", queryType, checkType );
      result = Collections.singletonList( factory.create() );
    }
    Logger.infoF( "finish calculation: %d", c );
    return result;
  }

  private Map<Method, Collection<MethodMatchingInfo>> createMethodMatchingInfoForGen2SpecMapping( Class<?> genType,
      Class<?> specType ) {
    Map<Method, Collection<MethodMatchingInfo>> matchingInfos = new HashMap<>();

    // Es wird davon ausgegangen, dass nur f�r die �berschreibbaren Methoden MatchingInfos erzeugt werden k�nnen.
    // Und f�r jede �berschreibbare Methode muss eine MatchingInfo erzeugt werden.
    Collection<Method> genMethods = getOverrideableMethods( genType );
    // Methoden, die im speziellen Typ deklariert aber nicht �berschrieben wurden, k�nnen keine passenede Methode im
    // Supertyp haben.
    Method[] specMethods = specType.getDeclaredMethods();
    for ( Method specM : specMethods ) {
      Method genM = getOriginalMethod( specM, genType );
      if ( genM != null ) {
        genMethods.remove( genM );
        Logger.infoF( "matching methods found: %s === %s", specM, genM );
        MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( specM, genM );
        // Hier kann wirklich der gleiche TypeMatcher verwendet werden, weil f�r �berschriebene Methode bestimmte
        // Regeln
        // gelten.
        // Regel 1: Der Returntype kann spezieller werden. Liskov l�sst gr��en. (Kovarianz)
        ModuleMatchingInfo returnTypeMatchingInfo = calculateTypeMatchingInfos( genM.getReturnType(),
            specM.getReturnType() ).iterator().next();

        // Regel 2: Die Argumente k�nnen allgemeiner werden. Liskov l�sst gr��en (Kontravarianz)
        Map<ParamPosition, Collection<ModuleMatchingInfo>> argumentTypeMatchingInfos = calculateArgumentTypesMatchingInfos(
            specM.getParameterTypes(), genM.getParameterTypes() );
        matchingInfos.put( genM,

            factory.createFromTypeMatchingInfos( Collections.singletonList( returnTypeMatchingInfo ),
                Collections.singletonList( argumentTypeMatchingInfos ) ) );
      }
    }

    for ( Method genM : genMethods ) {
      MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( genM, genM );
      matchingInfos.put( genM, Collections.singletonList( factory.create( null, new HashMap<>() ) ) );
    }

    return matchingInfos;
  }

  private Map<ParamPosition, Collection<ModuleMatchingInfo>> calculateArgumentTypesMatchingInfos(
      Class<?>[] checkATs, Class<?>[] queryATs ) {
    Map<ParamPosition, Collection<ModuleMatchingInfo>> matchingMap = new HashMap<>();
    for ( int i = 0; i < checkATs.length; i++ ) {
      Class<?> checkAT = checkATs[i];
      Class<?> queryAT = queryATs[i];
      Collection<ModuleMatchingInfo> infos = calculateTypeMatchingInfos( checkAT, queryAT );
      matchingMap.put( new ParamPosition( i, i ), infos );
    }

    return matchingMap;
  }

  private Collection<Method> getOverrideableMethods( Class<?> genType ) {
    Method[] methods = genType.getDeclaredMethods();
    return Stream.of( methods ).filter( m -> !Modifier.isPrivate( m.getModifiers() ) ).collect( Collectors.toList() );
  }

  public static Method getOriginalMethod( final Method myMethod, Class<?> superType ) {
    if ( superType.equals( Object.class ) ) {
      return null;
    }
    try {
      Method superMethod = superType.getDeclaredMethod( myMethod.getName(),
          myMethod.getParameterTypes() );
      return !Modifier.isPrivate( superMethod.getModifiers() ) ? superMethod : null;
    }
    catch ( NoSuchMethodException e ) {
      return null;
    }
  }

  @Override
  public double getTypeMatcherRate() {
    return Setting.GEN_SPEC_TYPE_MATCHER_BASE_RATING;
  }

}
