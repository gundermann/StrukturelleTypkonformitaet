package matching.methods;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

import matching.modules.ModuleMatchingInfo;

/**
 * Dieser {@link MethodMatcher} soll in der Lage sein, zwei Methoden mit einer unterschiedlichen Anzahl von Parametern
 * zu matchen. Dabei wird davon ausgegangen, dass die Parameter-Typen der Methode mit den wenigeren Parametern einen
 * Container darstellen, der die Parameter, zu denen kein passender Typ gefunden werden kann, enthält.
 */
// TODO
public class ContaineredArgumentMethodMatcher implements MethodMatcher {

  @Override
  public boolean matches( Method m1, Method m2 ) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Set<MethodMatchingInfo> calculateMatchingInfos( Method m1, Method m2 ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean matchesType( Class<?> checkType, Class<?> queryType ) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Collection<ModuleMatchingInfo> calculateTypeMatchingInfos( Class<?> checkType, Class<?> queryType ) {
    // TODO Auto-generated method stub
    return null;
  }

}
