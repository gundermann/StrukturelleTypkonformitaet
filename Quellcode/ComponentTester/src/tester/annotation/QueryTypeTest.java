package tester.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @deprecated die Referenz auf den Test wird im jeweiligen Interface-Typ durch die Annotation
 *             {@link QueryTypeTestReference} hergestellt.
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
@Deprecated
public @interface QueryTypeTest {

  Class<?> queryType();
}
