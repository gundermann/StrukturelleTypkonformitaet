/*   $HeadURL$
 * ----------------------------------------------------------------------------
 *     (c) by data experts gmbh
 *            Woldegker Str. 12
 *            17033 Neubrandenburg
 * ----------------------------------------------------------------------------
 *     Dieses Dokument und die hierin enthaltenen Informationen unterliegen
 *     dem Urheberrecht und duerfen ohne die schriftliche Genehmigung des
 *     Herausgebers weder als ganzes noch in Teilen dupliziert, reproduziert
 *     oder manipuliert werden.
 * ----------------------------------------------------------------------------
 *     $Id$
 * ----------------------------------------------------------------------------
 */
package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

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

}
