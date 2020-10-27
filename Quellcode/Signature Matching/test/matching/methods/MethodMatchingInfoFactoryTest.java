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
import matching.modules.ModuleMatchingInfo;
import matching.modules.ModuleMatchingInfoFactory;

public class MethodMatchingInfoFactoryTest {

  private ModuleMatchingInfoFactory<?, ?> moduleMatchingInfoFactory = new ModuleMatchingInfoFactory<>( null, null );

  @Test
  public void create() {
    Method source = MethodPool.getMethod( "getOne" );
    Method target = MethodPool.getMethod( "getOneNativeWrapped" );
    MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( target, source );
    ModuleMatchingInfo<?> returnTypeMatchingInfo = moduleMatchingInfoFactory.create();
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
    MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( target, source );
    Collection<ModuleMatchingInfo<?>> returnTypeMatchingInfos = new ArrayList<>( 2 );
    returnTypeMatchingInfos.add( moduleMatchingInfoFactory.create() );
    returnTypeMatchingInfos.add( moduleMatchingInfoFactory.create() );
    Collection<Map<Integer, ModuleMatchingInfo<?>>> argumentTypesMatchingInfos = new ArrayList<>( 2 );
    argumentTypesMatchingInfos.add( new HashMap<>() );
    argumentTypesMatchingInfos.add( new HashMap<>() );
    Set<MethodMatchingInfo> infos = factory.createFromTypeMatchingInfos( returnTypeMatchingInfos,
        argumentTypesMatchingInfos );

    assertThat( infos, notNullValue() );
    assertThat( infos.size(), equalTo( 4 ) );
    Iterator<MethodMatchingInfo> iterator = infos.iterator();
    while ( iterator.hasNext() ) {
      MethodMatchingInfo info = iterator.next();
      assertThat( info.getSource(), equalTo( source ) );
      assertThat( info.getTarget(), equalTo( target ) );
    }
  }

  @Test
  public void createFromTypeMatchingInfos_keineErzeugtWeilEsKeinenReturnTypeGibt() {
    Method source = MethodPool.getMethod( "getOne" );
    Method target = MethodPool.getMethod( "getOneNativeWrapped" );
    MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory( target, source );
    Collection<ModuleMatchingInfo<?>> returnTypeMatchingInfos = new ArrayList<>( 0 );
    Collection<Map<Integer, ModuleMatchingInfo<?>>> argumentTypesMatchingInfos = new ArrayList<>( 2 );
    argumentTypesMatchingInfos.add( new HashMap<>() );
    argumentTypesMatchingInfos.add( new HashMap<>() );
    Set<MethodMatchingInfo> infos = factory.createFromTypeMatchingInfos( returnTypeMatchingInfos,
        argumentTypesMatchingInfos );

    assertThat( infos, notNullValue() );
    assertThat( infos.size(), equalTo( 0 ) );
  }

}
