package matching.methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.modules.ModuleMatchingInfo;
import matching.modules.TypeMatcher;
import matching.modules.WrappedTypeMatcher;

/**
 * Dieser Matcher beachtet, dass die Typen (Return- und Argumenttypen) einer der beiden Methoden in einem Typ der
 * anderen Methode enthalten sein können (Wrapper).
 */
public class WrappedTypeMethodMatcher implements MethodMatcher {

  private final TypeMatcher typeMatcher;

  public WrappedTypeMethodMatcher( Supplier<TypeMatcher> innerMethodMatcherSupplier ) {
    this.typeMatcher = new WrappedTypeMatcher( innerMethodMatcherSupplier );
  }

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
    if ( !typeMatcher.matchesType( ms1.getReturnType(), ms2.getReturnType() ) ) {
      return false;
    }
    for ( int i = 0; i < ms1.getSortedArgumentTypes().length; i++ ) {
      if ( !typeMatcher.matchesType( ms1.getSortedArgumentTypes()[i], ms2.getSortedArgumentTypes()[i] ) ) {
        return false;
      }
    }
    return true;
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

  @Override
  public Collection<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod ) {
    if ( !matches( checkMethod, queryMethod ) ) {
      return new ArrayList<>();
    }
    MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( checkMethod, queryMethod );
    Collection<ModuleMatchingInfo> returnTypeMatchingInfos = typeMatcher.calculateTypeMatchingInfos(
        queryMethod.getReturnType(),
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
      Collection<ModuleMatchingInfo> infos = typeMatcher.calculateTypeMatchingInfos( checkAT, queryAT );
      matchingMap.put( new ParamPosition( i, i ), infos );
    }
    return Collections.singletonList( matchingMap );
  }

}
