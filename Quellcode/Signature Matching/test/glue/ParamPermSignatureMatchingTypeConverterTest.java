package glue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

import matching.methods.MethodMatchingInfo;
import matching.types.TypeMatchingInfo;
import testcomponents.paramperm.Desired1ParameterInterface;
import testcomponents.paramperm.Desired2ParameterInterface;
import testcomponents.paramperm.Desired3ParameterInterface;
import testcomponents.paramperm.Offered1ParameterClass;
import testcomponents.paramperm.Offered2ParameterClass;
import testcomponents.paramperm.Offered3ParameterClass;

public class ParamPermSignatureMatchingTypeConverterTest {

  @Test
  public void oneParameter() throws NoSuchMethodException, SecurityException,
      IllegalArgumentException {
    Class<Desired1ParameterInterface> source = Desired1ParameterInterface.class;
    Class<Offered1ParameterClass> target = Offered1ParameterClass.class;
    Offered1ParameterClass convertationObject = new Offered1ParameterClass();
    TypeConverter<Desired1ParameterInterface> converter = new TypeConverter<>(
        source );

    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();

    MethodMatchingInfo mmi = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmi.getTarget() )
        .andReturn( target.getDeclaredMethod( "doOfferedWith1Param", String.class ) ).anyTimes();
    EasyMock.expect( mmi.getSource() ).andReturn( source.getMethod( "doStuffWith1Param", String.class ) )
        .anyTimes();
    EasyMock.expect( mmi.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( String.class ) )
        .anyTimes();
    EasyMock.expect( mmi.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap(
            MatchingInfoTestUtil.createPPMapEntry( 0, 0, MatchingInfoTestUtil.createMMI_SameTypes( String.class ) ) ) )
        .anyTimes();

    EasyMock.replay( mmi );

    methodMatchingInfos.add( mmi );

    TypeMatchingInfo moduleMatchingInfo = EasyMock.createNiceMock( TypeMatchingInfo.class );
    EasyMock.expect( moduleMatchingInfo.getMethodMatchingInfos() ).andReturn( methodMatchingInfos ).anyTimes();
    EasyMock.replay( moduleMatchingInfo );

    Map<Object, Collection<MethodMatchingInfo>> obj2MatchingInfo = new HashMap<>();
    obj2MatchingInfo.put( convertationObject, moduleMatchingInfo.getMethodMatchingInfos() );
    Desired1ParameterInterface converted = converter.convert( obj2MatchingInfo );

    assertThat( converted.doStuffWith1Param( "helloworld" ), equalTo( "helloworld" ) );
    assertThat( converted.doStuffWith1Param( "hier gibt es keine Permutation" ),
        equalTo( "hier gibt es keine Permutation" ) );

    checkInvokationOfAllNonParametrizedMethods( converted );
  }

  @Test
  public void towParameter() throws NoSuchMethodException, SecurityException,
      IllegalArgumentException {
    Class<Desired2ParameterInterface> source = Desired2ParameterInterface.class;
    Class<Offered2ParameterClass> target = Offered2ParameterClass.class;
    Offered2ParameterClass convertationObject = new Offered2ParameterClass();
    TypeConverter<Desired2ParameterInterface> converter = new TypeConverter<>(
        source );

    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();

    MethodMatchingInfo mmi = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmi.getTarget() )
        .andReturn( target.getDeclaredMethod( "doOfferedWith2Params", int.class, String.class ) ).anyTimes();
    EasyMock.expect( mmi.getSource() ).andReturn( source.getMethod( "doStuffWith2Params", String.class, int.class ) )
        .anyTimes();
    EasyMock.expect( mmi.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( String.class ) )
        .anyTimes();
    EasyMock.expect( mmi.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap(
            MatchingInfoTestUtil.createPPMapEntry( 0, 1, MatchingInfoTestUtil.createMMI_SameTypes( String.class ) ),
            MatchingInfoTestUtil.createPPMapEntry( 1, 0, MatchingInfoTestUtil.createMMI_SameTypes( int.class ) ) ) )
        .anyTimes();

    EasyMock.replay( mmi );

    methodMatchingInfos.add( mmi );

    TypeMatchingInfo moduleMatchingInfo = EasyMock.createNiceMock( TypeMatchingInfo.class );
    EasyMock.expect( moduleMatchingInfo.getMethodMatchingInfos() ).andReturn( methodMatchingInfos ).anyTimes();
    EasyMock.replay( moduleMatchingInfo );
    Map<Object, Collection<MethodMatchingInfo>> obj2MatchingInfo = new HashMap<>();
    obj2MatchingInfo.put( convertationObject, moduleMatchingInfo.getMethodMatchingInfos() );
    Desired2ParameterInterface converted = converter.convert( obj2MatchingInfo );

    assertThat( converted.doStuffWith2Params( "helloworld", 1 ), equalTo( "1helloworld" ) );
    assertThat( converted.doStuffWith2Params( " Permutation gibt es hier", 2 ),
        equalTo( "2 Permutation gibt es hier" ) );

    checkInvokationOfAllNonParametrizedMethods( converted );

  }

  @Test
  public void threeParameter() throws NoSuchMethodException, SecurityException,
      IllegalArgumentException {

    Class<Desired3ParameterInterface> source = Desired3ParameterInterface.class;
    Class<Offered3ParameterClass> target = Offered3ParameterClass.class;
    Offered3ParameterClass convertationObject = new Offered3ParameterClass();
    TypeConverter<Desired3ParameterInterface> converter = new TypeConverter<>(
        source );

    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();

    MethodMatchingInfo mmi = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmi.getTarget() )
        .andReturn( target.getDeclaredMethod( "doOfferedWith3Params", int.class, boolean.class, String.class ) )
        .anyTimes();
    EasyMock.expect( mmi.getSource() )
        .andReturn( source.getMethod( "doStuffWith3Params", String.class, int.class, boolean.class ) )
        .anyTimes();
    EasyMock.expect( mmi.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( String.class ) )
        .anyTimes();
    EasyMock.expect( mmi.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap(
            MatchingInfoTestUtil.createPPMapEntry( 2, 1, MatchingInfoTestUtil.createMMI_SameTypes( boolean.class ) ),
            MatchingInfoTestUtil.createPPMapEntry( 0, 2, MatchingInfoTestUtil.createMMI_SameTypes( String.class ) ),
            MatchingInfoTestUtil.createPPMapEntry( 1, 0, MatchingInfoTestUtil.createMMI_SameTypes( int.class ) ) ) )
        .anyTimes();

    EasyMock.replay( mmi );

    methodMatchingInfos.add( mmi );

    TypeMatchingInfo moduleMatchingInfo = EasyMock.createNiceMock( TypeMatchingInfo.class );
    EasyMock.expect( moduleMatchingInfo.getMethodMatchingInfos() ).andReturn( methodMatchingInfos ).anyTimes();
    EasyMock.replay( moduleMatchingInfo );

    Map<Object, Collection<MethodMatchingInfo>> obj2MatchingInfo = new HashMap<>();
    obj2MatchingInfo.put( convertationObject, moduleMatchingInfo.getMethodMatchingInfos() );
    Desired3ParameterInterface converted = converter.convert( obj2MatchingInfo );

    assertThat( converted.doStuffWith3Params( "helloworld", 1, false ), equalTo( "1falsehelloworld" ) );
    assertThat( converted.doStuffWith3Params( " Permutation gibt es hier", 6, true ),
        equalTo( "6true Permutation gibt es hier" ) );

    checkInvokationOfAllNonParametrizedMethods( converted );
  }

  private void checkInvokationOfAllNonParametrizedMethods( Object converted )
      throws IllegalArgumentException {
    for ( Method m : converted.getClass().getMethods() ) {
      if ( m.getParameterCount() == 0 ) {
        System.out.println( "try to invoke: " + m.getName() );
        // TODO mit wait() gibt es Probleme
        // m.invoke( converted );
      }
    }

  }

}
