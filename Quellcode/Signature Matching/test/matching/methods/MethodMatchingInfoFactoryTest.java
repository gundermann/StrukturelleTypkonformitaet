package matching.methods;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import matching.methods.testmethods.MethodPool;

public class MethodMatchingInfoFactoryTest {

  private TypeMatchingInfoFactory<?, ?> typeMatchingInfoFactory = new TypeMatchingInfoFactory<>( null, null );

  @Test
  public void create() {
    Method source = MethodPool.getMethod( "getOne" );
    Method target = MethodPool.getMethod( "getOneNativeWrapped" );
    MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( source, target );
    TypeMatchingInfo<?, ?> returnTypeMatchingInfo = typeMatchingInfoFactory.create();
    MethodMatchingInfo info = factory.create( returnTypeMatchingInfo, new HashMap<>() );
    assertThat( info, notNullValue() );
    assertThat( info.getReturnTypeMatchingInfo(), equalTo( returnTypeMatchingInfo ) );
    assertThat( info.getSource(), equalTo( source ) );
    assertThat( info.getTarget(), equalTo( target ) );
    assertThat( info.getArgumentTypeMatchingInfos(), notNullValue() );
    assertThat( info.getArgumentTypeMatchingInfos().keySet().isEmpty(), equalTo( true ) );

  }

  @Test
  public void createFromTypeMatchingInfos() {
    Method source = MethodPool.getMethod( "getOne" );
    Method target = MethodPool.getMethod( "getOneNativeWrapped" );
    MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( source, target );
    Collection<TypeMatchingInfo<?, ?>> returnTypeMatchingInfos = new ArrayList<>( 2 );
    returnTypeMatchingInfos.add( typeMatchingInfoFactory.create() );
    returnTypeMatchingInfos.add( typeMatchingInfoFactory.create() );
    Collection<Map<Integer, TypeMatchingInfo<?, ?>>> argumentTypesMatchingInfos = new ArrayList<>( 2 );
    argumentTypesMatchingInfos.add( new HashMap<>() );
    argumentTypesMatchingInfos.add( new HashMap<>() );
    Set<MethodMatchingInfo> infos = factory.createFromTypeMatchingInfos( returnTypeMatchingInfos,
        argumentTypesMatchingInfos );

    assertThat( infos, notNullValue() );
    assertThat( infos.size(), equalTo( 2 ) );
    Iterator<MethodMatchingInfo> iterator = infos.iterator();
    while ( iterator.hasNext() ) {
      MethodMatchingInfo info = iterator.next();
      assertThat( info.getSource(), equalTo( source ) );
      assertThat( info.getTarget(), equalTo( target ) );
    }
  }

  @Test
  public void createFromTypeMatchingInfos_Fail() {
    Method source = MethodPool.getMethod( "getOne" );
    Method target = MethodPool.getMethod( "getOneNativeWrapped" );
    MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( source, target );
    Collection<TypeMatchingInfo<?, ?>> returnTypeMatchingInfos = new ArrayList<>( 1 );
    returnTypeMatchingInfos.add( typeMatchingInfoFactory.create() );
    Collection<Map<Integer, TypeMatchingInfo<?, ?>>> argumentTypesMatchingInfos = new ArrayList<>( 2 );
    argumentTypesMatchingInfos.add( new HashMap<>() );
    argumentTypesMatchingInfos.add( new HashMap<>() );
    Set<MethodMatchingInfo> infos = factory.createFromTypeMatchingInfos( returnTypeMatchingInfos,
        argumentTypesMatchingInfos );

    assertThat( infos, notNullValue() );
    assertThat( infos.size(), equalTo( 0 ) );
  }

}
