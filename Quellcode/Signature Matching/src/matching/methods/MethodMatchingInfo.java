package matching.methods;

import java.lang.reflect.Method;
import java.util.Map;

public class MethodMatchingInfo {

  private final Method source;

  private final Method target;

  private final TypeMatchingInfo<?, ?> returnTypeMatchingInfo;

  private final Map<Integer, TypeMatchingInfo<?, ?>> argumentTypeMatchingInfos;

  public MethodMatchingInfo( Method source, Method target, TypeMatchingInfo<?, ?> returnTypeMatchingInfo,
      Map<Integer, TypeMatchingInfo<?, ?>> argumentTypeMatchingInfos ) {
    this.source = source;
    this.target = target;
    this.returnTypeMatchingInfo = returnTypeMatchingInfo;
    this.argumentTypeMatchingInfos = argumentTypeMatchingInfos;
  }

  public Method getSource() {
    return source;
  }

  public Method getTarget() {
    return target;
  }

  public TypeMatchingInfo<?, ?> getReturnTypeMatchingInfo() {
    return returnTypeMatchingInfo;
  }

  public Map<Integer, TypeMatchingInfo<?, ?>> getArgumentTypeMatchingInfos() {
    return argumentTypeMatchingInfos;
  }

}
