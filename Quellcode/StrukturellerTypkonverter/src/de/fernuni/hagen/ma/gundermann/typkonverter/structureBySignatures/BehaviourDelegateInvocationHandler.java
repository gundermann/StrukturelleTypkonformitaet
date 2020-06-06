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
package de.fernuni.hagen.ma.gundermann.typkonverter.structureBySignatures;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import de.fernuni.hagen.ma.gundermann.typkonverter.structureBySignatures.StructureBySignatureTypeConverter.MethodStructure;


public class BehaviourDelegateInvocationHandler implements InvocationHandler {

  private final Object sourceObject;

  public BehaviourDelegateInvocationHandler( Object sourceObject ) {
    this.sourceObject = sourceObject;
  }

  @Override
  public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable {
    MethodStructure targetMethodStructure = MethodStructure.createFromDeclaredMethod( method );

    Method[] declaredSourceMethods = sourceObject.getClass().getDeclaredMethods();
    for ( Method m : declaredSourceMethods ) {
      MethodStructure sourceMethodStructure = MethodStructure.createFromDeclaredMethod( m );
      if ( sourceMethodStructure.equals( targetMethodStructure ) ) {
        return m.invoke( sourceObject, args );
      }
    }
    throw new NoSuchMethodException();
  }

}
