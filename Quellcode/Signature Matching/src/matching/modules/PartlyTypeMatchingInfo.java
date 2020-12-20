package matching.modules;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import matching.methods.MethodMatchingInfo;

//Aequivalent zur ModuleMatchingInfo
public class PartlyTypeMatchingInfo {

  // Hier stehen nur Methoden drin, bei deren Aufruf auch an ein anderes Objekt
  // delegiert werden muss. Methoden, die von dem Objekt selbst ausgefuehrt werden
  // koennen, stehen hier nicht drin. D.h. bei einem Exact Matching waere diese Liste leer.
  private final Collection<Method> originalMethods;

  private final Map<Method, Supplier<Collection<MethodMatchingInfo>>> methodMatchingInfoSupplier;

  private final Class<?> checkType;

  /**
   * Die Methoden, die im Check-Type beim Ermitteln des Matchings untersucht wurden.
   */
  private int countOfPotentialMethods;

  PartlyTypeMatchingInfo( Class<?> checkType, Collection<Method> originalMethods,
      Map<Method, Supplier<Collection<MethodMatchingInfo>>> methodMatchingInfoSupplier, int countOfPotentialMethods ) {
    this.checkType = checkType;
    this.originalMethods = originalMethods;
    this.methodMatchingInfoSupplier = methodMatchingInfoSupplier;
    this.countOfPotentialMethods = countOfPotentialMethods;
  }

  public Collection<Method> getOriginalMethods() {
    return originalMethods;
  }

  public Map<Method, Supplier<Collection<MethodMatchingInfo>>> getMethodMatchingInfoSupplier() {
    return methodMatchingInfoSupplier;
  }

  public Class<?> getCheckType() {
    return checkType;
  }

  public double getQuantitaiveMatchRating() {
    if ( originalMethods == null || originalMethods.size() == 0 ) {
      return 1.0d;
    }
    if ( methodMatchingInfoSupplier == null ) {
      return 0.0d;
    }
    return Double.valueOf( methodMatchingInfoSupplier.keySet().size() ) / Double.valueOf( originalMethods.size() );
  }

  public double getQuantitativeMatchedToPotentialMethodsRating() {
    if ( countOfPotentialMethods == 0 ) {
      return 1.0d;
    }
    if ( methodMatchingInfoSupplier == null ) {
      return 0.0d;
    }
    return Double.valueOf( methodMatchingInfoSupplier.keySet().size() ) / Double.valueOf( countOfPotentialMethods );
  }

  public double getQualitativeMatchRating() {
    // TODO
    return -100d;
  }

}
