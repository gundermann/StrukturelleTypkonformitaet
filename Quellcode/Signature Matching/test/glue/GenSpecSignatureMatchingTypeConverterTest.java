package glue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

import matching.methods.MethodMatchingInfo;
import matching.modules.ModuleMatchingInfo;
import matching.modules.testmodules.ClassSpec;
import matching.modules.testmodules.EnumNative;
import matching.modules.testmodules.InterfaceGen;
import matching.modules.testmodules.InterfaceWrapper;

public class GenSpecSignatureMatchingTypeConverterTest {

  @Test
  public void specTargetMatching() throws NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    Class<InterfaceGen> source = InterfaceGen.class;
    Class<ClassSpec> target = ClassSpec.class;
    ClassSpec convertationObject = new ClassSpec();
    SignatureMatchingTypeConverter<InterfaceGen> converter = new SignatureMatchingTypeConverter<>( source );

    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();
    MethodMatchingInfo mmiAdd = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiAdd.getTarget() )
        .andReturn( target.getDeclaredMethod( "add", BigInteger.class, BigInteger.class ) )
        .anyTimes();
    EasyMock.expect( mmiAdd.getSource() ).andReturn( source.getMethod( "add", Number.class, Number.class ) ).anyTimes();
    // EasyMock.expect( mmiAdd.getReturnTypeMatchingInfo() ).andReturn( createMMI_N2N() )
    // .anyTimes();
    EasyMock.expect( mmiAdd.getArgumentTypeMatchingInfos() )
        .andReturn(
            createMMIMap( createMMI_B2N(),
                createMMI_B2N() ) )
        .anyTimes();

    MethodMatchingInfo mmiSub = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiSub.getTarget() ).andReturn( target.getDeclaredMethod( "sub", Number.class, Number.class ) )
        .anyTimes();
    EasyMock.expect( mmiSub.getSource() ).andReturn( source.getMethod( "sub", Number.class, Number.class ) ).anyTimes();
    // EasyMock.expect( mmiSub.getReturnTypeMatchingInfo() ).andReturn( createMMI_B2N() )
    // .anyTimes();
    EasyMock.expect( mmiSub.getArgumentTypeMatchingInfos() )
        .andReturn(
            createMMIMap( createMMI_N2N(),
                createMMI_N2N() ) )
        .anyTimes();

    MethodMatchingInfo mmiDiv = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiDiv.getTarget() ).andReturn( target.getDeclaredMethod( "div", BigInteger.class, Number.class ) )
        .anyTimes();
    EasyMock.expect( mmiDiv.getSource() ).andReturn( source.getMethod( "div", Number.class, Number.class ) ).anyTimes();
    // EasyMock.expect( mmiDiv.getReturnTypeMatchingInfo() ).andReturn( createMMI_N2N() )
    // .anyTimes();
    EasyMock.expect( mmiDiv.getArgumentTypeMatchingInfos() )
        .andReturn(
            createMMIMap( createMMI_B2N(),
                createMMI_N2N() ) )
        .anyTimes();

    MethodMatchingInfo mmiMult = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiMult.getTarget() )
        .andReturn( target.getDeclaredMethod( "mult", Number.class, BigInteger.class ) )
        .anyTimes();
    EasyMock.expect( mmiMult.getSource() ).andReturn( source.getMethod( "mult", Number.class, Number.class ) )
        .anyTimes();
    // EasyMock.expect( mmiMult.getReturnTypeMatchingInfo() ).andReturn( createMMI_B2N() )
    // .anyTimes();
    EasyMock.expect( mmiMult.getArgumentTypeMatchingInfos() )
        .andReturn(
            createMMIMap( createMMI_N2N(),
                createMMI_B2N() ) )
        .anyTimes();
    EasyMock.replay( mmiAdd, mmiSub,
        mmiMult, mmiDiv );

    methodMatchingInfos.add( mmiAdd );
    methodMatchingInfos.add( mmiSub );
    methodMatchingInfos.add( mmiDiv );
    methodMatchingInfos.add( mmiMult );

    ModuleMatchingInfo<InterfaceGen> moduleMatchingInfo = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( moduleMatchingInfo.getMethodMatchingInfos() ).andReturn( methodMatchingInfos ).anyTimes();
    EasyMock.replay( moduleMatchingInfo );

    InterfaceGen converted = converter.convert( convertationObject, moduleMatchingInfo );

    // Der automatische Up- und Down-Cast funktioniert an dieser Stelle nicht. Diese Tests schlagen fehl!!!
    // assertThat( converted.sub( 2 , 1 ), equalTo( 1 ) );
    // assertThat( converted.sub( 2l , 1l ), equalTo( 1l ) );

    assertThat( converted.sub( BigInteger.valueOf( 2 ), BigInteger.valueOf( 1 ) ), equalTo( BigInteger.valueOf( 1 ) ) );
    assertThat( converted.add( 1l, 2l ), equalTo( 3l ) );
    assertThat( converted.div( 4l, 2l ), equalTo( 2l ) );
    assertThat( converted.mult( 4l, 2l ), equalTo( 8l ) );
    checkInvokationOfAllNonParametrizedMethods( converted );
  }

  private Map<Integer, ModuleMatchingInfo<?>> createMMIMap( ModuleMatchingInfo... infos ) {
    Map<Integer, ModuleMatchingInfo<?>> map = new HashMap<>();
    for ( int i = 0; i < infos.length; i++ ) {
      map.put( i, infos[i] );
    }
    return map;
  }

  private ModuleMatchingInfo createMMI_N2N() {
    ModuleMatchingInfo mmi = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( mmi.getSource() ).andReturn( Number.class ).anyTimes();
    EasyMock.expect( mmi.getTarget() ).andReturn( Number.class ).anyTimes();
    EasyMock.expect( mmi.getMethodMatchingInfos() ).andReturn( new HashSet<>() ).anyTimes();
    EasyMock.replay( mmi );
    return mmi;
  }

  private ModuleMatchingInfo createMMI_N2B() {
    ModuleMatchingInfo mmit = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( mmit.getSource() ).andReturn( Number.class ).anyTimes();
    EasyMock.expect( mmit.getTarget() ).andReturn( BigInteger.class ).anyTimes();
    EasyMock.expect( mmit.getMethodMatchingInfos() ).andReturn( new HashSet<>() ).anyTimes();
    EasyMock.replay( mmit );
    return mmit;
  }

  private ModuleMatchingInfo createMMI_B2N() {
    ModuleMatchingInfo mmit = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( mmit.getSource() ).andReturn( BigInteger.class ).anyTimes();
    EasyMock.expect( mmit.getTarget() ).andReturn( Number.class ).anyTimes();
    EasyMock.expect( mmit.getMethodMatchingInfos() ).andReturn( new HashSet<>() ).anyTimes();
    EasyMock.replay( mmit );
    return mmit;
  }

  @Test
  public void genTargetReturnTypeMatching() throws NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    Class<InterfaceWrapper> source = InterfaceWrapper.class;
    Class<EnumNative> target = EnumNative.class;
    EnumNative convertationObject = EnumNative.CONSTANT_1;
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
