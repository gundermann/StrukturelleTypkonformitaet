package matching.modules;

import java.lang.reflect.Method;

public class MatchingMethod {

  private Method method;

  private double matcherRating;

  public MatchingMethod( Method method, double rating ) {
    this.method = method;
    matcherRating = rating;
  }

  public Method getMethod() {
    return method;
  }

  public double getMatcherRating() {
    return matcherRating;
  }

}
