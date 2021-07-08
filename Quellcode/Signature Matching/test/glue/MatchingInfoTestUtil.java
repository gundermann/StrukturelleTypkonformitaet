package glue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.easymock.EasyMock;

import matching.MatchingInfo;
import matching.methods.MethodMatchingInfo.ParamPosition;
import testcomponents.wrapped.Wrapped;
import testcomponents.wrapped.Wrapper;

public class MatchingInfoTestUtil {

  @SafeVarargs
  static Map<ParamPosition, MatchingInfo> createMMIMap( Entry<ParamPosition, MatchingInfo>... infos ) {
    Map<ParamPosition, MatchingInfo> map = new HashMap<>();
    for ( int i = 0; i < infos.length; i++ ) {
      Entry<ParamPosition, MatchingInfo> entry = infos[i];
      map.put( entry.getKey(), entry.getValue() );
    }
    return map;
  }

  static Entry<ParamPosition, MatchingInfo> createPPMapEntry( int sourcePos, int targetPos,
		  MatchingInfo createMMI_SameTypes ) {
    ParamPosition paramPosition = new ParamPosition( sourcePos, targetPos );
    Map<ParamPosition, MatchingInfo> tmpMap = new HashMap<>();
    tmpMap.put( paramPosition, createMMI_SameTypes );
    return tmpMap.entrySet().iterator().next();
  }

  static Map<ParamPosition, MatchingInfo> createMMIMap( MatchingInfo... infos ) {
    Map<ParamPosition, MatchingInfo> map = new HashMap<>();
    for ( int i = 0; i < infos.length; i++ ) {
      map.put( new ParamPosition( i, i ), infos[i] );
    }
    return map;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  static MatchingInfo createMMI_Wrapper2Wrapped( String delegateAttr ) {
	  MatchingInfo mmi = EasyMock.createNiceMock( MatchingInfo.class );
    EasyMock.expect( mmi.getSource() ).andReturn( (Class) Wrapper.class ).anyTimes();
    EasyMock.expect( mmi.getTarget() ).andReturn( (Class) Wrapped.class ).anyTimes();
    EasyMock.expect( mmi.getMethodMatchingInfoSupplier() ).andReturn( Collections.emptyMap() ).anyTimes();
    EasyMock.expect( mmi.getConverterCreator() )
        .andReturn( ProxyCreatorFactories.getWrappedFactoryCreator( delegateAttr ) )
        .anyTimes();
    EasyMock.replay( mmi );
    return mmi;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  static MatchingInfo createMMI_Wrapped2Wrapper( String delegateAttr ) {
	  MatchingInfo mmi = EasyMock.createNiceMock( MatchingInfo.class );
    EasyMock.expect( mmi.getSource() ).andReturn( (Class) Wrapped.class ).anyTimes();
    EasyMock.expect( mmi.getTarget() ).andReturn( (Class) Wrapper.class ).anyTimes();
    EasyMock.expect( mmi.getMethodMatchingInfoSupplier() ).andReturn( Collections.emptyMap() ).anyTimes();
    EasyMock.expect( mmi.getConverterCreator() )
        .andReturn( ProxyCreatorFactories.getWrapperFactoryCreator( delegateAttr ) )
        .anyTimes();
    EasyMock.replay( mmi );
    return mmi;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  static MatchingInfo createMMI_SameTypes( Class<?> type ) {
	  MatchingInfo mmi = EasyMock.createNiceMock( MatchingInfo.class );
    EasyMock.expect( mmi.getSource() ).andReturn( (Class) type ).anyTimes();
    EasyMock.expect( mmi.getTarget() ).andReturn( (Class) type ).anyTimes();
    EasyMock.expect( mmi.getMethodMatchingInfoSupplier() ).andReturn( Collections.emptyMap() ).anyTimes();
    EasyMock.expect( mmi.getConverterCreator() ).andReturn( ProxyCreatorFactories.getIdentityFactoryCreator() )
        .anyTimes();
    EasyMock.replay( mmi );
    return mmi;
  }

}
