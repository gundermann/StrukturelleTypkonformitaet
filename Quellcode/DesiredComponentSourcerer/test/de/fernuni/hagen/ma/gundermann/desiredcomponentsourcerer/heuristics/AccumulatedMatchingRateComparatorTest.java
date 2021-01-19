package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import org.easymock.EasyMock;
import org.junit.Test;

import matching.MatcherRate;
import matching.modules.PartlyTypeMatchingInfo;

public class AccumulatedMatchingRateComparatorTest {

  private Comparator<Collection<PartlyTypeMatchingInfo>> comparator = new AccumulatedMatchingRateComparator();

  @Test
  public void test() {
    PartlyTypeMatchingInfo pi300 = createPartlyTypeMatchingInfo( 300 );
    PartlyTypeMatchingInfo pi200 = createPartlyTypeMatchingInfo( 200 );
    PartlyTypeMatchingInfo pi100 = createPartlyTypeMatchingInfo( 100 );
    Collection<PartlyTypeMatchingInfo> col500 = Arrays.asList( pi300, pi200 );
    Collection<PartlyTypeMatchingInfo> col400 = Arrays.asList( pi300, pi100 );
    Collection<PartlyTypeMatchingInfo> col300 = Arrays.asList( pi100, pi200 );
    Collection<PartlyTypeMatchingInfo> col200_2 = Arrays.asList( pi100, pi100 );
    Collection<PartlyTypeMatchingInfo> col200 = Arrays.asList( pi200 );
    Collection<PartlyTypeMatchingInfo> col100 = Arrays.asList( pi100 );

    assertThat( comparator.compare( col100, col200_2 ), equalTo( -1 ) );
    assertThat( comparator.compare( col100, col200 ), equalTo( -1 ) );
    assertThat( comparator.compare( col200, col200_2 ), equalTo( 0 ) );
    assertThat( comparator.compare( col200, col300 ), equalTo( -1 ) );
    assertThat( comparator.compare( col200_2, col300 ), equalTo( -1 ) );
    assertThat( comparator.compare( col300, col400 ), equalTo( -1 ) );
    assertThat( comparator.compare( col400, col500 ), equalTo( -1 ) );

  }

  private PartlyTypeMatchingInfo createPartlyTypeMatchingInfo( int rate ) {
    PartlyTypeMatchingInfo ptmi = EasyMock.createNiceMock( PartlyTypeMatchingInfo.class );
    EasyMock.expect( ptmi.getQualitativeMatchRating() ).andReturn( createMatcherRate( rate ) ).anyTimes();
    EasyMock.expect( ptmi.getQuantitaiveMatchRating() ).andReturn( Double.valueOf( rate ) ).anyTimes();
    EasyMock.replay( ptmi );
    return ptmi;
  }

  private MatcherRate createMatcherRate( int rate ) {
    MatcherRate mr = EasyMock.createNiceMock( MatcherRate.class );
    EasyMock.expect( mr.getMatcherRating() ).andReturn( Double.valueOf( rate ) ).anyTimes();
    EasyMock.replay( mr );
    return mr;
  }

}
