package matching.methods;

import java.lang.reflect.Method;

import matching.MatcherRate;

public class MatchingMethod {

	private Method method;

	private double matcherRating;

	private MatcherRate rate;


	public MatchingMethod(Method method, MatcherRate rate) {
		this.method = method;
		this.rate = rate;
	}

	public Method getMethod() {
		return method;
	}

	@Deprecated
	public double getMatcherRating() {
		return matcherRating;
	}

	public MatcherRate getRate() {
		return rate;
	}

	
}
