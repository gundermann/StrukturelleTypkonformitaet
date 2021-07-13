package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import matching.MatchingInfo;


public class HigherPotentialTypesFirstComparatorTest {

  private List<Collection<MatchingInfo>> list;

  @Before
  public void setup() {
    list = new ArrayList<>();

    list.add( Arrays.asList( mockMatchingInfo( Object.class ) ) );
    list.add( Arrays.asList( mockMatchingInfo( Boolean.class ) ) );
    list.add( Arrays.asList( mockMatchingInfo( String.class ), mockMatchingInfo( Object.class ) ) );
    list.add( Arrays.asList( mockMatchingInfo( String.class ) ) );
    list.add( Arrays.asList( mockMatchingInfo( Integer.class ) ) );
  }

  @Test
  public void testCompareWithNoHigherPotentialTypes() {
    HigherPotentialTypesFirstComparator comparator = new HigherPotentialTypesFirstComparator(
        new ArrayList<Class<?>>() );
    Collections.sort( list, comparator );
    assertThat( list.get( 0 ).size(), equalTo( 1 ) );
    assertThat( list.get( 0 ).iterator().next().getTarget(), equalTo( Object.class ) );

    assertThat( list.get( 1 ).size(), equalTo( 1 ) );
    assertThat( list.get( 1 ).iterator().next().getTarget(), equalTo( Boolean.class ) );

    assertThat( list.get( 2 ).size(), equalTo( 2 ) );
    Iterator<MatchingInfo> iterator = list.get( 2 ).iterator();
    assertThat( iterator.next().getTarget(), equalTo( String.class ) );
    assertThat( iterator.next().getTarget(), equalTo( Object.class ) );

    assertThat( list.get( 3 ).size(), equalTo( 1 ) );
    assertThat( list.get( 3 ).iterator().next().getTarget(), equalTo( String.class ) );

    assertThat( list.get( 4 ).size(), equalTo( 1 ) );
    assertThat( list.get( 4 ).iterator().next().getTarget(), equalTo( Integer.class ) );
  }

  @Test
  public void testCompareWithOneHigherPotentialTypes() {
    HigherPotentialTypesFirstComparator comparator = new HigherPotentialTypesFirstComparator(
        Arrays.asList( String.class ) );

    Collections.sort( list, comparator );
    assertThat( list.get( 0 ).size(), equalTo( 2 ) );
    Iterator<MatchingInfo> iterator = list.get( 0 ).iterator();
    assertThat( iterator.next().getTarget(), equalTo( String.class ) );
    assertThat( iterator.next().getTarget(), equalTo( Object.class ) );

    assertThat( list.get( 1 ).size(), equalTo( 1 ) );
    assertThat( list.get( 1 ).iterator().next().getTarget(), equalTo( String.class ) );
  }

  @Test
  public void testCompareWithMultipleHigherPotentialTypes() {
    HigherPotentialTypesFirstComparator comparator = new HigherPotentialTypesFirstComparator(
        Arrays.asList( Integer.class, String.class ) );

    Collections.sort( list, comparator );
    assertThat( list.get( 0 ).size(), equalTo( 2 ) );
    Iterator<MatchingInfo> iterator = list.get( 0 ).iterator();
    assertThat( iterator.next().getTarget(), equalTo( String.class ) );
    assertThat( iterator.next().getTarget(), equalTo( Object.class ) );

    assertThat( list.get( 1 ).size(), equalTo( 1 ) );
    assertThat( list.get( 1 ).iterator().next().getTarget(), equalTo( String.class ) );

    assertThat( list.get( 2 ).size(), equalTo( 1 ) );
    assertThat( list.get( 2 ).iterator().next().getTarget(), equalTo( Integer.class ) );
  }

  private MatchingInfo mockMatchingInfo( Class<?> checkType ) {
    return new MatchingInfo.Builder( null, checkType, null ).build();
  }

}
