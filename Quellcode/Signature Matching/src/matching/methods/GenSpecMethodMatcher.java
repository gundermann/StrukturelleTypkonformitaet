package matching.methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import matching.types.TypeMatchingInfo;
import matching.types.TypeMatchingInfoFactory;

/**
 * Dieser Matcher achtet darauf, dass die Typen (Return- und Argumenttypen) der beiden Methoden auch Generelisierungen
 * bzw. Spezialisierungen von einander sein k�nnen.
 */
public class GenSpecMethodMatcher implements MethodMatcher {

  // Versuch: Cache der Wrapped-Pr�fungen
  // Grund: Im WrappedTypeMethodMatcher wird auch ein Cache verwendet und es ist sicherlich aus Performance-Sicht
  // sinnvoll auch hier einen Cache aufzubauen.
  Map<Class<?>[], Boolean> cachedGenSpecTypesChecks = new HashMap<>();

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
    if ( !machtesGenSpecType( ms1.getReturnType(), ms2.getReturnType() ) ) {
      return false;
    }
    for ( int i = 0; i < ms1.getSortedArgumentTypes().length; i++ ) {
      if ( !machtesGenSpecType( ms1.getSortedArgumentTypes()[i], ms2.getSortedArgumentTypes()[i] ) ) {
        return false;
      }
    }
    return true;
  }

  boolean machtesGenSpecType( Class<?> t1, Class<?> t2 ) {
    // Versuch 1: �ber die Methode isAssignableFrom feststellen, ob die Typen voneinander erben.
    // Problem: native Typen erben nicht von Object
    // #####################Frage: Ist das wirklich ein Problem, oder ist das so korrekt????????#####################
    // return t1.isAssignableFrom( t2 ) || t2.isAssignableFrom( t1 );

    // Versuche 2: wie Versuch 1 mit zus�tzlicher Equals-Pr�fung, aufgrund des in Versuch 1 erw�hnten Problems
    // return t1.equals( Object.class ) || t2.equals( Object.class ) || t1.isAssignableFrom( t2 )
    // || t2.isAssignableFrom( t1 );

    // Versuch 3: wie Versuch 2 mit Cache
    Class<?>[] cacheKey = new Class<?>[] { t1, t2 };
    if ( isCombinationCached( cacheKey ) ) {
      // false, weil die �berpr�fung noch nicht stattgefunden bzw. wenn sie bereits true ermittelt hatte, dann w�re die
      // �berpr�fung bereits erfolgreich gewesen
      return getResultFromCache( cacheKey );
    }
    cachedGenSpecTypesChecks.put( cacheKey, null );
    boolean result = t1.equals( Object.class ) || t2.equals( Object.class ) || t1.isAssignableFrom( t2 )
        || t2.isAssignableFrom( t1 );
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
  public Set<MethodMatchingInfo> calculateMatchingInfos( Method source, Method target ) {
    MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( source, target );
    MethodStructure sourceStruct = MethodStructure.createFromDeclaredMethod( source );
    MethodStructure targetStruct = MethodStructure.createFromDeclaredMethod( target );
    Collection<TypeMatchingInfo<?, ?>> returnTypeMatchingInfos = calculateReturnTypeMatchingInfos(
        sourceStruct.getReturnType(), targetStruct.getReturnType() );
    Collection<Map<Integer, TypeMatchingInfo<?, ?>>> argumentTypesMatchingInfos = calculateArgumentTypesMatchingInfos(
        sourceStruct.getSortedArgumentTypes(), targetStruct.getSortedArgumentTypes() );
    return factory.createFromTypeMatchingInfos( returnTypeMatchingInfos, argumentTypesMatchingInfos );
  }

  private Collection<Map<Integer, TypeMatchingInfo<?, ?>>> calculateArgumentTypesMatchingInfos(
      Class<?>[] sourceATs, Class<?>[] targetATs ) {
    Map<Integer, TypeMatchingInfo<?, ?>> matchingMap = new HashMap<>();
    for ( int i = 0; i < sourceATs.length; i++ ) {
      Class<?> sourceAT = sourceATs[i];
      Class<?> targetAT = targetATs[i];
      TypeMatchingInfoFactory<?, ?> factory = new TypeMatchingInfoFactory<>( sourceAT, targetAT );
      if ( sourceAT.equals( targetAT ) ) {
        matchingMap.put( i, factory.create() );
      }
      else if ( sourceAT.isAssignableFrom( targetAT ) || sourceAT.equals( Object.class ) ) {
        // Gen: sourceAT
        // Spec: targetAT
        matchingMap.put( i, factory.create() );
      }
      else if ( targetAT.isAssignableFrom( sourceAT ) || targetAT.equals( Object.class ) ) {
        // Gen: targetAT
        // Spec: sourceAT
        matchingMap.put( i, factory.create() );
      }
    }

    return Collections.singletonList( matchingMap );
  }

  private Collection<TypeMatchingInfo<?, ?>> calculateReturnTypeMatchingInfos( Class<?> sourceRT,
      Class<?> targetRT ) {
    TypeMatchingInfoFactory<?, ?> factory = new TypeMatchingInfoFactory<>( sourceRT, targetRT );
    if ( sourceRT.equals( targetRT ) ) {
      return Collections.singletonList( factory.create() );
    }
    else if ( sourceRT.isAssignableFrom( targetRT ) || sourceRT.equals( Object.class ) ) {
      // Gen: sourceRT
      // Spec: targetRT
      return Collections.singletonList( factory.create() );
    }
    else if ( targetRT.isAssignableFrom( sourceRT ) || targetRT.equals( Object.class ) ) {
      // Gen: targetRT
      // Spec: sourceRT
      return Collections.singletonList( factory.create() );
    }
    return new ArrayList<>();
  }
}
