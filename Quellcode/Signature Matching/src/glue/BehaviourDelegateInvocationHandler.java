package glue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import matching.methods.MethodMatchingInfo;
import matching.modules.ModuleMatchingInfo;

public class BehaviourDelegateInvocationHandler<T> implements InvocationHandler {

  private Object component;

  private ModuleMatchingInfo<T> matchingInfos;

  public BehaviourDelegateInvocationHandler( Object component, ModuleMatchingInfo<T> matchingInfos ) {
    this.component = component;
    this.matchingInfos = matchingInfos;
  }

  @Override
  public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable {
    Optional<MethodMatchingInfo> optMatchingInfo = getMethodMatchingInfo( method );
    if ( optMatchingInfo.isPresent() ) {
      MethodMatchingInfo methodMatchingInfo = optMatchingInfo.get();
      Method targetMethod = methodMatchingInfo.getTarget();
      Object[] convertedArgs = convertArgs( args, methodMatchingInfo.getArgumentTypeMatchingInfos() );
      Object returnValue = targetMethod.invoke( component, convertedArgs );
      ModuleMatchingInfo<?> returnTypeMatchingInfo = methodMatchingInfo.getReturnTypeMatchingInfo();
      if ( returnTypeMatchingInfo == null ) {
        return returnValue;
      }
      // Bei einem allgemeineren returnValue des Targets (Target.retrunValue >
      // Source.returnValue) muss der ReturnType
      // ebenfalls gemocked werden
      return convertType( returnValue, returnTypeMatchingInfo );
    }

    // Default-Methode
    // (Hierbei handelt es sich um Methoden, dei von jedem Objekt erf�llt werden und
    // f�r die aufgrund einer fehlenden
    // Implementierung keine MatchingInfo existiert)
    return method.invoke( component, args );
  }

  private Object[] convertArgs( Object[] args, Map<Integer, ModuleMatchingInfo<?>> argMMI ) {
    if ( args == null ) {
      return null;
    }
    Object[] convertedArgs = new Object[args.length];
    for ( int i = 0; i < args.length; i++ ) {
      if ( argMMI.containsKey( i ) ) {
        ModuleMatchingInfo<?> moduleMatchingInfo = argMMI.get( i );
        Object convertedArg = convertType( args[i], moduleMatchingInfo );
        convertedArgs[i] = convertedArg;
      }
      else {
        // Keine Konvertierung notwendig
        convertedArgs[i] = args[i];
      }
    }
    return convertedArgs;
  }

  /**
   * Rekursiver Aufruf des gesamten Konvertierungsprozesses
   *
   * @param returnValue
   * @param moduleMatchingInfo
   * @return
   */
  private <RT> RT convertType( Object returnValue, ModuleMatchingInfo<RT> moduleMatchingInfo ) {
    System.out.println( String.format( "convert type %s -> %s", moduleMatchingInfo.getTarget().getName(),
        moduleMatchingInfo.getSource().getName() ) );
    if ( moduleMatchingInfo.getMethodMatchingInfos().isEmpty() ) {
      return (RT) returnValue;
    }
    return new SignatureMatchingTypeConverter<>( moduleMatchingInfo.getSource() ).convert( returnValue,
        moduleMatchingInfo );
  }

  private Optional<MethodMatchingInfo> getMethodMatchingInfo( Method method ) {
    for ( MethodMatchingInfo mmi : matchingInfos.getMethodMatchingInfos() ) {
      if ( mmi.getSource().getName().equals( method.getName() ) && agrumentsMatches( mmi, method ) ) {
        return Optional.of( mmi );
      }
    }
    return Optional.empty();
  }

  private boolean agrumentsMatches( MethodMatchingInfo mmi, Method method ) {
    for ( Entry<Integer, ModuleMatchingInfo<?>> argMMIEntry : mmi.getArgumentTypeMatchingInfos().entrySet() ) {
      if ( method.getParameterCount() <= argMMIEntry.getKey() ) {
        throw new RuntimeException( "wrong parameter count" );
      }
      Class<?> parameterType = method.getParameterTypes()[argMMIEntry.getKey()];
      if ( !parameterType.equals( argMMIEntry.getValue().getTarget() ) ) {
        return false;
      }
    }
    return true;
  }

}
