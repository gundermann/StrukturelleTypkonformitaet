package glue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

import matching.methods.MethodMatchingInfo;
import matching.modules.ModuleMatchingInfo;
import matching.modules.testmodules.Enum1;
import matching.modules.testmodules.Enum2;
import matching.modules.testmodules.InterfaceGen;
import matching.modules.testmodules.InterfaceWrapper;

public class GenSpecSignatureMatchingTypeConverterTest {

  @Test
  public void specTargetReturnTypeMatching() throws NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    Class<InterfaceGen> source = InterfaceGen.class;
    Class<Enum2> target = Enum2.class;
    Enum2 convertationObject = Enum2.CONSTANT_1;
    SignatureMatchingTypeConverter<InterfaceGen> converter = new SignatureMatchingTypeConverter<>( source );

    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();
    MethodMatchingInfo methodMatchingInfoGetFalse = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( methodMatchingInfoGetFalse.getTarget() ).andReturn( target.getMethod( "getFalse" ) ).anyTimes();
    EasyMock.expect( methodMatchingInfoGetFalse.getSource() ).andReturn( source.getMethod( "getFalse" ) ).anyTimes();
    methodMatchingInfos.add( methodMatchingInfoGetFalse );

    MethodMatchingInfo methodMatchingInfoGetTrue = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( methodMatchingInfoGetTrue.getTarget() ).andReturn( target.getMethod( "getTrue" ) ).anyTimes();
    EasyMock.expect( methodMatchingInfoGetTrue.getSource() ).andReturn( source.getMethod( "getTrue" ) ).anyTimes();
    methodMatchingInfos.add( methodMatchingInfoGetTrue );

    MethodMatchingInfo methodMatchingInfoGetOne = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( methodMatchingInfoGetOne.getTarget() ).andReturn( target.getMethod( "getOne" ) ).anyTimes();
    EasyMock.expect( methodMatchingInfoGetOne.getSource() ).andReturn( source.getMethod( "getOne" ) ).anyTimes();
    methodMatchingInfos.add( methodMatchingInfoGetOne );

    ModuleMatchingInfo<InterfaceGen> moduleMatchingInfo = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( moduleMatchingInfo.getMethodMatchingInfos() ).andReturn( methodMatchingInfos ).anyTimes();
    EasyMock.replay( moduleMatchingInfo, methodMatchingInfoGetFalse, methodMatchingInfoGetTrue,
        methodMatchingInfoGetOne );
    InterfaceGen converted = converter.convert( convertationObject, moduleMatchingInfo );
    assertThat( converted.getFalse(), equalTo( convertationObject.getFalse() ) );
    assertThat( converted.getTrue(), equalTo( convertationObject.getTrue() ) );
    assertThat( converted.getOne(), equalTo( convertationObject.getOne() ) );
    checkInvokationOfAllNonParametrizedMethods( converted );
  }

  @Test
  public void genTargetReturnTypeMatching() throws NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    Class<InterfaceWrapper> source = InterfaceWrapper.class;
    Class<Enum1> target = Enum1.class;
    Enum1 convertationObject = Enum1.CONSTANT_1;
    SignatureMatchingTypeConverter<InterfaceWrapper> converter = new SignatureMatchingTypeConverter<>( source );

    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();
    MethodMatchingInfo methodMatchingInfoGetFalse = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( methodMatchingInfoGetFalse.getTarget() ).andReturn( target.getMethod( "getFalse" ) ).anyTimes();
    EasyMock.expect( methodMatchingInfoGetFalse.getSource() ).andReturn( source.getMethod( "getFalse" ) ).anyTimes();
    methodMatchingInfos.add( methodMatchingInfoGetFalse );

    MethodMatchingInfo methodMatchingInfoGetTrue = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( methodMatchingInfoGetTrue.getTarget() ).andReturn( target.getMethod( "getTrue" ) ).anyTimes();
    EasyMock.expect( methodMatchingInfoGetTrue.getSource() ).andReturn( source.getMethod( "getTrue" ) ).anyTimes();
    methodMatchingInfos.add( methodMatchingInfoGetTrue );

    MethodMatchingInfo methodMatchingInfoGetOne = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( methodMatchingInfoGetOne.getTarget() ).andReturn( target.getMethod( "getOne" ) ).anyTimes();
    EasyMock.expect( methodMatchingInfoGetOne.getSource() ).andReturn( source.getMethod( "getOne" ) ).anyTimes();
    methodMatchingInfos.add( methodMatchingInfoGetOne );

    ModuleMatchingInfo<InterfaceWrapper> moduleMatchingInfo = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( moduleMatchingInfo.getMethodMatchingInfos() ).andReturn( methodMatchingInfos ).anyTimes();
    EasyMock.replay( moduleMatchingInfo, methodMatchingInfoGetFalse, methodMatchingInfoGetTrue,
        methodMatchingInfoGetOne );
    InterfaceWrapper converted = converter.convert( convertationObject, moduleMatchingInfo );
    assertThat( converted.getFalse(), equalTo( convertationObject.getFalse() ) );
    assertThat( converted.getTrue(), equalTo( convertationObject.getTrue() ) );
    assertThat( converted.getOne(), equalTo( convertationObject.getOne() ) );
    checkInvokationOfAllNonParametrizedMethods( converted );
    // Check further calls
    checkBoolean2booleanCalls( converted.getFalse(), convertationObject.getFalse() );

  }

  private void checkBoolean2booleanCalls( Boolean spec, boolean false2 ) {
    // TODO Auto-generated method stub

  }

  private void checkInvokationOfAllNonParametrizedMethods( Object converted )
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    for ( Method m : converted.getClass().getMethods() ) {
      if ( m.getParameterCount() == 0 ) {
        System.out.println( "try to invoke: " + m.getName() );
        // TODO mit wait() gibt es Probleme
        // m.invoke( converted );
      }
    }

  }

}
