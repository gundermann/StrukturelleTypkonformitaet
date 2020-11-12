package matching.methods;

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

import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.modules.ModuleMatchingInfo;
import matching.modules.ModuleMatchingInfoFactory;
import util.Logger;

/**
 * Dieser Matcher achtet darauf, dass die Typen (Return- und Argumenttypen) der beiden Methoden auch Generelisierungen
 * bzw. Spezialisierungen von einander sein können.
 */
public class GenSpecMethodMatcher implements MethodMatcher {

  static int counter = 0;

  // Versuch: Cache der Wrapped-Prüfungen
  // Grund: Im WrappedTypeMethodMatcher wird auch ein Cache verwendet und es ist sicherlich aus Performance-Sicht
  // sinnvoll auch hier einen Cache aufzubauen.
  Map<Class<?>[], Boolean> cachedGenSpecTypesChecks = new HashMap<>();

  @Override
  public boolean matches( Method checkMethod, Method queryMethod ) {
    MethodStructure cms1 = MethodStructure.createFromDeclaredMethod( checkMethod );
    MethodStructure qms2 = MethodStructure.createFromDeclaredMethod( queryMethod );
    return matches( cms1, qms2 );
  }

  private boolean matches( MethodStructure cms1, MethodStructure qms2 ) {
    if ( cms1.getSortedArgumentTypes().length != qms2.getSortedArgumentTypes().length ) {
      return false;
    }
    if ( !matchesType( cms1.getReturnType(), qms2.getReturnType() ) ) {
      return false;
    }
    for ( int i = 0; i < cms1.getSortedArgumentTypes().length; i++ ) {
      if ( !matchesType( cms1.getSortedArgumentTypes()[i], qms2.getSortedArgumentTypes()[i] ) ) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean matchesType( Class<?> checkType, Class<?> queryType ) {
    // Versuch 1: Über die Methode isAssignableFrom feststellen, ob die Typen voneinander erben.
    // Problem: native Typen erben nicht von Object
    // #####################Frage: Ist das wirklich ein Problem, oder ist das so korrekt????????#####################
    // return t1.isAssignableFrom( t2 ) || t2.isAssignableFrom( t1 );

    // Versuche 2: wie Versuch 1 mit zusätzlicher Equals-Prüfung, aufgrund des in Versuch 1 erwähnten Problems
    // return t1.equals( Object.class ) || t2.equals( Object.class ) || t1.isAssignableFrom( t2 )
    // || t2.isAssignableFrom( t1 );

    // Versuch 3: wie Versuch 2 mit Cache
    Class<?>[] cacheKey = new Class<?>[] { checkType, queryType };
    if ( isCombinationCached( cacheKey ) ) {
      // false, weil die Überprüfung noch nicht stattgefunden bzw. wenn sie bereits true ermittelt hatte, dann wäre die
      // Überprüfung bereits erfolgreich gewesen
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
    // Hier ist die Richtung der geprüften Typen egal. Also
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
  public Collection<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod ) {
    if ( !matches( checkMethod, queryMethod ) ) {
      return new ArrayList<>();
    }
    MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( checkMethod, queryMethod );
    MethodStructure checkStruct = MethodStructure.createFromDeclaredMethod( checkMethod );
    MethodStructure queryStruct = MethodStructure.createFromDeclaredMethod( queryMethod );
    Collection<ModuleMatchingInfo> returnTypeMatchingInfos = calculateTypeMatchingInfos(
        queryStruct.getReturnType(), checkStruct.getReturnType() );
    Map<ParamPosition, Collection<ModuleMatchingInfo>> argumentTypesMatchingInfos = calculateArgumentTypesMatchingInfos(
        checkStruct.getSortedArgumentTypes(), queryStruct.getSortedArgumentTypes() );
    return factory.createFromTypeMatchingInfos( returnTypeMatchingInfos,
        Collections.singletonList( argumentTypesMatchingInfos ) );
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
    // für primitive Typen, die auch als Object verwendet werden können
    else if ( ( checkType.isPrimitive() && queryType.equals( Object.class ) )
        || ( queryType.isPrimitive() && checkType.equals( Object.class ) ) ) {
      Logger.infoF( "assumtion: Object > primitiv" );
      Logger.infoF( "finish calculation: %d", c );
      result = Collections.singletonList( factory.create() );
    }
    else if ( queryType.isAssignableFrom( checkType )
    // Wurde nur für native Typen gemacht
    // || queryType.equals( Object.class )
    ) {
      // queryType > checkType
      // Gen: queryType
      // Spec: checkType

      // Versuch 1
      // TODO ich bin mir unsicher, ob die Übergabe von this an dieser Stelle korrekt ist, oder ob die
      // ModuleMatchingInfos an dieser Stelle nicht auch durch den kombinierten Matcher ermittelt werden müssen. Das
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

    // Wurde nur für native Typen gemacht
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
    // Es wird davon ausgegangen, dass nur für die überschreibbaren Methoden MatchingInfos erzeugt werden können.
    Method[] genMethods = getOverrideableMethods( genType );
    // Methoden, die im speziellen Typ deklariert aber nicht überschrieben wurden, können keine passenede Methode im
    // Supertyp haben.
    Method[] specMethods = specType.getDeclaredMethods();
    for ( Method genM : genMethods ) {
      for ( Method specM : specMethods ) {
        if ( specM.getName().equals( genM.getName() ) && matches( specM, genM ) ) {
          Logger.infoF( "matching methods found: %s === %s", specM, genM );
          MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( specM, genM );
          // Der Returntype kann spezieller werden. Liskov lässt grüßen. (Kovarianz)
          ModuleMatchingInfo returnTypeMatchingInfo = calculateTypeMatchingInfos( genM.getReturnType(),
              specM.getReturnType() ).iterator().next();

          // Die Argumente können allgemeiner werden. Liskov lässt grüßen (Kontravarianz)
          Map<ParamPosition, Collection<ModuleMatchingInfo>> argumentTypeMatchingInfos = calculateArgumentTypesMatchingInfos(
              specM.getParameterTypes(), genM.getParameterTypes() );
          matchingInfos.put( genM,

              factory.createFromTypeMatchingInfos( Collections.singletonList( returnTypeMatchingInfo ),
                  Collections.singletonList( argumentTypeMatchingInfos ) ) );
        }
      }
    }

    return matchingInfos;
  }

  private Method[] getOverrideableMethods( Class<?> genType ) {
    Method[] methods = genType.getMethods();
    return Stream.of( methods ).filter( m -> !Modifier.isPrivate( m.getModifiers() ) ).collect( Collectors.toList() )
        .toArray( new Method[] {} );
  }

}
