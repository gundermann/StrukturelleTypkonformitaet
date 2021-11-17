package common;


import java.util.Collection;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.ProxyFactoryCreator;
import de.fernuni.hagen.ma.gundermann.signaturematching.glue.factories.ProxyCreatorFactories;

public class TypeMatchingInfo {

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
  @Deprecated
  public TypeMatchingInfo( Class<?> source, Class<?> target, Collection<MethodMatchingInfo> methodMatchingInfos ) {
    this( source, target, methodMatchingInfos,
        methodMatchingInfos.isEmpty() ? ProxyCreatorFactories.getIdentityFactoryCreator()
            : ProxyCreatorFactories.getClassProxyFactoryCreator() );
  }

  /**
   * Konstruktor fuer ModuleMatchings, bei denen zwischen Source und Target keine Contains-Assoziation besteht
   *
   * @param source
   * @param target
   * @param methodMatchingInfos
   */
  public TypeMatchingInfo( Class<?> source, Class<?> target, Collection<MethodMatchingInfo> methodMatchingInfos,
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
  public TypeMatchingInfo( Class<?> source, Class<?> target, String targetDelegate,
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
  public TypeMatchingInfo( Class<?> source, String sourceDelegate, Class<?> target,
      Collection<MethodMatchingInfo> methodMatchingInfos ) {
    this( source, target, methodMatchingInfos, ProxyCreatorFactories.getWrappedFactoryCreator( sourceDelegate ) );
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
