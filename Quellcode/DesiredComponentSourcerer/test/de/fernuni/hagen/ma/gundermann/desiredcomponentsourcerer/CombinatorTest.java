package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CombinatorTest {

  @Test
  public void test2() {
    Map<String, Collection<String>> stringMap = new HashMap<>();
    stringMap.put( "A", Arrays.asList( "1", "2" ) );
    stringMap.put( "B", Arrays.asList( "3" ) );
    Collection<Collection<String>> generateCombis = new Combinator<String, String>().generateCombis( stringMap );
    assertThat( generateCombis.size(), equalTo( 2 ) );
  }

  @Test
  public void test4() {
    Map<String, Collection<String>> stringMap = new HashMap<>();
    stringMap.put( "A", Arrays.asList( "1", "2" ) );
    stringMap.put( "B", Arrays.asList( "3", "4" ) );
    Collection<Collection<String>> generateCombis = new Combinator<String, String>().generateCombis( stringMap );
    assertThat( generateCombis.size(), equalTo( 4 ) );
  }

  @Test
  public void test6() {
    Map<String, Collection<String>> stringMap = new HashMap<>();
    stringMap.put( "A", Arrays.asList( "1", "2" ) );
    stringMap.put( "B", Arrays.asList( "3", "4", "5" ) );
    Collection<Collection<String>> generateCombis = new Combinator<String, String>().generateCombis( stringMap );
    assertThat( generateCombis.size(), equalTo( 6 ) );
  }

  @Test
  public void test2of4List() {
    Collection<String> stringList = new ArrayList<>();
    stringList.add( "1" );
    stringList.add( "2" );
    stringList.add( "3" );
    stringList.add( "4" );
    Collection<Collection<String>> generateCombis = Combinator.generateCombis( stringList, 2 );
    assertThat( generateCombis.size(), equalTo( 6 ) );
  }

}
