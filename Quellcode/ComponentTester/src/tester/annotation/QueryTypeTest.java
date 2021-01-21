package tester.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface QueryTypeTest {

  /**
   * @return Name der Methode des erwarteten Interfaces, die als einzige in diesem Test getestet wird. Leer, wenn
   *         mehrere Methoden getestet werden.
   */
  // ACHTUNG: Dadurch werden gleichnamige Methode mit unterschiedlichen Signaturen in den erwarteten Interfaces
  // ausgeschlossen!!!
  String testedSingleMethod() default "";
}
