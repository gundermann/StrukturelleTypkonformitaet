package glue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

import matching.methods.MethodMatchingInfo;
import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.modules.ModuleMatchingInfo;
import testcomponents.paramperm.Desired1ParameterInterface;
import testcomponents.paramperm.Desired2ParameterInterface;
import testcomponents.paramperm.Desired3ParameterInterface;
import testcomponents.paramperm.Offered1ParameterClass;
import testcomponents.paramperm.Offered2ParameterClass;
import testcomponents.paramperm.Offered3ParameterClass;

public class ParamPermSignatureMatchingTypeConverterTest {

  @Test
  public void oneParameter() throws NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    Class<Desired1ParameterInterface> source = Desired1ParameterInterface.class;
    Class<Offered1ParameterClass> target = Offered1ParameterClass.class;
    Offered1ParameterClass convertationObject = new Offered1ParameterClass();
    SignatureMatchingTypeConverter<Desired1ParameterInterface> converter = new SignatureMatchingTypeConverter<>(
        source );

    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();

    MethodMatchingInfo mmi = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmi.getTarget() )
        .andReturn( target.getDeclaredMethod( "doOfferedWith1Param", String.class ) ).anyTimes();
    EasyMock.expect( mmi.getSource() ).andReturn( source.getMethod( "doStuffWith1Param", String.class ) )
        .anyTimes();
    EasyMock.expect( mmi.getReturnTypeMatchingInfo() ).andReturn( createMMI_SameTypes( String.class ) )
        .anyTimes();
    EasyMock.expect( mmi.getArgumentTypeMatchingInfos() )
        .andReturn( createMMIMap( createPPMapEntry( 0, 0, createMMI_SameTypes( String.class ) ) ) ).anyTimes();

    EasyMock.replay( mmi );

    methodMatchingInfos.add( mmi );

    ModuleMatchingInfo moduleMatchingInfo = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( moduleMatchingInfo.getMethodMatchingInfos() ).andReturn( methodMatchingInfos ).anyTimes();
    EasyMock.replay( moduleMatchingInfo );

    Desired1ParameterInterface converted = converter.convert( convertationObject, moduleMatchingInfo );

    assertThat( converted.doStuffWith1Param( "helloworld" ), equalTo( "helloworld" ) );
    assertThat( converted.doStuffWith1Param( "hier gibt es keine Permutation" ),
        equalTo( "hier gibt es keine Permutation" ) );

    checkInvokationOfAllNonParametrizedMethods( converted );
  }

  @Test
  public void towParameter() throws NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    Class<Desired2ParameterInterface> source = Desired2ParameterInterface.class;
    Class<Offered2ParameterClass> target = Offered2ParameterClass.class;
    Offered2ParameterClass convertationObject = new Offered2ParameterClass();
    SignatureMatchingTypeConverter<Desired2ParameterInterface> converter = new SignatureMatchingTypeConverter<>(
        source );

    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();

    MethodMatchingInfo mmi = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmi.getTarget() )
        .andReturn( target.getDeclaredMethod( "doOfferedWith2Params", int.class, String.class ) ).anyTimes();
    EasyMock.expect( mmi.getSource() ).andReturn( source.getMethod( "doStuffWith2Params", String.class, int.class ) )
        .anyTimes();
    EasyMock.expect( mmi.getReturnTypeMatchingInfo() ).andReturn( createMMI_SameTypes( String.class ) )
        .anyTimes();
    EasyMock.expect( mmi.getArgumentTypeMatchingInfos() )
        .andReturn( createMMIMap( createPPMapEntry( 0, 1, createMMI_SameTypes( String.class ) ),
            createPPMapEntry( 1, 0, createMMI_SameTypes( int.class ) ) ) )
        .anyTimes();

    EasyMock.replay( mmi );

    methodMatchingInfos.add( mmi );

    ModuleMatchingInfo moduleMatchingInfo = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( moduleMatchingInfo.getMethodMatchingInfos() ).andReturn( methodMatchingInfos ).anyTimes();
    EasyMock.replay( moduleMatchingInfo );

    Desired2ParameterInterface converted = converter.convert( convertationObject, moduleMatchingInfo );

    assertThat( converted.doStuffWith2Params( "helloworld", 1 ), equalTo( "1helloworld" ) );
    assertThat( converted.doStuffWith2Params( " Permutation gibt es hier", 2 ),
        equalTo( "2 Permutation gibt es hier" ) );

    checkInvokationOfAllNonParametrizedMethods( converted );

  }

  @Test
  public void threeParameter() throws NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    Class<Desired3ParameterInterface> source = Desired3ParameterInterface.class;
    Class<Offered3ParameterClass> target = Offered3ParameterClass.class;
    Offered3ParameterClass convertationObject = new Offered3ParameterClass();
    SignatureMatchingTypeConverter<Desired3ParameterInterface> converter = new SignatureMatchingTypeConverter<>(
        source );

    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();

    MethodMatchingInfo mmi = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmi.getTarget() )
        .andReturn( target.getDeclaredMethod( "doOfferedWith3Params", int.class, boolean.class, String.class ) )
        .anyTimes();
    EasyMock.expect( mmi.getSource() )
        .andReturn( source.getMethod( "doStuffWith3Params", String.class, int.class, boolean.class ) )
        .anyTimes();
    EasyMock.expect( mmi.getReturnTypeMatchingInfo() ).andReturn( createMMI_SameTypes( String.class ) )
        .anyTimes();
    EasyMock.expect( mmi.getArgumentTypeMatchingInfos() )
        .andReturn( createMMIMap( createPPMapEntry( 2, 1, createMMI_SameTypes( boolean.class ) ),
            createPPMapEntry( 0, 2, createMMI_SameTypes( String.class ) ),
            createPPMapEntry( 1, 0, createMMI_SameTypes( int.class ) ) ) )
        .anyTimes();

    EasyMock.replay( mmi );

    methodMatchingInfos.add( mmi );

    ModuleMatchingInfo moduleMatchingInfo = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( moduleMatchingInfo.getMethodMatchingInfos() ).andReturn( methodMatchingInfos ).anyTimes();
    EasyMock.replay( moduleMatchingInfo );

    Desired3ParameterInterface converted = converter.convert( convertationObject, moduleMatchingInfo );

    assertThat( converted.doStuffWith3Params( "helloworld", 1, false ), equalTo( "1falsehelloworld" ) );
    assertThat( converted.doStuffWith3Params( " Permutation gibt es hier", 6, true ),
        equalTo( "6true Permutation gibt es hier" ) );

    checkInvokationOfAllNonParametrizedMethods( converted );
  }

  private Entry<ParamPosition, ModuleMatchingInfo> createPPMapEntry( int sourcePos, int targetPos,
      ModuleMatchingInfo createMMI_SameTypes ) {
    ParamPosition paramPosition = new ParamPosition( sourcePos, targetPos );
    Map<ParamPosition, ModuleMatchingInfo> tmpMap = new HashMap<>();
    tmpMap.put( paramPosition, createMMI_SameTypes );
    return tmpMap.entrySet().iterator().next();
  }

  private Map<ParamPosition, ModuleMatchingInfo> createMMIMap( Entry<ParamPosition, ModuleMatchingInfo>... infos ) {
    Map<ParamPosition, ModuleMatchingInfo> map = new HashMap<>();
    for ( int i = 0; i < infos.length; i++ ) {
      Entry<ParamPosition, ModuleMatchingInfo> entry = infos[i];
      map.put( entry.getKey(), entry.getValue() );
    }
    return map;
  }

  private ModuleMatchingInfo createMMI_SameTypes( Class<?> type ) {
    ModuleMatchingInfo mmi = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( mmi.getSource() ).andReturn( (Class) type ).anyTimes();
    EasyMock.expect( mmi.getTarget() ).andReturn( (Class) type ).anyTimes();
    EasyMock.expect( mmi.getMethodMatchingInfos() ).andReturn( new HashSet<>() ).anyTimes();
    EasyMock.replay( mmi );
    return mmi;
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
