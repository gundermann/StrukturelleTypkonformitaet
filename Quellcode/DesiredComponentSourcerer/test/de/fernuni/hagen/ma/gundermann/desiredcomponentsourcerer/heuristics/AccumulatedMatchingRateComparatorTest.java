package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import org.easymock.EasyMock;
import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatcherRate;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatchingInfo;

public class AccumulatedMatchingRateComparatorTest {

	private Comparator<Collection<MatchingInfo>> comparator = new AccumulatedMatchingRateComparator();

	// TODO Der Comparator muss erst noch evaluiert werden (Varianten LMF)
	@Test
	public void test() {
		MatchingInfo pi300 = createPartlyTypeMatchingInfo(300);
		MatchingInfo pi200 = createPartlyTypeMatchingInfo(200);
		MatchingInfo pi100 = createPartlyTypeMatchingInfo(100);
		Collection<MatchingInfo> col500 = Arrays.asList(pi300, pi200);
		Collection<MatchingInfo> col400 = Arrays.asList(pi300, pi100);
		Collection<MatchingInfo> col300 = Arrays.asList(pi100, pi200);
		Collection<MatchingInfo> col200_2 = Arrays.asList(pi100, pi100);
		Collection<MatchingInfo> col200 = Arrays.asList(pi200);
		Collection<MatchingInfo> col100 = Arrays.asList(pi100);

		assertThat(comparator.compare(col100, col200_2), equalTo(-1));
		assertThat(comparator.compare(col100, col200), equalTo(-1));
		assertThat(comparator.compare(col200, col200_2), equalTo(0));
		assertThat(comparator.compare(col200, col300), equalTo(-1));
		assertThat(comparator.compare(col200_2, col300), equalTo(-1));
		assertThat(comparator.compare(col300, col400), equalTo(-1));
		assertThat(comparator.compare(col400, col500), equalTo(-1));

	}

	private MatchingInfo createPartlyTypeMatchingInfo(int rate) {
		MatchingInfo ptmi = EasyMock.createNiceMock(MatchingInfo.class);
		EasyMock.expect(ptmi.getQualitativeMatchRating()).andReturn(createMatcherRate(rate)).anyTimes();
		EasyMock.replay(ptmi);
		return ptmi;
	}

	private MatcherRate createMatcherRate(int rate) {
		MatcherRate mr = EasyMock.createNiceMock(MatcherRate.class);
		EasyMock.expect(mr.getMatcherRating()).andReturn(Double.valueOf(rate)).anyTimes();
		EasyMock.replay(mr);
		return mr;
	}

}
