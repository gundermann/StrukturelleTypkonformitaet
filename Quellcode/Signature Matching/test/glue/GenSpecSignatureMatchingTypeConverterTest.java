package glue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

import matching.methods.MethodMatchingInfo;
import matching.modules.ModuleMatchingInfo;
import testcomponents.genspec.DesiredGenInterface;
import testcomponents.genspec.DesiredSpecInterface;
import testcomponents.genspec.General;
import testcomponents.genspec.OfferedGenClass;
import testcomponents.genspec.OfferedSpecClass;
import testcomponents.genspec.Specific;

public class GenSpecSignatureMatchingTypeConverterTest {

  @Test
  public void genSourceSpecTargetMatching() throws NoSuchMethodException, SecurityException,
      IllegalArgumentException {
    Class<DesiredGenInterface> source = DesiredGenInterface.class;
    Class<OfferedSpecClass> target = OfferedSpecClass.class;
    OfferedSpecClass convertationObject = new OfferedSpecClass();
    SignatureMatchingTypeConverter<DesiredGenInterface> converter = new SignatureMatchingTypeConverter<>( source );

    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();

    MethodMatchingInfo mmiConcat = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiConcat.getTarget() )
        .andReturn( target.getDeclaredMethod( "concat", Specific.class, Specific.class ) ).anyTimes();
    EasyMock.expect( mmiConcat.getSource() ).andReturn( source.getMethod( "concat", General.class, General.class ) )
        .anyTimes();
    EasyMock.expect( mmiConcat.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( String.class ) )
        .anyTimes();
    EasyMock.expect( mmiConcat.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( createMMI_G2S(), createMMI_G2S() ) ).anyTimes();

    MethodMatchingInfo mmiAddInt = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiAddInt.getTarget() )
        .andReturn( target.getDeclaredMethod( "add", Specific.class, Integer.class ) )
        .anyTimes();
    EasyMock.expect( mmiAddInt.getSource() ).andReturn( source.getMethod( "add", General.class, Integer.class ) )
        .anyTimes();
    EasyMock.expect( mmiAddInt.getReturnTypeMatchingInfo() ).andReturn( createMMI_S2G() ).anyTimes();
    EasyMock.expect( mmiAddInt.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( createMMI_G2S(),
            MatchingInfoTestUtil.createMMI_SameTypes( Integer.class ) ) )
        .anyTimes();

    MethodMatchingInfo mmiAddGen = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiAddGen.getTarget() )
        .andReturn( target.getDeclaredMethod( "add", Specific.class, Specific.class ) ).anyTimes();
    EasyMock.expect( mmiAddGen.getSource() ).andReturn( source.getMethod( "add", General.class, General.class ) )
        .anyTimes();
    EasyMock.expect( mmiAddGen.getReturnTypeMatchingInfo() ).andReturn( createMMI_S2G() ).anyTimes();
    EasyMock.expect( mmiAddGen.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( createMMI_G2S(), createMMI_G2S() ) ).anyTimes();

    MethodMatchingInfo mmiGetLong = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiGetLong.getTarget() )
        .andReturn( target.getDeclaredMethod( "getBoxedLongAttr", Specific.class ) )
        .anyTimes();
    EasyMock.expect( mmiGetLong.getSource() ).andReturn( source.getMethod( "getBoxedLongAttr", General.class ) )
        .anyTimes();
    EasyMock.expect( mmiGetLong.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( Long.class ) ).anyTimes();
    EasyMock.expect( mmiGetLong.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( createMMI_G2S() ) )
        .anyTimes();

    MethodMatchingInfo mmiGetNull = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiGetNull.getTarget() )
        .andReturn( target.getDeclaredMethod( "getNull" ) )
        .anyTimes();
    EasyMock.expect( mmiGetNull.getSource() ).andReturn( source.getMethod( "getNull" ) )
        .anyTimes();
    EasyMock.expect( mmiGetNull.getReturnTypeMatchingInfo() )
        .andReturn( createMMI_S2G() ).anyTimes();
    EasyMock.expect( mmiGetNull.getArgumentTypeMatchingInfos() )
        .andReturn( new HashMap<>() )
        .anyTimes();

    EasyMock.replay( mmiConcat, mmiAddInt, mmiAddGen, mmiGetLong, mmiGetNull );

    methodMatchingInfos.add( mmiConcat );
    methodMatchingInfos.add( mmiAddInt );
    methodMatchingInfos.add( mmiAddGen );
    methodMatchingInfos.add( mmiGetLong );
    methodMatchingInfos.add( mmiGetNull );

    ModuleMatchingInfo moduleMatchingInfo = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( moduleMatchingInfo.getMethodMatchingInfos() ).andReturn( methodMatchingInfos ).anyTimes();
    EasyMock.replay( moduleMatchingInfo );

    DesiredGenInterface converted = converter.convert( convertationObject, moduleMatchingInfo );
    General hello_1_10_Gen = new General( "hello", 1, 10L );
    General world_5_44_Gen = new General( "world", 5, 44L );
    Specific hello_2_Spec = new Specific( "hello", 2 );
    Specific world_99_Spec = new Specific( "world", 99 );

    // Test Method: getNull
    assertThat( converted.getNull(), nullValue() );

    // Test Method: concat
    assertThat( converted.concat( hello_1_10_Gen, world_5_44_Gen ), equalTo( "helloworld" ) );
    assertThat( converted.concat( hello_2_Spec, world_99_Spec ), equalTo( "helloworld" ) );

    // Test Method: add
    General result_2 = converted.add( hello_1_10_Gen, 1 );
    assertThat( result_2.getIntAttr(), equalTo( 2 ) );
    assertThat( result_2.getBoxedLongAttr(), equalTo( 2L ) );

    General result_109 = converted.add( world_99_Spec, 10 );
    assertThat( result_109.getIntAttr(), equalTo( 109 ) );
    assertThat( result_109.getBoxedLongAttr(), equalTo( 109L ) );

    General result_6 = converted.add( hello_1_10_Gen, world_5_44_Gen );
    assertThat( result_6.getIntAttr(), equalTo( 6 ) );
    assertThat( result_6.getBoxedLongAttr(), equalTo( 6L ) );

    General result_101 = converted.add( world_99_Spec, hello_2_Spec );
    assertThat( result_101.getIntAttr(), equalTo( 101 ) );
    assertThat( result_101.getBoxedLongAttr(), equalTo( 101L ) );

    General result_3 = converted.add( hello_1_10_Gen, hello_2_Spec );
    assertThat( result_3.getIntAttr(), equalTo( 3 ) );
    assertThat( result_3.getBoxedLongAttr(), equalTo( 3L ) );

    // Test Method: getBoxedLongAttr
    assertThat( converted.getBoxedLongAttr( world_5_44_Gen ), equalTo( 44L ) );
    assertThat( converted.getBoxedLongAttr( hello_1_10_Gen ), equalTo( 10L ) );
    assertThat( converted.getBoxedLongAttr( world_99_Spec ), equalTo( 99L ) );
    assertThat( converted.getBoxedLongAttr( hello_2_Spec ), equalTo( 2L ) );

    checkInvokationOfAllNonParametrizedMethods( converted );
  }

  @Test
  public void genSourceGenTargetMatching() throws NoSuchMethodException, SecurityException,
      IllegalArgumentException {
    Class<DesiredGenInterface> source = DesiredGenInterface.class;
    Class<OfferedGenClass> target = OfferedGenClass.class;
    OfferedGenClass convertationObject = new OfferedGenClass();
    SignatureMatchingTypeConverter<DesiredGenInterface> converter = new SignatureMatchingTypeConverter<>( source );

    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();

    MethodMatchingInfo mmiConcat = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiConcat.getTarget() )
        .andReturn( target.getDeclaredMethod( "concat", General.class, General.class ) ).anyTimes();
    EasyMock.expect( mmiConcat.getSource() ).andReturn( source.getMethod( "concat", General.class, General.class ) )
        .anyTimes();
    EasyMock.expect( mmiConcat.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( String.class ) )
        .anyTimes();
    EasyMock.expect( mmiConcat.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( MatchingInfoTestUtil.createMMI_SameTypes( General.class ),
            MatchingInfoTestUtil.createMMI_SameTypes( General.class ) ) )
        .anyTimes();

    MethodMatchingInfo mmiAddInt = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiAddInt.getTarget() )
        .andReturn( target.getDeclaredMethod( "add", General.class, Integer.class ) )
        .anyTimes();
    EasyMock.expect( mmiAddInt.getSource() ).andReturn( source.getMethod( "add", General.class, Integer.class ) )
        .anyTimes();
    EasyMock.expect( mmiAddInt.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( General.class ) )
        .anyTimes();
    EasyMock.expect( mmiAddInt.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( MatchingInfoTestUtil.createMMI_SameTypes( General.class ),
            MatchingInfoTestUtil.createMMI_SameTypes( Integer.class ) ) )
        .anyTimes();

    MethodMatchingInfo mmiAddGen = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiAddGen.getTarget() )
        .andReturn( target.getDeclaredMethod( "add", General.class, General.class ) ).anyTimes();
    EasyMock.expect( mmiAddGen.getSource() ).andReturn( source.getMethod( "add", General.class, General.class ) )
        .anyTimes();
    EasyMock.expect( mmiAddGen.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( General.class ) )
        .anyTimes();
    EasyMock.expect( mmiAddGen.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( MatchingInfoTestUtil.createMMI_SameTypes( General.class ),
            MatchingInfoTestUtil.createMMI_SameTypes( General.class ) ) )
        .anyTimes();

    MethodMatchingInfo mmiGetLong = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiGetLong.getTarget() )
        .andReturn( target.getDeclaredMethod( "getBoxedLongAttr", General.class ) )
        .anyTimes();
    EasyMock.expect( mmiGetLong.getSource() ).andReturn( source.getMethod( "getBoxedLongAttr", General.class ) )
        .anyTimes();
    EasyMock.expect( mmiGetLong.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( Long.class ) ).anyTimes();
    EasyMock.expect( mmiGetLong.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( MatchingInfoTestUtil.createMMI_SameTypes( General.class ) ) )
        .anyTimes();

    MethodMatchingInfo mmiGetNull = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiGetNull.getTarget() )
        .andReturn( target.getDeclaredMethod( "getNull" ) )
        .anyTimes();
    EasyMock.expect( mmiGetNull.getSource() ).andReturn( source.getMethod( "getNull" ) )
        .anyTimes();
    EasyMock.expect( mmiGetNull.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( General.class ) ).anyTimes();
    EasyMock.expect( mmiGetNull.getArgumentTypeMatchingInfos() )
        .andReturn( new HashMap<>() )
        .anyTimes();

    EasyMock.replay( mmiConcat, mmiAddInt, mmiAddGen, mmiGetLong, mmiGetNull );

    methodMatchingInfos.add( mmiConcat );
    methodMatchingInfos.add( mmiAddInt );
    methodMatchingInfos.add( mmiAddGen );
    methodMatchingInfos.add( mmiGetLong );
    methodMatchingInfos.add( mmiGetNull );

    ModuleMatchingInfo moduleMatchingInfo = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( moduleMatchingInfo.getMethodMatchingInfos() ).andReturn( methodMatchingInfos ).anyTimes();
    EasyMock.replay( moduleMatchingInfo );

    DesiredGenInterface converted = converter.convert( convertationObject, moduleMatchingInfo );
    General hello_1_10_Gen = new General( "hello", 1, 10L );
    General world_5_44_Gen = new General( "world", 5, 44L );
    Specific hello_2_Spec = new Specific( "hello", 2 );
    Specific world_99_Spec = new Specific( "world", 99 );

    // Test Method: getNull
    assertThat( converted.getNull(), nullValue() );

    // Test Method: concat
    assertThat( converted.concat( hello_1_10_Gen, world_5_44_Gen ), equalTo( "helloworld" ) );
    assertThat( converted.concat( hello_2_Spec, world_99_Spec ), equalTo( "helloworld" ) );

    // Test Method: add
    General result_2 = converted.add( hello_1_10_Gen, 1 );
    assertThat( result_2.getIntAttr(), equalTo( 2 ) );
    assertThat( result_2.getBoxedLongAttr(), equalTo( hello_1_10_Gen.getBoxedLongAttr() ) );

    General result_109 = converted.add( world_99_Spec, 10 );
    assertThat( result_109.getIntAttr(), equalTo( 109 ) );
    assertThat( result_109.getBoxedLongAttr(), equalTo( world_99_Spec.getBoxedLongAttr() ) );

    General result_6 = converted.add( hello_1_10_Gen, world_5_44_Gen );
    assertThat( result_6.getIntAttr(), equalTo( 6 ) );
    assertThat( result_6.getBoxedLongAttr(),
        equalTo( hello_1_10_Gen.getBoxedLongAttr() + world_5_44_Gen.getBoxedLongAttr() ) );

    General result_101 = converted.add( world_99_Spec, hello_2_Spec );
    assertThat( result_101.getIntAttr(), equalTo( 101 ) );
    assertThat( result_101.getBoxedLongAttr(),
        equalTo( world_99_Spec.getBoxedLongAttr() + hello_2_Spec.getBoxedLongAttr() ) );

    General result_3 = converted.add( hello_1_10_Gen, hello_2_Spec );
    assertThat( result_3.getIntAttr(), equalTo( 3 ) );
    assertThat( result_3.getBoxedLongAttr(),
        equalTo( hello_1_10_Gen.getBoxedLongAttr() + hello_2_Spec.getBoxedLongAttr() ) );

    // Test Method: getBoxedLongAttr
    assertThat( converted.getBoxedLongAttr( world_5_44_Gen ), equalTo( 44L ) );
    assertThat( converted.getBoxedLongAttr( hello_1_10_Gen ), equalTo( 10L ) );
    assertThat( converted.getBoxedLongAttr( world_99_Spec ), equalTo( 99L ) );
    assertThat( converted.getBoxedLongAttr( hello_2_Spec ), equalTo( 2L ) );

    checkInvokationOfAllNonParametrizedMethods( converted );
  }

  @Test
  public void specSourceSpecTargetMatching() throws NoSuchMethodException, SecurityException,
      IllegalArgumentException {
    Class<DesiredSpecInterface> source = DesiredSpecInterface.class;
    Class<OfferedSpecClass> target = OfferedSpecClass.class;
    OfferedSpecClass convertationObject = new OfferedSpecClass();
    SignatureMatchingTypeConverter<DesiredSpecInterface> converter = new SignatureMatchingTypeConverter<>( source );

    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();

    MethodMatchingInfo mmiConcat = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiConcat.getTarget() )
        .andReturn( target.getDeclaredMethod( "concat", Specific.class, Specific.class ) ).anyTimes();
    EasyMock.expect( mmiConcat.getSource() ).andReturn( source.getMethod( "concat", Specific.class, Specific.class ) )
        .anyTimes();
    EasyMock.expect( mmiConcat.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( String.class ) )
        .anyTimes();
    EasyMock.expect( mmiConcat.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( MatchingInfoTestUtil.createMMI_SameTypes( Specific.class ),
            MatchingInfoTestUtil.createMMI_SameTypes( Specific.class ) ) )
        .anyTimes();

    MethodMatchingInfo mmiAddInt = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiAddInt.getTarget() )
        .andReturn( target.getDeclaredMethod( "add", Specific.class, Integer.class ) )
        .anyTimes();
    EasyMock.expect( mmiAddInt.getSource() ).andReturn( source.getMethod( "add", Specific.class, Integer.class ) )
        .anyTimes();
    EasyMock.expect( mmiAddInt.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( Specific.class ) )
        .anyTimes();
    EasyMock.expect( mmiAddInt.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( MatchingInfoTestUtil.createMMI_SameTypes( Specific.class ),
            MatchingInfoTestUtil.createMMI_SameTypes( Integer.class ) ) )
        .anyTimes();

    MethodMatchingInfo mmiAddGen = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiAddGen.getTarget() )
        .andReturn( target.getDeclaredMethod( "add", Specific.class, Specific.class ) ).anyTimes();
    EasyMock.expect( mmiAddGen.getSource() ).andReturn( source.getMethod( "add", Specific.class, Specific.class ) )
        .anyTimes();
    EasyMock.expect( mmiAddGen.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( Specific.class ) )
        .anyTimes();
    EasyMock.expect( mmiAddGen.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( MatchingInfoTestUtil.createMMI_SameTypes( Specific.class ),
            MatchingInfoTestUtil.createMMI_SameTypes( Specific.class ) ) )
        .anyTimes();

    MethodMatchingInfo mmiGetLong = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiGetLong.getTarget() )
        .andReturn( target.getDeclaredMethod( "getBoxedLongAttr", Specific.class ) )
        .anyTimes();
    EasyMock.expect( mmiGetLong.getSource() ).andReturn( source.getMethod( "getBoxedLongAttr", Specific.class ) )
        .anyTimes();
    EasyMock.expect( mmiGetLong.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( Long.class ) ).anyTimes();
    EasyMock.expect( mmiGetLong.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( MatchingInfoTestUtil.createMMI_SameTypes( Specific.class ) ) )
        .anyTimes();

    MethodMatchingInfo mmiAnd = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiAnd.getTarget() ).andReturn( target.getDeclaredMethod( "and", Specific.class, Boolean.class ) )
        .anyTimes();
    EasyMock.expect( mmiAnd.getSource() ).andReturn( source.getMethod( "and", Specific.class, Boolean.class ) )
        .anyTimes();
    EasyMock.expect( mmiAnd.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( Boolean.class ) ).anyTimes();
    EasyMock.expect( mmiAnd.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( MatchingInfoTestUtil.createMMI_SameTypes( Specific.class ),
            MatchingInfoTestUtil.createMMI_SameTypes( Boolean.class ) ) )
        .anyTimes();

    MethodMatchingInfo mmiGetNull = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiGetNull.getTarget() )
        .andReturn( target.getDeclaredMethod( "getNull" ) )
        .anyTimes();
    EasyMock.expect( mmiGetNull.getSource() ).andReturn( source.getMethod( "getNull" ) )
        .anyTimes();
    EasyMock.expect( mmiGetNull.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( Specific.class ) ).anyTimes();
    EasyMock.expect( mmiGetNull.getArgumentTypeMatchingInfos() )
        .andReturn( new HashMap<>() )
        .anyTimes();

    EasyMock.replay( mmiConcat, mmiAddInt, mmiAddGen, mmiGetLong, mmiGetNull, mmiAnd );

    methodMatchingInfos.add( mmiConcat );
    methodMatchingInfos.add( mmiAddInt );
    methodMatchingInfos.add( mmiAddGen );
    methodMatchingInfos.add( mmiGetLong );
    methodMatchingInfos.add( mmiGetNull );
    methodMatchingInfos.add( mmiAnd );

    ModuleMatchingInfo moduleMatchingInfo = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( moduleMatchingInfo.getMethodMatchingInfos() ).andReturn( methodMatchingInfos ).anyTimes();
    EasyMock.replay( moduleMatchingInfo );

    DesiredSpecInterface converted = converter.convert( convertationObject, moduleMatchingInfo );
    Specific hello_true_Spec = new Specific( "hello", true );
    Specific world_true_Spec = new Specific( "world", true );
    Specific hello_2_false_Spec = new Specific( "hello", 2 );
    Specific world_99_false_Spec = new Specific( "world", 99 );

    // Test Method: getNull
    assertThat( converted.getNull(), nullValue() );

    // Test Method: concat
    assertThat( converted.concat( hello_true_Spec, world_true_Spec ), equalTo( "helloworld" ) );
    assertThat( converted.concat( hello_2_false_Spec, world_99_false_Spec ), equalTo( "helloworld" ) );

    // Test Method: add
    Specific result_109 = converted.add( world_99_false_Spec, 10 );
    assertThat( result_109.getIntAttr(), equalTo( 109 ) );
    assertThat( result_109.getBoxedLongAttr(), equalTo( 109L ) );

    Specific result_101 = converted.add( world_99_false_Spec, hello_2_false_Spec );
    assertThat( result_101.getIntAttr(), equalTo( 101 ) );
    assertThat( result_101.getBoxedLongAttr(), equalTo( 101L ) );

    // Test Method: getBoxedLongAttr
    assertThat( converted.getBoxedLongAttr( hello_true_Spec ), nullValue() );
    assertThat( converted.getBoxedLongAttr( world_true_Spec ), nullValue() );
    assertThat( converted.getBoxedLongAttr( hello_2_false_Spec ), equalTo( 2L ) );
    assertThat( converted.getBoxedLongAttr( world_99_false_Spec ), equalTo( 99L ) );

    // Test Method: and
    assertThat( converted.and( world_true_Spec, false ), equalTo( false ) );
    assertThat( converted.and( hello_true_Spec, true ), equalTo( true ) );
    assertThat( converted.and( hello_2_false_Spec, false ), equalTo( false ) );
    assertThat( converted.and( world_99_false_Spec, true ), equalTo( false ) );

    checkInvokationOfAllNonParametrizedMethods( converted );
  }

  @Test
  public void specSourceGenTargetMatching() throws NoSuchMethodException, SecurityException,
      IllegalArgumentException {
    Class<DesiredSpecInterface> source = DesiredSpecInterface.class;
    Class<OfferedGenClass> target = OfferedGenClass.class;
    OfferedGenClass convertationObject = new OfferedGenClass();
    SignatureMatchingTypeConverter<DesiredSpecInterface> converter = new SignatureMatchingTypeConverter<>( source );

    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();

    MethodMatchingInfo mmiConcat = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiConcat.getTarget() )
        .andReturn( target.getDeclaredMethod( "concat", General.class, General.class ) ).anyTimes();
    EasyMock.expect( mmiConcat.getSource() ).andReturn( source.getMethod( "concat", Specific.class, Specific.class ) )
        .anyTimes();
    EasyMock.expect( mmiConcat.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( String.class ) )
        .anyTimes();
    EasyMock.expect( mmiConcat.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( createMMI_S2G(), createMMI_S2G() ) )
        .anyTimes();

    MethodMatchingInfo mmiAddInt = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiAddInt.getTarget() )
        .andReturn( target.getDeclaredMethod( "add", General.class, Integer.class ) )
        .anyTimes();
    EasyMock.expect( mmiAddInt.getSource() ).andReturn( source.getMethod( "add", Specific.class, Integer.class ) )
        .anyTimes();
    EasyMock.expect( mmiAddInt.getReturnTypeMatchingInfo() ).andReturn( createMMI_G2S() )
        .anyTimes();
    EasyMock.expect( mmiAddInt.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( createMMI_S2G(),
            MatchingInfoTestUtil.createMMI_SameTypes( Integer.class ) ) )
        .anyTimes();

    MethodMatchingInfo mmiAddGen = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiAddGen.getTarget() )
        .andReturn( target.getDeclaredMethod( "add", General.class, General.class ) ).anyTimes();
    EasyMock.expect( mmiAddGen.getSource() ).andReturn( source.getMethod( "add", Specific.class, Specific.class ) )
        .anyTimes();
    EasyMock.expect( mmiAddGen.getReturnTypeMatchingInfo() ).andReturn( createMMI_G2S() )
        .anyTimes();
    EasyMock.expect( mmiAddGen.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( createMMI_S2G(), createMMI_S2G() ) )
        .anyTimes();

    MethodMatchingInfo mmiGetLong = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiGetLong.getTarget() )
        .andReturn( target.getDeclaredMethod( "getBoxedLongAttr", General.class ) )
        .anyTimes();
    EasyMock.expect( mmiGetLong.getSource() ).andReturn( source.getMethod( "getBoxedLongAttr", Specific.class ) )
        .anyTimes();
    EasyMock.expect( mmiGetLong.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( Long.class ) ).anyTimes();
    EasyMock.expect( mmiGetLong.getArgumentTypeMatchingInfos() )
        .andReturn( MatchingInfoTestUtil.createMMIMap( createMMI_S2G() ) )
        .anyTimes();
    MethodMatchingInfo mmiGetNull = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( mmiGetNull.getTarget() )
        .andReturn( target.getDeclaredMethod( "getNull" ) )
        .anyTimes();
    EasyMock.expect( mmiGetNull.getSource() ).andReturn( source.getMethod( "getNull" ) )
        .anyTimes();
    EasyMock.expect( mmiGetNull.getReturnTypeMatchingInfo() )
        .andReturn( createMMI_G2S() ).anyTimes();
    EasyMock.expect( mmiGetNull.getArgumentTypeMatchingInfos() )
        .andReturn( new HashMap<>() )
        .anyTimes();

    EasyMock.replay( mmiConcat, mmiAddInt, mmiAddGen,
        mmiGetLong,
        mmiGetNull );

    methodMatchingInfos.add( mmiConcat );
    methodMatchingInfos.add( mmiAddInt );
    methodMatchingInfos.add( mmiAddGen );
    methodMatchingInfos.add( mmiGetLong );
    methodMatchingInfos.add( mmiGetNull );

    ModuleMatchingInfo moduleMatchingInfo = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( moduleMatchingInfo.getMethodMatchingInfos() ).andReturn( methodMatchingInfos ).anyTimes();
    EasyMock.replay( moduleMatchingInfo );

    DesiredSpecInterface converted = converter.convert( convertationObject, moduleMatchingInfo );
    Specific hello_true_Spec = new Specific( "hello", true );
    Specific world_true_Spec = new Specific( "world", true );
    Specific hello_2_false_Spec = new Specific( "hello", 2 );
    Specific world_99_false_Spec = new Specific( "world", 99 );

    // Test Method: getNull
    assertThat( converted.getNull(), nullValue() );

    // Test Method: concat
    assertThat( converted.concat( hello_true_Spec, world_true_Spec ), equalTo( "helloworld" ) );
    assertThat( converted.concat( hello_2_false_Spec, world_99_false_Spec ), equalTo( "helloworld" ) );

    // Test Method: add
    Specific result_109 = converted.add( world_99_false_Spec, 10 );
    assertThat( result_109.getIntAttr(), equalTo( 109 ) );
    assertThat( result_109.getBoxedLongAttr(), equalTo( 99L ) );

    Specific result_101 = converted.add( world_99_false_Spec, hello_2_false_Spec );
    assertThat( result_101.getIntAttr(), equalTo( 101 ) );
    assertThat( result_101.getBoxedLongAttr(), equalTo( 101L ) );

    // Test Method: getBoxedLongAttr
    assertThat( converted.getBoxedLongAttr( hello_true_Spec ), nullValue() );
    assertThat( converted.getBoxedLongAttr( world_true_Spec ), nullValue() );
    assertThat( converted.getBoxedLongAttr( hello_2_false_Spec ), equalTo( 2L ) );
    assertThat( converted.getBoxedLongAttr( world_99_false_Spec ), equalTo( 99L ) );

    checkInvokationOfAllNonParametrizedMethods( converted );
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  private ModuleMatchingInfo createMMI_G2S() throws NoSuchMethodException, SecurityException {
    ModuleMatchingInfo mmit = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( mmit.getTarget() ).andReturn( (Class) Specific.class ).anyTimes();
    EasyMock.expect( mmit.getSource() ).andReturn( (Class) General.class ).anyTimes();
    EasyMock.expect( mmit.getConverterCreator() ).andReturn( ProxyCreatorFactories.getClassFactoryCreator() )
        .anyTimes();
    Set<MethodMatchingInfo> methodInfos = new HashSet<>();
    MethodMatchingInfo concatMethod = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( concatMethod.getTarget() ).andReturn( Specific.class.getMethod( "getStringAttr" ) ).anyTimes();
    EasyMock.expect( concatMethod.getSource() ).andReturn( General.class.getMethod( "getStringAttr" ) ).anyTimes();
    EasyMock.expect( concatMethod.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( String.class ) )
        .anyTimes();
    EasyMock.expect( concatMethod.getArgumentTypeMatchingInfos() ).andReturn( new HashMap<>() ).anyTimes();
    EasyMock.replay( concatMethod );
    methodInfos.add( concatMethod );

    MethodMatchingInfo addMethod = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( addMethod.getTarget() ).andReturn( Specific.class.getMethod( "getIntAttr" ) ).anyTimes();
    EasyMock.expect( addMethod.getSource() ).andReturn( General.class.getMethod( "getIntAttr" ) ).anyTimes();
    EasyMock.expect( addMethod.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( Integer.class ) )
        .anyTimes();
    EasyMock.expect( addMethod.getArgumentTypeMatchingInfos() ).andReturn( new HashMap<>() ).anyTimes();
    EasyMock.replay( addMethod );
    methodInfos.add( addMethod );

    MethodMatchingInfo getLongMethod = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( getLongMethod.getTarget() ).andReturn( Specific.class.getMethod( "getBoxedLongAttr" ) ).anyTimes();
    EasyMock.expect( getLongMethod.getSource() ).andReturn( General.class.getMethod( "getBoxedLongAttr" ) ).anyTimes();
    EasyMock.expect( getLongMethod.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( Long.class ) )
        .anyTimes();
    EasyMock.expect( getLongMethod.getArgumentTypeMatchingInfos() ).andReturn( new HashMap<>() ).anyTimes();
    EasyMock.replay( getLongMethod );
    methodInfos.add( getLongMethod );

    EasyMock.expect( mmit.getMethodMatchingInfos() ).andReturn( methodInfos ).anyTimes();
    EasyMock.replay( mmit );
    return mmit;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  private ModuleMatchingInfo createMMI_S2G() throws NoSuchMethodException, SecurityException {
    ModuleMatchingInfo mmit = EasyMock.createNiceMock( ModuleMatchingInfo.class );
    EasyMock.expect( mmit.getTarget() ).andReturn( (Class) General.class ).anyTimes();
    EasyMock.expect( mmit.getSource() ).andReturn( (Class) Specific.class ).anyTimes();
    EasyMock.expect( mmit.getConverterCreator() ).andReturn( ProxyCreatorFactories.getClassFactoryCreator() )
        .anyTimes();

    Set<MethodMatchingInfo> methodInfos = new HashSet<>();

    MethodMatchingInfo concatMethod = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( concatMethod.getTarget() ).andReturn( General.class.getMethod( "getStringAttr" ) ).anyTimes();
    EasyMock.expect( concatMethod.getSource() ).andReturn( Specific.class.getMethod( "getStringAttr" ) ).anyTimes();
    EasyMock.expect( concatMethod.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( String.class ) )
        .anyTimes();
    EasyMock.expect( concatMethod.getArgumentTypeMatchingInfos() ).andReturn( new HashMap<>() ).anyTimes();
    EasyMock.replay( concatMethod );
    methodInfos.add( concatMethod );

    MethodMatchingInfo addMethod = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( addMethod.getTarget() ).andReturn( General.class.getMethod( "getIntAttr" ) ).anyTimes();
    EasyMock.expect( addMethod.getSource() ).andReturn( Specific.class.getMethod( "getIntAttr" ) ).anyTimes();
    EasyMock.expect( addMethod.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( Integer.class ) )
        .anyTimes();
    EasyMock.expect( addMethod.getArgumentTypeMatchingInfos() ).andReturn( new HashMap<>() ).anyTimes();
    EasyMock.replay( addMethod );
    methodInfos.add( addMethod );

    MethodMatchingInfo getLongMethod = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.expect( getLongMethod.getTarget() ).andReturn( General.class.getMethod( "getBoxedLongAttr" ) ).anyTimes();
    EasyMock.expect( getLongMethod.getSource() ).andReturn( Specific.class.getMethod( "getBoxedLongAttr" ) ).anyTimes();
    EasyMock.expect( getLongMethod.getReturnTypeMatchingInfo() )
        .andReturn( MatchingInfoTestUtil.createMMI_SameTypes( Long.class ) )
        .anyTimes();
    EasyMock.expect( getLongMethod.getArgumentTypeMatchingInfos() ).andReturn( new HashMap<>() ).anyTimes();
    EasyMock.replay( getLongMethod );
    methodInfos.add( getLongMethod );

    EasyMock.expect( mmit.getMethodMatchingInfos() ).andReturn( methodInfos ).anyTimes();
    EasyMock.replay( mmit );
    return mmit;
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
