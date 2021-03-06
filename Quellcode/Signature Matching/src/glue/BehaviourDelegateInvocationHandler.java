package glue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import matching.methods.MethodMatchingInfo;
import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.modules.ModuleMatchingInfo;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import util.Logger;

public class BehaviourDelegateInvocationHandler implements MethodInterceptor, InvocationHandler {

  private final Map<Object, Collection<MethodMatchingInfo>> components2MatchingInfo;

  public BehaviourDelegateInvocationHandler( Object component, Collection<MethodMatchingInfo> methodMatchingInfos ) {
    this.components2MatchingInfo = new HashMap<>();
    this.components2MatchingInfo.put( component, methodMatchingInfos );
  }

  public BehaviourDelegateInvocationHandler( Map<Object, Collection<MethodMatchingInfo>> components2MatchingInfo ) {
    this.components2MatchingInfo = components2MatchingInfo;
  }

  @Override
  public Object intercept( Object callObject, Method method, Object[] args, MethodProxy methodProxy ) throws Throwable {
    Optional<ComponentWithMatchingInfo> optMatchingInfo = getMethodMatchingInfo( method );
    if ( optMatchingInfo.isPresent() ) {
      return invokeOnComponentWithMatchingInfo( optMatchingInfo.get(), args );
    }
    return methodProxy.invokeSuper( callObject, args );
  }

  @Override
  public Object invoke( Object methodProxy, Method method, Object[] args ) throws Throwable {
    Optional<ComponentWithMatchingInfo> optMatchingInfo = getMethodMatchingInfo( method );
    if ( optMatchingInfo.isPresent() ) {
      return invokeOnComponentWithMatchingInfo( optMatchingInfo.get(), args );
    }

    // Default-Methode
    // (Hierbei handelt es sich um Methoden, dei von jedem Objekt erf�llt werden und
    // f�r die aufgrund einer fehlenden
    // Implementierung keine MatchingInfo existiert)
    return method.invoke( getSingleComponent(), args );
  }

  private Object getSingleComponent() {
    if ( components2MatchingInfo.size() == 1 ) {
      return components2MatchingInfo.keySet().iterator().next();
    }
    return null;
  }

  private Object invokeOnComponentWithMatchingInfo( ComponentWithMatchingInfo component,
      Object[] args )
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    MethodMatchingInfo methodMatchingInfo = component.getMatchingInfo();
    Method targetMethod = methodMatchingInfo.getTarget();
    Object[] convertedArgs = convertArgs( args, methodMatchingInfo.getArgumentTypeMatchingInfos() );
    Object returnValue = targetMethod.invoke( component.getComponent(), convertedArgs );
    ModuleMatchingInfo returnTypeMatchingInfo = methodMatchingInfo.getReturnTypeMatchingInfo();
    if ( returnTypeMatchingInfo == null ) {
      return returnValue;
    }
    // Bei einem allgemeineren returnValue des Targets (Target.retrunValue >
    // Source.returnValue) muss der ReturnType
    // ebenfalls gemocked werden

    return convertType( returnValue, returnTypeMatchingInfo );
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
    Logger.info( String.format( "convert type %s -> %s",
        moduleMatchingInfo.getSource().getName(), moduleMatchingInfo.getTarget().getName() ) );
    // Wenn das zu konvertierende Objekt null ist, dann kann dies auch gleich zur�ckgegeben werden, da null-Objekte
    // keinen speziellen Typ haben
    // if ( sourceType == null || moduleMatchingInfo.getMethodMatchingInfos().isEmpty()
    // && moduleMatchingInfo.getTargetDelegate() == null
    // && moduleMatchingInfo.getSourceDelegate() == null ) {
    // return (RT) sourceType;
    // }

    if ( sourceType == null ) {
      return (RT) sourceType;
    }

    Object source = sourceType;
    // if ( moduleMatchingInfo.getSourceDelegate() != null ) {
    // source = moduleMatchingInfo.getSourceDelegate().apply( sourceType );
    // }
    return new SingleTypeConverter<>( (Class<RT>) moduleMatchingInfo.getTarget(),
        moduleMatchingInfo.getConverterCreator() ).convert( source,
            moduleMatchingInfo );
  }

  private Optional<ComponentWithMatchingInfo> getMethodMatchingInfo( Method method ) {
    for ( Entry<Object, Collection<MethodMatchingInfo>> entry : components2MatchingInfo.entrySet() ) {
      for ( MethodMatchingInfo mmi : entry.getValue() ) {
        if ( mmi.getSource().getName().equals( method.getName() ) && argumentsMatches( mmi, method ) ) {
          return Optional.of( new ComponentWithMatchingInfo( entry.getKey(), mmi ) );
        }
      }
    }
    return Optional.empty();
  }

  private boolean argumentsMatches( MethodMatchingInfo mmi, Method method ) {
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

  private static class ComponentWithMatchingInfo {
    private final Object component;

    private final MethodMatchingInfo matchingInfo;

    private ComponentWithMatchingInfo( Object component, MethodMatchingInfo matchingInfo ) {
      this.component = component;
      this.matchingInfo = matchingInfo;
    }

    public Object getComponent() {
      return component;
    }

    public MethodMatchingInfo getMatchingInfo() {
      return matchingInfo;
    }

  }

}
