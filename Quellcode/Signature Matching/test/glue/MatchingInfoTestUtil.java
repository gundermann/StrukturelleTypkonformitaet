package glue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.easymock.EasyMock;

import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.types.TypeMatchingInfo;
import testcomponents.wrapped.Wrapped;
import testcomponents.wrapped.Wrapper;

public class MatchingInfoTestUtil {

  @SafeVarargs
  static Map<ParamPosition, TypeMatchingInfo> createMMIMap( Entry<ParamPosition, TypeMatchingInfo>... infos ) {
    Map<ParamPosition, TypeMatchingInfo> map = new HashMap<>();
    for ( int i = 0; i < infos.length; i++ ) {
      Entry<ParamPosition, TypeMatchingInfo> entry = infos[i];
      map.put( entry.getKey(), entry.getValue() );
    }
    return map;
  }

  static Entry<ParamPosition, TypeMatchingInfo> createPPMapEntry( int sourcePos, int targetPos,
      TypeMatchingInfo createMMI_SameTypes ) {
    ParamPosition paramPosition = new ParamPosition( sourcePos, targetPos );
    Map<ParamPosition, TypeMatchingInfo> tmpMap = new HashMap<>();
    tmpMap.put( paramPosition, createMMI_SameTypes );
    return tmpMap.entrySet().iterator().next();
  }

  static Map<ParamPosition, TypeMatchingInfo> createMMIMap( TypeMatchingInfo... infos ) {
    Map<ParamPosition, TypeMatchingInfo> map = new HashMap<>();
    for ( int i = 0; i < infos.length; i++ ) {
      map.put( new ParamPosition( i, i ), infos[i] );
    }
    return map;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  static TypeMatchingInfo createMMI_Wrapper2Wrapped( String delegateAttr ) {
    TypeMatchingInfo mmi = EasyMock.createNiceMock( TypeMatchingInfo.class );
    EasyMock.expect( mmi.getSource() ).andReturn( (Class) Wrapper.class ).anyTimes();
    EasyMock.expect( mmi.getTarget() ).andReturn( (Class) Wrapped.class ).anyTimes();
    EasyMock.expect( mmi.getMethodMatchingInfos() ).andReturn( new HashSet<>() ).anyTimes();
    EasyMock.expect( mmi.getConverterCreator() )
        .andReturn( ProxyCreatorFactories.getWrappedFactoryCreator( delegateAttr ) )
        .anyTimes();
    EasyMock.replay( mmi );
    return mmi;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  static TypeMatchingInfo createMMI_Wrapped2Wrapper( String delegateAttr ) {
    TypeMatchingInfo mmi = EasyMock.createNiceMock( TypeMatchingInfo.class );
    EasyMock.expect( mmi.getSource() ).andReturn( (Class) Wrapped.class ).anyTimes();
    EasyMock.expect( mmi.getTarget() ).andReturn( (Class) Wrapper.class ).anyTimes();
    EasyMock.expect( mmi.getMethodMatchingInfos() ).andReturn( new HashSet<>() ).anyTimes();
    EasyMock.expect( mmi.getConverterCreator() )
        .andReturn( ProxyCreatorFactories.getWrapperFactoryCreator( delegateAttr ) )
        .anyTimes();
    EasyMock.replay( mmi );
    return mmi;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  static TypeMatchingInfo createMMI_SameTypes( Class<?> type ) {
    TypeMatchingInfo mmi = EasyMock.createNiceMock( TypeMatchingInfo.class );
    EasyMock.expect( mmi.getSource() ).andReturn( (Class) type ).anyTimes();
    EasyMock.expect( mmi.getTarget() ).andReturn( (Class) type ).anyTimes();
    EasyMock.expect( mmi.getMethodMatchingInfos() ).andReturn( new HashSet<>() ).anyTimes();
    EasyMock.expect( mmi.getConverterCreator() ).andReturn( ProxyCreatorFactories.getIdentityFactoryCreator() )
        .anyTimes();
    EasyMock.replay( mmi );
    return mmi;
  }

}
