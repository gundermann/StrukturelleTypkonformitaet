package matching.modules;

import java.util.Collection;

import glue.ProxyCreatorFactories;
import glue.ProxyFactoryCreator;
import matching.methods.MethodMatchingInfo;

public class ModuleMatchingInfo {

  private final Class<?> source;

  private final Class<?> target;

  private final Collection<MethodMatchingInfo> methodMatchingInfos;

  private ProxyFactoryCreator converterCreator;

  /**
   * Konstruktor fuer ModuleMatchings, bei denen zwischen Source und Target keine Contains-Assoziation besteht
   *
   * @param source
   * @param target
   * @param methodMatchingInfos
   */
  public ModuleMatchingInfo( Class<?> source, Class<?> target, Collection<MethodMatchingInfo> methodMatchingInfos ) {
    this( source, target, methodMatchingInfos,
        methodMatchingInfos.isEmpty() ? ProxyCreatorFactories.getIdentityFactoryCreator()
            : ProxyCreatorFactories.getClassFactoryCreator() );
  }

  /**
   * Konstruktor fuer ModuleMatchings, bei denen zwischen Source und Target keine Contains-Assoziation besteht
   *
   * @param source
   * @param target
   * @param methodMatchingInfos
   */
  public ModuleMatchingInfo( Class<?> source, Class<?> target, Collection<MethodMatchingInfo> methodMatchingInfos,
      ProxyFactoryCreator proxyFactoryCreator ) {
    this.source = source;
    this.target = target;
    this.methodMatchingInfos = methodMatchingInfos;
    this.converterCreator = proxyFactoryCreator;
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
      Collection<MethodMatchingInfo> methodMatchingInfos ) {
    this( source, target, methodMatchingInfos, ProxyCreatorFactories.getWrapperFactoryCreator( targetDelegate ) );
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
      Collection<MethodMatchingInfo> methodMatchingInfos ) {
    this( source, target, methodMatchingInfos, ProxyCreatorFactories.getWrappedFactoryCreator( sourceDelegate ) );
  }

  /**
   * Diese Methode f�hrt eine Bewertung hinsichtlich der Vollst�ndigkeit bzgl. der erw�nschten Methoden. Wenn das target
   * bspw. nur einen Teil der in der source erwarteten Methoden liefern kann , dann wird hier ein niedrigerer Wert
   * geliefert, als wenn das target vollst�ndig die source matched.
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
  public Collection<MethodMatchingInfo> getMethodMatchingInfos() {
    return methodMatchingInfos;
  }

  public Class<?> getSource() {
    return source;
  }

  public Class<?> getTarget() {
    return target;
  }

  public ProxyFactoryCreator getConverterCreator() {
    return this.converterCreator;
  }

  public void setConverterCreator( ProxyFactoryCreator converterCreator ) {
    this.converterCreator = converterCreator;
  }

}
