package matching.methods;

import java.lang.reflect.Method;
import java.util.Map;

import matching.modules.ModuleMatchingInfo;

public class MethodMatchingInfo {

  private final Method source;

  private final Method target;

  private final ModuleMatchingInfo returnTypeMatchingInfo;

  private final Map<Integer, ModuleMatchingInfo> argumentTypeMatchingInfos;

  public MethodMatchingInfo( Method source, Method target, ModuleMatchingInfo returnTypeMatchingInfo,
      Map<Integer, ModuleMatchingInfo> argumentTypeMatchingInfos ) {
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

  public Map<Integer, ModuleMatchingInfo> getArgumentTypeMatchingInfos() {
    return argumentTypeMatchingInfos;
  }

  @Override
  public String toString() {
    return String.format( "%s -> %s", source.getName(), target.getName() );
  }

}
