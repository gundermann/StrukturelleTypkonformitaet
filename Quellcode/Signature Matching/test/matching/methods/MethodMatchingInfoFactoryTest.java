package matching.methods;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.signaturematching.SingleMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo.ParamPosition;
import matching.methods.testmethods.MethodPool;

public class MethodMatchingInfoFactoryTest {


	@Test
	public void create() {
		Method source = MethodPool.getMethod("getOne");
		Method target = MethodPool.getMethod("getOneNativeWrapped");
		MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory(target, source);
		SingleMatchingInfo returnTypeMatchingInfo = mockMatchingInfo();
		MethodMatchingInfo info = factory.create(returnTypeMatchingInfo, new HashMap<>());
		assertThat(info, notNullValue());
		assertThat(info.getReturnTypeMatchingInfo(), equalTo(returnTypeMatchingInfo));
		assertThat(info.getSource(), equalTo(source));
		assertThat(info.getTarget(), equalTo(target));
		assertThat(info.getArgumentTypeMatchingInfos(), notNullValue());
		assertThat(info.getArgumentTypeMatchingInfos().keySet().isEmpty(), equalTo(true));
	}

	@Test
	public void createWithParamPosition_Default() {
		Method source = MethodPool.getMethod("addGen");
		Method target = MethodPool.getMethod("addSpec");
		MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory(target, source);
		SingleMatchingInfo returnTypeMatchingInfo = mockMatchingInfo();

		Map<ParamPosition, SingleMatchingInfo> argMatchingInfo = new HashMap<>();
		argMatchingInfo.put(new ParamPosition(1, 1), null);
		argMatchingInfo.put(new ParamPosition(2, 2), null);
		MethodMatchingInfo info = factory.create(returnTypeMatchingInfo, argMatchingInfo);
		assertThat(info, notNullValue());
		assertThat(info.getReturnTypeMatchingInfo(), equalTo(returnTypeMatchingInfo));
		assertThat(info.getSource(), equalTo(source));
		assertThat(info.getTarget(), equalTo(target));
		assertThat(info.getArgumentTypeMatchingInfos(), notNullValue());
		assertThat(info.getArgumentTypeMatchingInfos().size(), equalTo(2));
		for (ParamPosition pp : info.getArgumentTypeMatchingInfos().keySet()) {
			assertThat(pp.getSourceParamPosition(), equalTo(pp.getTargetParamPosition()));
		}

	}

	@Test
	public void createWithParamPosition() {
		Method source = MethodPool.getMethod("addGen");
		Method target = MethodPool.getMethod("addSpec");
		MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory(target, source);
		SingleMatchingInfo returnTypeMatchingInfo = mockMatchingInfo();

		ParamPosition pp2_1 = new ParamPosition(2, 1);
		ParamPosition pp1_2 = new ParamPosition(1, 2);

		Map<ParamPosition, SingleMatchingInfo> argMatchingInfo = new HashMap<>();
		argMatchingInfo.put(pp2_1, null);
		argMatchingInfo.put(pp1_2, null);
		MethodMatchingInfo info = factory.create(returnTypeMatchingInfo, argMatchingInfo);
		assertThat(info, notNullValue());
		assertThat(info.getReturnTypeMatchingInfo(), equalTo(returnTypeMatchingInfo));
		assertThat(info.getSource(), equalTo(source));
		assertThat(info.getTarget(), equalTo(target));
		assertThat(info.getArgumentTypeMatchingInfos(), notNullValue());
		assertThat(info.getArgumentTypeMatchingInfos().size(), equalTo(2));
		for (ParamPosition pp : info.getArgumentTypeMatchingInfos().keySet()) {
			if (pp.getSourceParamPosition() == 2) {
				assertThat(pp, equalTo(pp2_1));
			} else {
				assertThat(pp, equalTo(pp1_2));
			}
		}
	}

	@Test
	public void createFromTypeMatchingInfos() {
		Method source = MethodPool.getMethod("getOne");
		Method target = MethodPool.getMethod("getOneNativeWrapped");
		MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory(target, source);
		Collection<SingleMatchingInfo> returnTypeMatchingInfos = new ArrayList<>(2);
		returnTypeMatchingInfos.add(mockMatchingInfo());
		returnTypeMatchingInfos.add(mockMatchingInfo());
		Collection<Map<ParamPosition, Collection<SingleMatchingInfo>>> argumentTypesMatchingInfos = new ArrayList<>(0);
		Collection<MethodMatchingInfo> infos = factory.createFromTypeMatchingInfos(returnTypeMatchingInfos,
				argumentTypesMatchingInfos);

		assertThat(infos, notNullValue());
		assertThat(infos.size(), equalTo(2));
		Iterator<MethodMatchingInfo> iterator = infos.iterator();
		while (iterator.hasNext()) {
			MethodMatchingInfo info = iterator.next();
			assertThat(info.getSource(), equalTo(source));
			assertThat(info.getTarget(), equalTo(target));
		}
	}

	@Test
	public void createFromTypeMatchingInfos_MultipleArgTypesWithParamPositions() {
		Method source = MethodPool.getMethod("getOne");
		Method target = MethodPool.getMethod("getOneNativeWrapped");
		MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory(target, source);
		Collection<SingleMatchingInfo> returnTypeMatchingInfos = new ArrayList<>(2);
		returnTypeMatchingInfos.add(mockMatchingInfo());
		returnTypeMatchingInfos.add(mockMatchingInfo());

		Collection<Map<ParamPosition, Collection<SingleMatchingInfo>>> argumentTypesMatchingInfos = new ArrayList<>(2);
		Map<ParamPosition, Collection<SingleMatchingInfo>> argumentTypesMatchingInfos1 = new HashMap<>(2);
		argumentTypesMatchingInfos1.put(new ParamPosition(1, 1), Collections.singletonList(mockMatchingInfo()));
		argumentTypesMatchingInfos1.put(new ParamPosition(2, 2), Collections.singletonList(mockMatchingInfo()));

		Map<ParamPosition, Collection<SingleMatchingInfo>> argumentTypesMatchingInfos2 = new HashMap<>(2);
		argumentTypesMatchingInfos2.put(new ParamPosition(2, 1), Collections.singletonList(mockMatchingInfo()));
		argumentTypesMatchingInfos2.put(new ParamPosition(1, 2), Collections.singletonList(mockMatchingInfo()));

		argumentTypesMatchingInfos.add(argumentTypesMatchingInfos1);
		argumentTypesMatchingInfos.add(argumentTypesMatchingInfos2);
		Collection<MethodMatchingInfo> infos = factory.createFromTypeMatchingInfos(returnTypeMatchingInfos,
				argumentTypesMatchingInfos);

		assertThat(infos, notNullValue());
		assertThat(infos.size(), equalTo(4));
		Iterator<MethodMatchingInfo> iterator = infos.iterator();
		while (iterator.hasNext()) {
			MethodMatchingInfo info = iterator.next();
			assertThat(info.getSource(), equalTo(source));
			assertThat(info.getTarget(), equalTo(target));
		}

	}

	private SingleMatchingInfo mockMatchingInfo() {
		SingleMatchingInfo mi = EasyMock.createNiceMock(SingleMatchingInfo.class);
		EasyMock.replay(mi);
		return mi;
	}

	@Test
	public void createFromTypeMatchingInfos_keineErzeugtWeilEsKeinenReturnTypeGibt() {
		Method source = MethodPool.getMethod("getOne");
		Method target = MethodPool.getMethod("getOneNativeWrapped");
		MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory(target, source);
		Collection<SingleMatchingInfo> returnTypeMatchingInfos = new ArrayList<>(0);
		Map<ParamPosition, Collection<SingleMatchingInfo>> argumentTypesMatchingInfos = new HashMap<>(2);
		argumentTypesMatchingInfos.put(new ParamPosition(1, 1), new ArrayList<>());
		argumentTypesMatchingInfos.put(new ParamPosition(1, 2), new ArrayList<>());
		Collection<MethodMatchingInfo> infos = factory.createFromTypeMatchingInfos(returnTypeMatchingInfos,
				Collections.singletonList(argumentTypesMatchingInfos));

		assertThat(infos, notNullValue());
		assertThat(infos.size(), equalTo(0));
	}

}
