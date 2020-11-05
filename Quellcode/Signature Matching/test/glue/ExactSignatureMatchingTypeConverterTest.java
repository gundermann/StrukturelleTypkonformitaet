package glue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

import matching.methods.MethodMatchingInfo;
import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.modules.ModuleMatchingInfo;
import matching.modules.testmodules.Enum2;
import matching.modules.testmodules.InterfaceWrapper;

public class ExactSignatureMatchingTypeConverterTest {

  @Test
  public void exactFullMatching() throws NoSuchMethodException, SecurityException {
    Class<InterfaceWrapper> source = InterfaceWrapper.class;
    Class<Enum2> target = Enum2.class;
    Enum2 convertationObject = Enum2.CONSTANT_1;
    SignatureMatchingTypeConverter<InterfaceWrapper> converter = new SignatureMatchingTypeConverter<>( source );

    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();
    MethodMatchingInfo methodMatchingInfoGetFalse = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( methodMatchingInfoGetFalse.getTarget() ).andReturn( target.getMethod( "getFalse" ) ).anyTimes();
    EasyMock.expect( methodMatchingInfoGetFalse.getSource() ).andReturn( source.getMethod( "getFalse" ) ).anyTimes();
    EasyMock.expect( methodMatchingInfoGetFalse.getArgumentTypeMatchingInfos() )
        .andReturn( new HashMap<ParamPosition, ModuleMatchingInfo>() ).anyTimes();
    methodMatchingInfos.add( methodMatchingInfoGetFalse );

    MethodMatchingInfo methodMatchingInfoGetTrue = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( methodMatchingInfoGetTrue.getTarget() ).andReturn( target.getMethod( "getTrue" ) ).anyTimes();
    EasyMock.expect( methodMatchingInfoGetTrue.getSource() ).andReturn( source.getMethod( "getTrue" ) ).anyTimes();
    EasyMock.expect( methodMatchingInfoGetTrue.getArgumentTypeMatchingInfos() )
        .andReturn( new HashMap<ParamPosition, ModuleMatchingInfo>() ).anyTimes();
    methodMatchingInfos.add( methodMatchingInfoGetTrue );

    MethodMatchingInfo methodMatchingInfoGetOne = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( methodMatchingInfoGetOne.getTarget() ).andReturn( target.getMethod( "getOne" ) ).anyTimes();
    EasyMock.expect( methodMatchingInfoGetOne.getSource() ).andReturn( source.getMethod( "getOne" ) ).anyTimes();
    EasyMock.expect( methodMatchingInfoGetOne.getArgumentTypeMatchingInfos() )
        .andReturn( new HashMap<ParamPosition, ModuleMatchingInfo>() ).anyTimes();
    methodMatchingInfos.add( methodMatchingInfoGetOne );

    ModuleMatchingInfo moduleMatchingInfo = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( moduleMatchingInfo.getMethodMatchingInfos() ).andReturn( methodMatchingInfos ).anyTimes();
    EasyMock.replay( moduleMatchingInfo, methodMatchingInfoGetFalse, methodMatchingInfoGetTrue,
        methodMatchingInfoGetOne );
    InterfaceWrapper converted = converter.convert( convertationObject, moduleMatchingInfo );
    assertThat( converted.getFalse(), equalTo( convertationObject.getFalse() ) );
    assertThat( converted.getTrue(), equalTo( convertationObject.getTrue() ) );
    assertThat( converted.getOne(), equalTo( convertationObject.getOne() ) );
  }

}
