package matching.methods;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import matching.Logger;

public final class MethodMatchingInfoFactory {

  private final Method source;

  private final Method target;

  public MethodMatchingInfoFactory( Method source, Method target ) {
    this.source = source;
    this.target = target;
  }

  public MethodMatchingInfo create( TypeMatchingInfo returnTypeMatchingInfo,
      Map<Integer, TypeMatchingInfo> argumentTypeMatchingInfos ) {
    return new MethodMatchingInfo( source, target, returnTypeMatchingInfo, argumentTypeMatchingInfos );
  }

  public Set<MethodMatchingInfo> createFromTypeMatchingInfos( Set<TypeMatchingInfo> returnTypeMatchingInfos,
      Set<Map<Integer, TypeMatchingInfo>> argumentTypesMatchingInfos ) {
    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();
    if ( returnTypeMatchingInfos.size() != argumentTypesMatchingInfos.size() ) {
      Logger.info( String.format( "different size of matchingInfos %d != %d", returnTypeMatchingInfos.size(),
          argumentTypesMatchingInfos.size() ) );
      return methodMatchingInfos;
    }

    Iterator<TypeMatchingInfo> returnTypeMIIterator = returnTypeMatchingInfos.iterator();
    Iterator<Map<Integer, TypeMatchingInfo>> argumentTypesITIterator = argumentTypesMatchingInfos.iterator();
    while ( returnTypeMIIterator.hasNext() && argumentTypesITIterator.hasNext() ) {
      TypeMatchingInfo selectedRT = returnTypeMIIterator.next();
      Map<Integer, TypeMatchingInfo> selectedAT = argumentTypesITIterator.next();
      methodMatchingInfos.add( create( selectedRT, selectedAT ) );
    }

    return methodMatchingInfos;
  }

}
