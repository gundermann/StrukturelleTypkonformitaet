package matching.methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import matching.modules.ModuleMatcher;
import matching.modules.ModuleMatchingInfo;
import matching.modules.ModuleMatchingInfoFactory;

/**
 * Dieser Matcher achtet darauf, dass die Typen (Return- und Argumenttypen) der beiden Methoden auch Generelisierungen
 * bzw. Spezialisierungen von einander sein können.
 */
public class GenSpecMethodMatcher implements MethodMatcher {

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
  public Set<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod ) {
    if ( !matches( checkMethod, queryMethod ) ) {
      return new HashSet<>();
    }
    MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( checkMethod, queryMethod );
    MethodStructure checkStruct = MethodStructure.createFromDeclaredMethod( checkMethod );
    MethodStructure queryStruct = MethodStructure.createFromDeclaredMethod( queryMethod );
    Collection<?> returnTypeMatchingInfos = calculateTypeMatchingInfos(
        queryStruct.getReturnType(), checkStruct.getReturnType() );
    Map<Integer, Collection<ModuleMatchingInfo>> argumentTypesMatchingInfos = calculateArgumentTypesMatchingInfos(
        checkStruct.getSortedArgumentTypes(), queryStruct.getSortedArgumentTypes() );
    return factory.createFromTypeMatchingInfos( (Collection<ModuleMatchingInfo>) returnTypeMatchingInfos,
        argumentTypesMatchingInfos );
  }

  private Map<Integer, Collection<ModuleMatchingInfo>> calculateArgumentTypesMatchingInfos(
      Class<?>[] checkATs, Class<?>[] queryATs ) {
    Map<Integer, Collection<ModuleMatchingInfo>> matchingMap = new HashMap<>();
    for ( int i = 0; i < checkATs.length; i++ ) {
      Class<?> checkAT = checkATs[i];
      Class<?> queryAT = queryATs[i];
      Collection<?> infos = calculateTypeMatchingInfos( checkAT, queryAT );
      matchingMap.put( i, (Collection<ModuleMatchingInfo>) infos );
    }

    return matchingMap;
  }

  @Override
  public Collection<ModuleMatchingInfo> calculateTypeMatchingInfos( Class<?> checkType,
      Class<?> queryType ) {
    ModuleMatchingInfoFactory factory = new ModuleMatchingInfoFactory( checkType, queryType );
    if ( checkType.equals( queryType ) ) {
      return Collections.singletonList( factory.create() );
    }
    else if ( queryType.isAssignableFrom( checkType )
    // Wurde nur für native Typen gemacht
    // || queryType.equals( Object.class )
    ) {
      // queryType > checkType
      // Gen: queryType
      // Spec: checkType
      return new ModuleMatcher<>( queryType ).calculateMatchingInfos( checkType );
    }
    else if ( checkType.isAssignableFrom( queryType )
    // Wurde nur für native Typen gemacht
    // || checkType.equals( Object.class )
    ) {
      // queryType < checkType
      // Gen: checkType
      // Spec: queryType
      return Collections.singletonList( factory.create() );
    }
    return new ArrayList<>();
  }

}
