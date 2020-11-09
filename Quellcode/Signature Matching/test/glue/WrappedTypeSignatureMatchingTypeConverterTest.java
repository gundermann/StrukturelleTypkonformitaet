package glue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

import matching.methods.MethodMatchingInfo;
import matching.modules.ModuleMatchingInfo;
import testcomponents.wrapped.DesiredWrappedParameterInterface;
import testcomponents.wrapped.DesiredWrapperParameterInterface;
import testcomponents.wrapped.OfferedWrappedParameterClass;
import testcomponents.wrapped.OfferedWrapperParameterClass;
import testcomponents.wrapped.Wrapped;
import testcomponents.wrapped.Wrapper;

public class WrappedTypeSignatureMatchingTypeConverterTest {

  @Test
  public void fromWrapped2Wrapper() throws NoSuchMethodException, SecurityException {
    Class<DesiredWrappedParameterInterface> source = DesiredWrappedParameterInterface.class;
    Class<OfferedWrapperParameterClass> target = OfferedWrapperParameterClass.class;
    OfferedWrapperParameterClass convertationObject = new OfferedWrapperParameterClass();
    SignatureMatchingTypeConverter<DesiredWrappedParameterInterface> converter = new SignatureMatchingTypeConverter<>(
        source );

    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();

    MethodMatchingInfo mmiConcat = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiConcat.getTarget() )
        .andReturn( target.getDeclaredMethod( "unify", Wrapper.class, Wrapper.class ) ).anyTimes();
    EasyMock.expect( mmiConcat.getSource() ).andReturn( source.getMethod( "concat", Wrapped.class, Wrapped.class ) )
        .anyTimes();
    EasyMock.expect( mmiConcat.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_Wrapper2Wrapped( "wrapped" ) )
        .anyTimes();
    EasyMock.expect( mmiConcat.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( MatchingInfoTestUtil.createMMI_Wrapped2Wrapper( "wrapped" ),
            MatchingInfoTestUtil.createMMI_Wrapped2Wrapper( "wrapped" ) ) )
        .anyTimes();

    MethodMatchingInfo mmiGetString = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiGetString.getTarget() )
        .andReturn( target.getDeclaredMethod( "wrapper", Wrapper.class ) ).anyTimes();
    EasyMock.expect( mmiGetString.getSource() ).andReturn( source.getMethod( "getString", Wrapped.class ) )
        .anyTimes();
    EasyMock.expect( mmiGetString.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( String.class ) )
        .anyTimes();
    EasyMock.expect( mmiGetString.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( MatchingInfoTestUtil.createMMI_Wrapped2Wrapper( "wrapped" ) ) )
        .anyTimes();

    EasyMock.replay( mmiGetString, mmiConcat );
    methodMatchingInfos.add( mmiGetString );
    methodMatchingInfos.add( mmiConcat );

    ModuleMatchingInfo moduleMatchingInfo = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( moduleMatchingInfo.getMethodMatchingInfos() ).andReturn( methodMatchingInfos ).anyTimes();
    EasyMock.replay( moduleMatchingInfo );

    DesiredWrappedParameterInterface converted = converter.convert( convertationObject, moduleMatchingInfo );
    Wrapped wrapped_hello = new Wrapped( "hello" );
    Wrapped wrapped_world = new Wrapped( "world" );

    assertThat( converted.getString( wrapped_hello ), equalTo( "hello" ) );
    assertThat( converted.getString( wrapped_world ), equalTo( "world" ) );
    Wrapped concatinated = converted.concat( wrapped_hello, wrapped_world );
    assertThat( converted.getString( concatinated ), equalTo( "helloworld" ) );
  }

  @Test
  public void fromWrapper2Wrapped() throws NoSuchMethodException, SecurityException {
    Class<DesiredWrapperParameterInterface> source = DesiredWrapperParameterInterface.class;
    Class<OfferedWrappedParameterClass> target = OfferedWrappedParameterClass.class;
    OfferedWrappedParameterClass convertationObject = new OfferedWrappedParameterClass();
    SignatureMatchingTypeConverter<DesiredWrapperParameterInterface> converter = new SignatureMatchingTypeConverter<>(
        source );

    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();

    MethodMatchingInfo mmiConnect = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiConnect.getTarget() )
        .andReturn( target.getDeclaredMethod( "unify", Wrapped.class, Wrapped.class ) ).anyTimes();
    EasyMock.expect( mmiConnect.getSource() ).andReturn( source.getMethod( "connect", Wrapper.class, Wrapper.class ) )
        .anyTimes();
    EasyMock.expect( mmiConnect.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_Wrapped2Wrapper( "wrapped" ) )
        .anyTimes();
    EasyMock.expect( mmiConnect.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( MatchingInfoTestUtil.createMMI_Wrapper2Wrapped( "wrapped" ),
            MatchingInfoTestUtil.createMMI_Wrapper2Wrapped( "wrapped" ) ) )
        .anyTimes();

    MethodMatchingInfo mmiValue = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiValue.getTarget() )
        .andReturn( target.getDeclaredMethod( "wrapped", Wrapped.class ) ).anyTimes();
    EasyMock.expect( mmiValue.getSource() ).andReturn( source.getMethod( "value", Wrapper.class ) )
        .anyTimes();
    EasyMock.expect( mmiValue.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( String.class ) )
        .anyTimes();
    EasyMock.expect( mmiValue.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( MatchingInfoTestUtil.createMMI_Wrapper2Wrapped( "wrapped" ) ) )
        .anyTimes();

    EasyMock.replay( mmiValue, mmiConnect );
    methodMatchingInfos.add( mmiValue );
    methodMatchingInfos.add( mmiConnect );

    ModuleMatchingInfo moduleMatchingInfo = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( moduleMatchingInfo.getMethodMatchingInfos() ).andReturn( methodMatchingInfos ).anyTimes();
    EasyMock.replay( moduleMatchingInfo );

    DesiredWrapperParameterInterface converted = converter.convert( convertationObject, moduleMatchingInfo );
    Wrapped wrapped_hello = new Wrapped( "hello" );
    Wrapped wrapped_world = new Wrapped( "world" );
    Wrapper wrapper_hello = new Wrapper( wrapped_hello );
    Wrapper wrapper_world = new Wrapper( wrapped_world );

    assertThat( converted.value( wrapper_hello ), equalTo( "hello" ) );
    assertThat( converted.value( wrapper_world ), equalTo( "world" ) );
    Wrapper concat = converted.connect( wrapper_hello, wrapper_world );
    assertThat( converted.value( concat ), equalTo( "helloworld" ) );
  }

}
