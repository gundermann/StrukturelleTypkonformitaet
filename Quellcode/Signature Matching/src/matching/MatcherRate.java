package matching;

public class MatcherRate {

  private double matcherRating;

  private String[] matcherNames = new String[] {};

  public void add( String matcherName, double additionalMatcherRating ) {
    this.matcherRating += additionalMatcherRating;
    String[] temp = new String[this.matcherNames.length + 1];
    for ( int i = 0; i < this.matcherNames.length; i++ ) {
      temp[i] = this.matcherNames[i];
    }
    temp[temp.length - 1] = matcherName;
    this.matcherNames = temp;
  }

  public void add( MatcherRate matcherRate ) {
    this.matcherRating += matcherRate.getMatcherRating();
    String[] temp = new String[this.matcherNames.length + matcherRate.getMatcherNames().length];
    for ( int i = 0; i < this.matcherNames.length; i++ ) {
      temp[i] = this.matcherNames[i];
    }
    for ( int i = 0; i < matcherRate.getMatcherNames().length; i++ ) {
      temp[i + this.matcherNames.length] = matcherRate.getMatcherNames()[i];
    }
    this.matcherNames = temp;
  }

  public double getMatcherRating() {
    return matcherRating;
  }

  public String[] getMatcherNames() {
    return matcherNames;
  }

  public static int compare( MatcherRate r1, MatcherRate r2 ) {
    return Double.compare( r1.getMatcherRating(), r2.getMatcherRating() );
  }

}
