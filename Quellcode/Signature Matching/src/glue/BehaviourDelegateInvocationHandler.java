package glue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
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
      Object returnValue = targetMethod.invoke( component, args );
      ModuleMatchingInfo<?> returnTypeMatchingInfo = methodMatchingInfo.getReturnTypeMatchingInfo();
      if ( returnTypeMatchingInfo == null ) {
        return returnValue;
      }
      // Bei einem allgemeineren returnValue des Targets (Target.retrunValue > Source.returnValue) muss der ReturnType
      // ebenfalls gemocked werden
      return convertReturnType( returnValue, returnTypeMatchingInfo );
    }

    // Default-Methode
    // (Hierbei handelt es sich um Methoden, dei von jedem Objekt erfüllt werden und für die aufgrund einer fehlenden
    // Implementierung keine MatchingInfo existiert)
    return method.invoke( component, args );
  }

  /**
   * Rekursiver Aufruf des gesamten Konvertierungsprozesses
   *
   * @param returnValue
   * @param returnTypeMatchingInfo
   * @return
   */
  private <RT> RT convertReturnType( Object returnValue, ModuleMatchingInfo<RT> returnTypeMatchingInfo ) {
    return new SignatureMatchingTypeConverter<>( returnTypeMatchingInfo.getSource() ).convert( returnValue,
        returnTypeMatchingInfo );
  }

  private Optional<MethodMatchingInfo> getMethodMatchingInfo( Method method ) {
    return matchingInfos.getMethodMatchingInfos().stream().filter( info -> info.getSource().equals( method ) )
        .findFirst();
  }

}
