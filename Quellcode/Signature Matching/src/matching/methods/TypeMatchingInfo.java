package matching.methods;

/**
 * <ul>
 * sourceAttribute == null && targetAttribute == null => S =< T
 * </ul>
 */
public class TypeMatchingInfo<S, T> {

  private Class<S> source;

  private Class<T> target;

  private String sourceAttribute;

  private String targetAttribute;

}
