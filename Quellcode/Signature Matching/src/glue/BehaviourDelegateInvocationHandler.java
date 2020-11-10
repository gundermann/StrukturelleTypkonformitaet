package glue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import matching.methods.MethodMatchingInfo;
import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.modules.ModuleMatchingInfo;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class BehaviourDelegateInvocationHandler implements MethodInterceptor, InvocationHandler {

  private final Object component;

  private final ModuleMatchingInfo matchingInfos;

  private final String targetDelegationComponent;

  public BehaviourDelegateInvocationHandler( Object component, ModuleMatchingInfo matchingInfos ) {
    this.component = component;
    this.matchingInfos = matchingInfos;
    this.targetDelegationComponent = matchingInfos.getTargetDelegateAttribute();
  }

  @Override
  public Object invoke( Object methodProxy, Method method, Object[] args ) throws Throwable {
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
    // (Hierbei handelt es sich um Methoden, dei von jedem Objekt erfï¿½llt werden und
    // fï¿½r die aufgrund einer fehlenden
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
    System.out.println( String.format( "convert type %s -> %s",
        moduleMatchingInfo.getSource().getName(), moduleMatchingInfo.getTarget().getName() ) );
    if ( moduleMatchingInfo.getMethodMatchingInfos().isEmpty()
        && moduleMatchingInfo.getTargetDelegateAttribute() == null
        && moduleMatchingInfo.getSourceDelegateAttribute() == null ) {
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
      Field sourceField = moduleMatchingInfo.getSource()
          .getDeclaredField( moduleMatchingInfo.getSourceDelegateAttribute() );
      // Field sourceField = getDeclaredFieldFromClass( type.getClass(), moduleMatchingInfo.getSourceDelegateAttribute()
      // );
      sourceField.setAccessible( true );
      return sourceField.get( type );
    }
    catch ( NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e ) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Es muss zwischen echten Klassen und Proxies unterschieden werden. Bei einem Proxy ist das Feld nämlich in der
   * Oberklasse zu suchen
   *
   * @param targetClass
   * @param fieldName
   * @return
   * @throws SecurityException
   * @throws NoSuchFieldException
   */
  private Field getDeclaredFieldFromClass( Class<? extends Object> searchClass, String fieldName )
      throws NoSuchFieldException, SecurityException {
    if ( Proxy.isProxyClass( searchClass ) ) {
      return searchClass.getSuperclass().getDeclaredField( fieldName );
    }
    return searchClass.getDeclaredField( fieldName );
  }

  private Object getTargetRefFromModuleMatchingInfo( Object o, ModuleMatchingInfo moduleMatchingInfo ) {
    if ( moduleMatchingInfo.getTargetDelegateAttribute() == null ) {
      return o;
    }
    try {
      Field targetField = o.getClass().getDeclaredField( moduleMatchingInfo.getSourceDelegateAttribute() );
      targetField.setAccessible( true );
      return targetField.get( o );
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

  @Override
  public Object intercept( Object callObject, Method method, Object[] args, MethodProxy methodProxy ) throws Throwable {
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
    if ( matchingInfos.getTargetDelegateAttribute() == null ) {
      return method.invoke( component, args );
    }
    return methodProxy.invokeSuper( callObject, args );
  }

}
