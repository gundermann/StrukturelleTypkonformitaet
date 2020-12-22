package matching.modules;

import java.util.Collection;
import java.util.function.Supplier;

import matching.methods.MethodMatchingInfo;

public class MatchingSupplier {

  private Supplier<Collection<MethodMatchingInfo>> methodMatchInfosSupplier;

  private double matcherRating;

  public MatchingSupplier( Supplier<Collection<MethodMatchingInfo>> supplier, Double matcherRating ) {
    methodMatchInfosSupplier = supplier;
    this.matcherRating = matcherRating;
  }

  public Supplier<Collection<MethodMatchingInfo>> getMethodMatchingInfosSupplier() {
    return methodMatchInfosSupplier;
  }

  public double getMatcherRating() {
    return matcherRating;
  }

}
