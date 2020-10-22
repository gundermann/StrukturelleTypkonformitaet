package matching.modules;

import java.util.Set;

import matching.methods.MethodMatchingInfo;

public class ModuleMatchingInfo {

  private Class<?> source;

  private Class<?> target;

  private Set<MethodMatchingInfo> methodMatchingInfos;

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
