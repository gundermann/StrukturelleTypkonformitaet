package glue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.easymock.EasyMock;

import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.modules.ModuleMatchingInfo;
import matching.modules.ModuleMatchingInfoUtil;
import testcomponents.wrapped.Wrapped;
import testcomponents.wrapped.Wrapper;

public class MatchingInfoTestUtil {

  @SafeVarargs
  static Map<ParamPosition, ModuleMatchingInfo> createMMIMap( Entry<ParamPosition, ModuleMatchingInfo>... infos ) {
    Map<ParamPosition, ModuleMatchingInfo> map = new HashMap<>();
    for ( int i = 0; i < infos.length; i++ ) {
      Entry<ParamPosition, ModuleMatchingInfo> entry = infos[i];
      map.put( entry.getKey(), entry.getValue() );
    }
    return map;
  }

  static Entry<ParamPosition, ModuleMatchingInfo> createPPMapEntry( int sourcePos, int targetPos,
      ModuleMatchingInfo createMMI_SameTypes ) {
    ParamPosition paramPosition = new ParamPosition( sourcePos, targetPos );
    Map<ParamPosition, ModuleMatchingInfo> tmpMap = new HashMap<>();
    tmpMap.put( paramPosition, createMMI_SameTypes );
    return tmpMap.entrySet().iterator().next();
  }

  static Map<ParamPosition, ModuleMatchingInfo> createMMIMap( ModuleMatchingInfo... infos ) {
    Map<ParamPosition, ModuleMatchingInfo> map = new HashMap<>();
    for ( int i = 0; i < infos.length; i++ ) {
      map.put( new ParamPosition( i, i ), infos[i] );
    }
    return map;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  static ModuleMatchingInfo createMMI_Wrapper2Wrapped( String delegateAttr ) {
    ModuleMatchingInfo mmi = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( mmi.getSource() ).andReturn( (Class) Wrapper.class ).anyTimes();
    EasyMock.expect( mmi.getSourceDelegate() )
        .andReturn( ModuleMatchingInfoUtil.getFieldFunction( Wrapper.class, delegateAttr ) ).anyTimes();
    EasyMock.expect( mmi.getTarget() ).andReturn( (Class) Wrapped.class ).anyTimes();
    EasyMock.expect( mmi.getMethodMatchingInfos() ).andReturn( new HashSet<>() ).anyTimes();
    EasyMock.replay( mmi );
    return mmi;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  static ModuleMatchingInfo createMMI_Wrapped2Wrapper( String delegateAttr ) {
    ModuleMatchingInfo mmi = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( mmi.getSource() ).andReturn( (Class) Wrapped.class ).anyTimes();
    EasyMock.expect( mmi.getTarget() ).andReturn( (Class) Wrapper.class ).anyTimes();
    EasyMock.expect( mmi.getTargetDelegate() )
        .andReturn( ModuleMatchingInfoUtil.setFieldFunction( Wrapper.class, delegateAttr ) ).anyTimes();
    EasyMock.expect( mmi.getMethodMatchingInfos() ).andReturn( new HashSet<>() ).anyTimes();
    EasyMock.replay( mmi );
    return mmi;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  static ModuleMatchingInfo createMMI_SameTypes( Class<?> type ) {
    ModuleMatchingInfo mmi = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( mmi.getSource() ).andReturn( (Class) type ).anyTimes();
    EasyMock.expect( mmi.getTarget() ).andReturn( (Class) type ).anyTimes();
    EasyMock.expect( mmi.getMethodMatchingInfos() ).andReturn( new HashSet<>() ).anyTimes();
    EasyMock.replay( mmi );
    return mmi;
  }

}
