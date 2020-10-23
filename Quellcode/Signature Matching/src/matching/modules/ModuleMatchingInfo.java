package matching.modules;

import java.util.Set;

import matching.methods.MethodMatchingInfo;

public class ModuleMatchingInfo<S, T> {

  private final Class<S> source;

  private final Class<T> target;

  private final Set<MethodMatchingInfo> methodMatchingInfos;

  public ModuleMatchingInfo( Class<S> source, Class<T> target, Set<MethodMatchingInfo> methodMatchingInfos ) {
    this.source = source;
    this.target = target;
    this.methodMatchingInfos = methodMatchingInfos;
  }

  /**
   * Diese Methode f�hrt eine Bewertung hinsichtlich der Vollst�ndigkeit bzgl. der erw�nschten Methoden. Wenn das target
   * bspw. nur einen Teil der in der source erwarteten Methoden liefern kann (siehe {@link ModuleMatcher}->
   * partlyMatches), dann wird hier ein niedrigerer Wert geliefert, als wenn das target vollst�ndig die source matched
   * (siehe {@link ModuleMatcher}-> matches).
   *
   * @return
   */
  private int getRating() {
    int desiredMethodCount = source.getMethods().length;
    int matchedMethodCount = methodMatchingInfos.size();
    return matchedMethodCount / desiredMethodCount * 100;
  }
}
