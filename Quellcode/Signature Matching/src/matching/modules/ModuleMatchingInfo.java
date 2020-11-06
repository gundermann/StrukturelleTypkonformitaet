package matching.modules;

import java.util.Set;

import matching.methods.MethodMatchingInfo;

public class ModuleMatchingInfo {

  private final Class<?> source;

  private final Class<?> target;

  // Benoetigt, wenn die Source das Target enthaelt (Target in Source)
  private final String sourceDelegateAttribute;

  // Benoetigt, wenn das Target die Source enthaelt (Source in Target)
  private final String targetDelegateAttribute;

  private final Set<MethodMatchingInfo> methodMatchingInfos;

  /**
   * Konstruktor fuer ModuleMatchings, bei denen zwischen Source und Target keine Contains-Assoziation besteht
   *
   * @param source
   * @param target
   * @param methodMatchingInfos
   */
  public ModuleMatchingInfo( Class<?> source, Class<?> target, Set<MethodMatchingInfo> methodMatchingInfos ) {
    this.source = source;
    this.target = target;
    this.methodMatchingInfos = methodMatchingInfos;
    this.sourceDelegateAttribute = null;
    this.targetDelegateAttribute = null;
  }

  /**
   * Konstruktor fuer ModuleMatchings, bei denen die Source im Target enthalten ist. (Source in Target)
   *
   * @param source
   * @param target
   * @param targetDelegate
   * @param methodMatchingInfos
   */
  public ModuleMatchingInfo( Class<?> source, Class<?> target, String targetDelegate,
      Set<MethodMatchingInfo> methodMatchingInfos ) {
    this.source = source;
    this.target = target;
    this.methodMatchingInfos = methodMatchingInfos;
    this.sourceDelegateAttribute = null;
    this.targetDelegateAttribute = targetDelegate;
  }

  /**
   * Konstruktor fuer ModuleMatchings, bei denen das Target in der Source enthalten ist. (Target in Source)
   *
   * @param source
   * @param sourceDelegate
   * @param target
   * @param methodMatchingInfos
   */
  public ModuleMatchingInfo( Class<?> source, String sourceDelegate, Class<?> target,
      Set<MethodMatchingInfo> methodMatchingInfos ) {
    this.source = source;
    this.target = target;
    this.methodMatchingInfos = methodMatchingInfos;
    this.sourceDelegateAttribute = sourceDelegate;
    this.targetDelegateAttribute = null;
  }

  /**
   * Diese Methode führt eine Bewertung hinsichtlich der Vollständigkeit bzgl. der erwünschten Methoden. Wenn das target
   * bspw. nur einen Teil der in der source erwarteten Methoden liefern kann (siehe {@link ModuleMatcher}->
   * partlyMatches), dann wird hier ein niedrigerer Wert geliefert, als wenn das target vollständig die source matched
   * (siehe {@link ModuleMatcher}-> matches).
   *
   * @deprecated TODO das muss woanders hin verschobenw reden
   * @return
   */
  @Deprecated
  public int getRating() {
    // Der Algorithmus muss noch angepasst werden!!!!
    int desiredMethodCount = source.getMethods().length;
    int matchedMethodCount = methodMatchingInfos.size();
    return matchedMethodCount / desiredMethodCount * 100;
  }

  /**
   * @return empty Set, if the source type is the same, or a superclass or a superinterface of the target type
   */
  public Set<MethodMatchingInfo> getMethodMatchingInfos() {
    return methodMatchingInfos;
  }

  public Class<?> getSource() {
    return source;
  }

  public Class<?> getTarget() {
    return target;
  }

  /**
   * @return <code> null </code> wenn das Target nicht in der Source enthalten ist.
   */
  public String getSourceDelegateAttribute() {
    return sourceDelegateAttribute;
  }

  /**
   * @return <code> null </code> wenn die Source nicht im Target enthalten ist.
   */
  public String getTargetDelegateAttribute() {
    return targetDelegateAttribute;
  }

}
