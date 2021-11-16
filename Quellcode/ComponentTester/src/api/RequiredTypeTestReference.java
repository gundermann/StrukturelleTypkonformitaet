package api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation fuer die Referenzierung der Testklassen im RequiredTyp.
 * 
 * @author Niels Gundermann
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequiredTypeTestReference {

	Class<?>[] testClasses();
}
