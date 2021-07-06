package matching.methods;

import java.lang.reflect.Method;
import java.util.Map;

import matching.types.TypeMatchingInfo;

public class MethodMatchingInfo {

  private final Method source;

  private final Method target;

  private final TypeMatchingInfo returnTypeMatchingInfo;

  private final Map<ParamPosition, TypeMatchingInfo> argumentTypeMatchingInfos;

  public MethodMatchingInfo( Method source, Method target, TypeMatchingInfo returnTypeMatchingInfo,
      Map<ParamPosition, TypeMatchingInfo> argumentTypeMatchingInfos ) {
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

  public TypeMatchingInfo getReturnTypeMatchingInfo() {
    return returnTypeMatchingInfo;
  }

  public Map<ParamPosition, TypeMatchingInfo> getArgumentTypeMatchingInfos() {
    return argumentTypeMatchingInfos;
  }

  @Override
  public String toString() {
    return String.format( "%s -> %s", source.getName(), target.getName() );
  }

  public static class ParamPosition {

    private final Integer sourceParamPosition;

    private final Integer targetParamPosition;

    public ParamPosition( Integer sourceParamPosition, Integer targetParamPosition ) {
      this.sourceParamPosition = sourceParamPosition;
      this.targetParamPosition = targetParamPosition;
    }

    public Integer getTargetParamPosition() {
      return targetParamPosition;
    }

    public Integer getSourceParamPosition() {
      return sourceParamPosition;
    }
  }

}
