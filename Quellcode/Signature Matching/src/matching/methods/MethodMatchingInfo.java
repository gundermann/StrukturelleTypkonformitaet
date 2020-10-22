package matching.methods;

import java.lang.reflect.Method;
import java.util.Map;

public class MethodMatchingInfo {

  private Method source;

  private Method target;

  private TypeMatchingInfo returnTypeMatchingInfo;

  private Map<Integer, TypeMatchingInfo> argumentTypeMatchingInfos;
}
