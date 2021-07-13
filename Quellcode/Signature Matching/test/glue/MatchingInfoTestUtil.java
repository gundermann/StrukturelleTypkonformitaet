package glue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.easymock.EasyMock;

import de.fernuni.hagen.ma.gundermann.signaturematching.SingleMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo.ParamPosition;
import testcomponents.wrapped.Wrapped;
import testcomponents.wrapped.Wrapper;

public class MatchingInfoTestUtil {

  @SafeVarargs
  static Map<ParamPosition, SingleMatchingInfo> createMMIMap( Entry<ParamPosition, SingleMatchingInfo>... infos ) {
    Map<ParamPosition, SingleMatchingInfo> map = new HashMap<>();
    for ( int i = 0; i < infos.length; i++ ) {
      Entry<ParamPosition, SingleMatchingInfo> entry = infos[i];
      map.put( entry.getKey(), entry.getValue() );
    }
    return map;
  }

  static Entry<ParamPosition, SingleMatchingInfo> createPPMapEntry( int sourcePos, int targetPos,
		  SingleMatchingInfo createMMI_SameTypes ) {
    ParamPosition paramPosition = new ParamPosition( sourcePos, targetPos );
    Map<ParamPosition, SingleMatchingInfo> tmpMap = new HashMap<>();
    tmpMap.put( paramPosition, createMMI_SameTypes );
    return tmpMap.entrySet().iterator().next();
  }

  static Map<ParamPosition, SingleMatchingInfo> createMMIMap( SingleMatchingInfo... infos ) {
    Map<ParamPosition, SingleMatchingInfo> map = new HashMap<>();
    for ( int i = 0; i < infos.length; i++ ) {
      map.put( new ParamPosition( i, i ), infos[i] );
    }
    return map;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  static SingleMatchingInfo createMMI_Wrapper2Wrapped( String delegateAttr ) {
	  SingleMatchingInfo mmi = EasyMock.createNiceMock( SingleMatchingInfo.class );
    EasyMock.expect( mmi.getSource() ).andReturn( (Class) Wrapper.class ).anyTimes();
    EasyMock.expect( mmi.getTarget() ).andReturn( (Class) Wrapped.class ).anyTimes();
    EasyMock.expect( mmi.getMethodMatchingInfos() ).andReturn( Collections.emptyMap() ).anyTimes();
    EasyMock.expect( mmi.getConverterCreator() )
        .andReturn( ProxyCreatorFactories.getWrappedFactoryCreator( delegateAttr ) )
        .anyTimes();
    EasyMock.replay( mmi );
    return mmi;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  static SingleMatchingInfo createMMI_Wrapped2Wrapper( String delegateAttr ) {
	  SingleMatchingInfo mmi = EasyMock.createNiceMock( SingleMatchingInfo.class );
    EasyMock.expect( mmi.getSource() ).andReturn( (Class) Wrapped.class ).anyTimes();
    EasyMock.expect( mmi.getTarget() ).andReturn( (Class) Wrapper.class ).anyTimes();
    EasyMock.expect( mmi.getMethodMatchingInfos() ).andReturn( Collections.emptyMap() ).anyTimes();
    EasyMock.expect( mmi.getConverterCreator() )
        .andReturn( ProxyCreatorFactories.getWrapperFactoryCreator( delegateAttr ) )
        .anyTimes();
    EasyMock.replay( mmi );
    return mmi;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  static SingleMatchingInfo createMMI_SameTypes( Class<?> type ) {
	  SingleMatchingInfo mmi = EasyMock.createNiceMock( SingleMatchingInfo.class );
    EasyMock.expect( mmi.getSource() ).andReturn( (Class) type ).anyTimes();
    EasyMock.expect( mmi.getTarget() ).andReturn( (Class) type ).anyTimes();
    EasyMock.expect( mmi.getMethodMatchingInfos() ).andReturn( Collections.emptyMap() ).anyTimes();
    EasyMock.expect( mmi.getConverterCreator() ).andReturn( ProxyCreatorFactories.getIdentityFactoryCreator() )
        .anyTimes();
    EasyMock.replay( mmi );
    return mmi;
  }

}
