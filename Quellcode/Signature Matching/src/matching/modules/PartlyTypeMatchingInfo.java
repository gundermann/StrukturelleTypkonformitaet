package matching.modules;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import matching.methods.MethodMatchingInfo;

//Äquivalent zur ModuleMatchingInfo
public class PartlyTypeMatchingInfo {

  private final Collection<Method> originalMethods;

  private final Map<Method, Supplier<Collection<MethodMatchingInfo>>> methodMatchingInfoSupplier;

  private final Class<?> checkType;

  PartlyTypeMatchingInfo( Class<?> checkType, Collection<Method> originalMethods,
      Map<Method, Supplier<Collection<MethodMatchingInfo>>> methodMatchingInfoSupplier ) {
    this.checkType = checkType;
    this.originalMethods = originalMethods;
    this.methodMatchingInfoSupplier = methodMatchingInfoSupplier;
  }

}
