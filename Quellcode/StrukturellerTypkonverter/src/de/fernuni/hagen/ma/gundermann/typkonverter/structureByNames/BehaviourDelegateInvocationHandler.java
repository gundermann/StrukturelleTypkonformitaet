/*   $HeadURL$
 * ----------------------------------------------------------------------------
 *     (c) by data experts gmbh
 *            Woldegker Str. 12
 *            17033 Neubrandenburg
 * ----------------------------------------------------------------------------
 *     Dieses Dokument und die hierin enthaltenen Informationen unterliegen
 *     dem Urheberrecht und duerfen ohne die schriftliche Genehmigung des
 *     Herausgebers weder als ganzes noch in Teilen dupliziert, reproduziert
 *     oder manipuliert werden.
 * ----------------------------------------------------------------------------
 *     $Id$
 * ----------------------------------------------------------------------------
 */
package de.fernuni.hagen.ma.gundermann.typkonverter.structureByNames;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BehaviourDelegateInvocationHandler implements InvocationHandler {

  private final Object sourceObject;

  public BehaviourDelegateInvocationHandler( Object sourceObject ) {
    this.sourceObject = sourceObject;
  }

  @Override
  public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable {
    String methodName = method.getName();
    Method methodFromSource;
    if ( args != null ) {
      List<Class<?>> argsInList = Stream.of( args ).map( Object::getClass ).collect( Collectors.toList() );
      methodFromSource = sourceObject.getClass().getMethod( methodName, argsInList.toArray( new Class<?>[] {} ) );
    }
    else {
      methodFromSource = sourceObject.getClass().getMethod( methodName );
    }
    return methodFromSource.invoke( sourceObject, args );
  }

}
