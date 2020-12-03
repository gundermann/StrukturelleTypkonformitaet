package matching.modules;

public interface PartlyTypeMatcher extends TypeMatcher {

  /**
   * @param checkType
   * @param queryType
   * @return Exists m1 checkType, m2 in queryType: m1 matches m2
   */
  boolean matchesTypePartly( Class<?> checkType, Class<?> queryType );

  PartlyTypeMatchingInfo calculatePartlyTypeMatchingInfos( Class<?> checkType, Class<?> queryType );

}
