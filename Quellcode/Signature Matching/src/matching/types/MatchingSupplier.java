package matching.types;

import java.util.Collection;
import java.util.function.Supplier;

import matching.MatcherRate;
import matching.methods.MethodMatchingInfo;

public class MatchingSupplier {

  private Supplier<Collection<MethodMatchingInfo>> methodMatchInfosSupplier;

  private MatcherRate matcherRating;

  public MatchingSupplier( Supplier<Collection<MethodMatchingInfo>> supplier, MatcherRate matcherRating ) {
    methodMatchInfosSupplier = supplier;
    this.matcherRating = matcherRating;
  }

  public Supplier<Collection<MethodMatchingInfo>> getMethodMatchingInfosSupplier() {
    return methodMatchInfosSupplier;
  }

  public MatcherRate getMatcherRating() {
    return matcherRating;
  }

}
