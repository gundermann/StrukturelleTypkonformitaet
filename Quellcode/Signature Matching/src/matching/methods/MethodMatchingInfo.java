package matching.methods;

import java.lang.reflect.Method;
import java.util.Map;

import matching.modules.ModuleMatchingInfo;

public class MethodMatchingInfo {

  private final Method source;

  private final Method target;

  private final ModuleMatchingInfo returnTypeMatchingInfo;

  private final Map<ParamPosition, ModuleMatchingInfo> argumentTypeMatchingInfos;

  public MethodMatchingInfo( Method source, Method target, ModuleMatchingInfo returnTypeMatchingInfo,
      Map<ParamPosition, ModuleMatchingInfo> argumentTypeMatchingInfos ) {
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

  public ModuleMatchingInfo getReturnTypeMatchingInfo() {
    return returnTypeMatchingInfo;
  }

  public Map<ParamPosition, ModuleMatchingInfo> getArgumentTypeMatchingInfos() {
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
