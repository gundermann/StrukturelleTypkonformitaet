package matching.types;

@Deprecated
public class TypeMatchingInfoFactory<S, T> {

  private final Class<S> sourceType;

  private final Class<T> targetType;

  public TypeMatchingInfoFactory( Class<S> sourceType, Class<T> targetType ) {
    this.sourceType = sourceType;
    this.targetType = targetType;
  }

  public TypeMatchingInfo<S, T> create() {
    return new TypeMatchingInfo<>( sourceType, targetType );
  }

}
