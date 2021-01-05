package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Supplier;

import matching.methods.MethodMatchingInfo;
import matching.modules.PartlyTypeMatchingInfo;

public class Transformator {

  public static Collection<CombinationPartInfo> transformTypeInfo2CombinationPartInfos(
      PartlyTypeMatchingInfo typeInfo ) {
    Collection<CombinationPartInfo> combinationPartInfos = new ArrayList<>();
    Class<?> componentClass = typeInfo.getCheckType();
    for ( Entry<Method, Supplier<Collection<MethodMatchingInfo>>> entry : typeInfo.getMethodMatchingInfoSupplier()
        .entrySet() ) {
      Method method = entry.getKey();
      Collection<MethodMatchingInfo> mMIs = entry.getValue().get();
      for ( MethodMatchingInfo mMI : mMIs ) {
        if ( Objects.equals( method, mMI.getSource() ) ) {
          CombinationPartInfo combinationPartInfo = new CombinationPartInfo( componentClass, method, mMI );
          combinationPartInfos.add( combinationPartInfo );
        }
      }
    }
    return combinationPartInfos;
  }
}
