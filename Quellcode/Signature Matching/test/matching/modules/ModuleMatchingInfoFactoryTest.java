package matching.modules;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

import matching.methods.MethodMatchingInfo;

public class ModuleMatchingInfoFactoryTest {

  @Test
  public void create() {
    ModuleMatchingInfoFactory factory = new ModuleMatchingInfoFactory( Object.class,
        Object.class );
    ModuleMatchingInfo matchingInfo = factory.create();
    assertThat( matchingInfo, notNullValue() );
    assertThat( matchingInfo.getRating(), equalTo( 0 ) );
    assertThat( matchingInfo.getMethodMatchingInfos(), notNullValue() );
    assertThat( matchingInfo.getMethodMatchingInfos().size(), equalTo( 0 ) );
    assertThat( matchingInfo.getSourceDelegateAttribute(), nullValue() );
    assertThat( matchingInfo.getTargetDelegateAttribute(), nullValue() );
  }

  @Test
  public void create_WithSourceDelegateAttr() {
    ModuleMatchingInfoFactory factory = new ModuleMatchingInfoFactory( Object.class,
        Object.class, "SOURCE" );
    ModuleMatchingInfo matchingInfo = factory.create();
    assertThat( matchingInfo, notNullValue() );
    assertThat( matchingInfo.getRating(), equalTo( 0 ) );
    assertThat( matchingInfo.getMethodMatchingInfos(), notNullValue() );
    assertThat( matchingInfo.getMethodMatchingInfos().size(), equalTo( 0 ) );
    assertThat( matchingInfo.getSourceDelegateAttribute(), notNullValue() );
    assertThat( matchingInfo.getSourceDelegateAttribute(), equalTo( "SOURCE" ) );
    assertThat( matchingInfo.getTargetDelegateAttribute(), nullValue() );
  }

  @Test
  public void create_WithTargetDelegateAttr() {
    ModuleMatchingInfoFactory factory = new ModuleMatchingInfoFactory( Object.class, "TARGET",
        Object.class );
    ModuleMatchingInfo matchingInfo = factory.create();
    assertThat( matchingInfo, notNullValue() );
    assertThat( matchingInfo.getRating(), equalTo( 0 ) );
    assertThat( matchingInfo.getMethodMatchingInfos(), notNullValue() );
    assertThat( matchingInfo.getMethodMatchingInfos().size(), equalTo( 0 ) );
    assertThat( matchingInfo.getSourceDelegateAttribute(), nullValue() );
    assertThat( matchingInfo.getTargetDelegateAttribute(), notNullValue() );
    assertThat( matchingInfo.getTargetDelegateAttribute(), equalTo( "TARGET" ) );
  }

  @Test
  public void create_emptyMethodMatchingInfos() {
    ModuleMatchingInfoFactory factory = new ModuleMatchingInfoFactory( Object.class,
        Object.class );
    ModuleMatchingInfo matchingInfo = factory.create( new HashSet<>() );
    assertThat( matchingInfo, notNullValue() );
    assertThat( matchingInfo.getRating(), equalTo( 0 ) );
    assertThat( matchingInfo.getMethodMatchingInfos(), notNullValue() );
    assertThat( matchingInfo.getMethodMatchingInfos().size(), equalTo( 0 ) );
  }

  @Test
  public void create_withMethodMatchingInfos() {
    ModuleMatchingInfoFactory factory = new ModuleMatchingInfoFactory( Object.class,
        Object.class );
    Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();
    methodMatchingInfos.add( mockMethodMatchingInfo() );
    ModuleMatchingInfo matchingInfo = factory.create( methodMatchingInfos );
    assertThat( matchingInfo, notNullValue() );
    // assertThat( matchingInfo.getRating(), equalTo( 0 ) );
    assertThat( matchingInfo.getMethodMatchingInfos(), notNullValue() );
    assertThat( matchingInfo.getMethodMatchingInfos().size(), equalTo( 1 ) );
  }

  @Test
  public void create_withMultipleMethodMatchingInfos() {
    ModuleMatchingInfoFactory factory = new ModuleMatchingInfoFactory( Object.class,
        Object.class );
    Set<MethodMatchingInfo> method1MatchingInfos = new HashSet<>();
    method1MatchingInfos.add( mockMethodMatchingInfo() );

    Set<MethodMatchingInfo> method2MatchingInfos = new HashSet<>();
    method2MatchingInfos.add( mockMethodMatchingInfo() );

    Map<Method, Set<MethodMatchingInfo>> possibleMethodMatches = new HashMap<>();
    possibleMethodMatches.put( Object.class.getMethods()[0], method1MatchingInfos );
    possibleMethodMatches.put( Object.class.getMethods()[1], method2MatchingInfos );

    Set<ModuleMatchingInfo> matchingInfos = factory.createFromMethodMatchingInfos( possibleMethodMatches );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 1 ) );
    // assertThat( matchingInfo.getRating(), equalTo( 0 ) );
    for ( ModuleMatchingInfo info : matchingInfos ) {
      assertThat( info.getMethodMatchingInfos(), notNullValue() );
      assertThat( info.getMethodMatchingInfos().size(), equalTo( 2 ) );
    }
  }

  @Test
  public void create_withMultipleMethodMatchingInfosForOneMethod() {
    ModuleMatchingInfoFactory factory = new ModuleMatchingInfoFactory( Object.class,
        Object.class );
    Set<MethodMatchingInfo> method1MatchingInfos = new HashSet<>();
    method1MatchingInfos.add( mockMethodMatchingInfo() );
    method1MatchingInfos.add( mockMethodMatchingInfo() );
    method1MatchingInfos.add( mockMethodMatchingInfo() );

    Set<MethodMatchingInfo> method2MatchingInfos = new HashSet<>();
    method2MatchingInfos.add( mockMethodMatchingInfo() );
    method2MatchingInfos.add( mockMethodMatchingInfo() );

    Set<MethodMatchingInfo> method3MatchingInfos = new HashSet<>();
    method3MatchingInfos.add( mockMethodMatchingInfo() );

    Map<Method, Set<MethodMatchingInfo>> possibleMethodMatches = new HashMap<>();
    possibleMethodMatches.put( Object.class.getMethods()[0], method1MatchingInfos );
    possibleMethodMatches.put( Object.class.getMethods()[1], method2MatchingInfos );
    possibleMethodMatches.put( Object.class.getMethods()[2], method3MatchingInfos );

    Set<ModuleMatchingInfo> matchingInfos = factory.createFromMethodMatchingInfos( possibleMethodMatches );
    assertThat( matchingInfos, notNullValue() );
    assertThat( matchingInfos.size(), equalTo( 6 ) );
    // assertThat( matchingInfo.getRating(), equalTo( 0 ) );
    for ( ModuleMatchingInfo info : matchingInfos ) {
      Set<MethodMatchingInfo> methodMatchingInfos = info.getMethodMatchingInfos();
      assertThat( methodMatchingInfos, notNullValue() );
      assertThat( methodMatchingInfos.size(), equalTo( 3 ) );
    }
  }

  private MethodMatchingInfo mockMethodMatchingInfo() {
    MethodMatchingInfo mock = EasyMock.createNiceMock( MethodMatchingInfo.class );
    EasyMock.replay( mock );
    return mock;
  }

}
