package api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation fuer die markierung des RequiredType-Setters in der Testklasse.
 * (Fuer Setter-Injection)
 * 
 * @author Niels Gundermann
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequiredTypeInstanceSetter {

}
