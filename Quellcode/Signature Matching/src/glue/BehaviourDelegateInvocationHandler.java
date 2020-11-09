package glue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import matching.methods.MethodMatchingInfo;
import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.modules.ModuleMatchingInfo;

public class BehaviourDelegateInvocationHandler implements InvocationHandler {

  private Object component;

  private ModuleMatchingInfo matchingInfos;

  public BehaviourDelegateInvocationHandler( Object component, ModuleMatchingInfo matchingInfos ) {
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
      ModuleMatchingInfo returnTypeMatchingInfo = methodMatchingInfo.getReturnTypeMatchingInfo();
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

  private Object[] convertArgs( Object[] args, Map<ParamPosition, ModuleMatchingInfo> argMMI ) {
    if ( args == null ) {
      return null;
    }
    Object[] convertedArgs = new Object[args.length];
    for ( int i = 0; i < args.length; i++ ) {
      Entry<ParamPosition, ModuleMatchingInfo> moduleMatchingInfoEntry = getParameterWithRepectToPosition( i, argMMI );
      if ( moduleMatchingInfoEntry != null && moduleMatchingInfoEntry.getValue() != null
          && moduleMatchingInfoEntry.getKey() != null ) {
        ParamPosition paramPosition = moduleMatchingInfoEntry.getKey();
        ModuleMatchingInfo moduleMatchingInfo = moduleMatchingInfoEntry.getValue();
        Object convertedArg = convertType( args[paramPosition.getSourceParamPosition()],
            moduleMatchingInfo );
        convertedArgs[paramPosition.getTargetParamPosition()] = convertedArg;
      }
      else {
        // Keine Konvertierung notwendig
        convertedArgs[i] = args[i];
      }
    }
    return convertedArgs;
  }

  private Entry<ParamPosition, ModuleMatchingInfo> getParameterWithRepectToPosition( int sourceParamPosition,
      Map<ParamPosition, ModuleMatchingInfo> argMMI ) {
    return argMMI.entrySet().stream().filter( e -> e.getKey().getSourceParamPosition().equals( sourceParamPosition ) )
        .findFirst().orElse( null );
  }

  /**
   * Rekursiver Aufruf des gesamten Konvertierungsprozesses
   *
   * @param sourceType
   * @param moduleMatchingInfo
   * @return
   */
  @SuppressWarnings( "unchecked" )
  private <RT> RT convertType( Object sourceType, ModuleMatchingInfo moduleMatchingInfo ) {
    System.out.println( String.format( "convert type %s <- %s", moduleMatchingInfo.getTarget().getName(),
        moduleMatchingInfo.getSource().getName() ) );
    if ( moduleMatchingInfo.getMethodMatchingInfos().isEmpty() ) {
      return (RT) sourceType;
    }
    Object source = getSourceRefFromModuleMatchingInfo( sourceType, moduleMatchingInfo );
    return new SignatureMatchingTypeConverter<>( (Class<RT>) moduleMatchingInfo.getTarget() ).convert( source,
        moduleMatchingInfo );
  }

  private Object getSourceRefFromModuleMatchingInfo( Object type, ModuleMatchingInfo moduleMatchingInfo ) {
    if ( moduleMatchingInfo.getSourceDelegateAttribute() == null ) {
      return type;
    }
    try {
      Field sourceField = type.getClass().getField( moduleMatchingInfo.getSourceDelegateAttribute() );
      return sourceField.get( type );
    }
    catch ( NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e ) {
      e.printStackTrace();
      return null;
    }
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
    for ( Entry<ParamPosition, ModuleMatchingInfo> argMMIEntry : mmi.getArgumentTypeMatchingInfos().entrySet() ) {
      if ( method.getParameterCount() <= argMMIEntry.getKey().getSourceParamPosition() ) {
        throw new RuntimeException( "wrong parameter count" );
      }
      Class<?> parameterType = method.getParameterTypes()[argMMIEntry.getKey().getSourceParamPosition()];
      if ( !parameterType.equals( argMMIEntry.getValue().getSource() ) ) {
        return false;
      }
    }
    return true;
  }

}
