package matching.methods;

import java.util.function.Function;

/**
 * <ul>
 * sourceAttribute == null && targetAttribute == null => S =< T
 * </ul>
 */
public class TypeMatchingInfo<S, T> {

  private final Class<S> source;

  private final Class<T> target;

  private final Function<S, T> sourceAttribute;

  private final Function<T, S> targetAttribute;

  public TypeMatchingInfo( Class<S> sourceType, Class<T> targetType ) {
    source = sourceType;
    target = targetType;
    sourceAttribute = null;
    targetAttribute = null;
  }

  public Class<?> getSource() {
    return source;
  }

  public Class<?> getTarget() {
    return target;
  }

}
