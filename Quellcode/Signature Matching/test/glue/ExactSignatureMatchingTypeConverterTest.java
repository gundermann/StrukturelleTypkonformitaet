package glue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

import matching.MatchingInfo;
import matching.methods.MethodMatchingInfo;
import matching.methods.MethodMatchingInfo.ParamPosition;
import matching.types.TypeMatchingInfo;
import matching.types.testtypes.Enum2;
import matching.types.testtypes.InterfaceWrapper;

public class ExactSignatureMatchingTypeConverterTest {

	@Test
	public void exactFullMatching() throws NoSuchMethodException, SecurityException {
		Class<InterfaceWrapper> libType = InterfaceWrapper.class;
		Class<Enum2> queryType = Enum2.class;
		Enum2 convertationObject = Enum2.CONSTANT_1;
		TypeConverter<InterfaceWrapper> converter = new TypeConverter<>(libType);

		Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();
		MethodMatchingInfo methodMatchingInfoGetFalse = EasyMock.createNiceMock(MethodMatchingInfo.class);
		EasyMock.expect(methodMatchingInfoGetFalse.getTarget()).andReturn(queryType.getMethod("getFalse")).anyTimes();
		EasyMock.expect(methodMatchingInfoGetFalse.getSource()).andReturn(libType.getMethod("getFalse")).anyTimes();
		EasyMock.expect(methodMatchingInfoGetFalse.getArgumentTypeMatchingInfos())
				.andReturn(new HashMap<ParamPosition, MatchingInfo>()).anyTimes();
		methodMatchingInfos.add(methodMatchingInfoGetFalse);

		MethodMatchingInfo methodMatchingInfoGetTrue = EasyMock.createNiceMock(MethodMatchingInfo.class);
		EasyMock.expect(methodMatchingInfoGetTrue.getTarget()).andReturn(queryType.getMethod("getTrue")).anyTimes();
		EasyMock.expect(methodMatchingInfoGetTrue.getSource()).andReturn(libType.getMethod("getTrue")).anyTimes();
		EasyMock.expect(methodMatchingInfoGetTrue.getArgumentTypeMatchingInfos())
				.andReturn(new HashMap<ParamPosition, MatchingInfo>()).anyTimes();
		methodMatchingInfos.add(methodMatchingInfoGetTrue);

		MethodMatchingInfo methodMatchingInfoGetOne = EasyMock.createNiceMock(MethodMatchingInfo.class);
		EasyMock.expect(methodMatchingInfoGetOne.getTarget()).andReturn(queryType.getMethod("getOne")).anyTimes();
		EasyMock.expect(methodMatchingInfoGetOne.getSource()).andReturn(libType.getMethod("getOne")).anyTimes();
		EasyMock.expect(methodMatchingInfoGetOne.getArgumentTypeMatchingInfos())
				.andReturn(new HashMap<ParamPosition, MatchingInfo>()).anyTimes();
		methodMatchingInfos.add(methodMatchingInfoGetOne);

		MethodMatchingInfo methodMatchingInfoGetNull = EasyMock.createNiceMock(MethodMatchingInfo.class);
		EasyMock.expect(methodMatchingInfoGetNull.getTarget()).andReturn(queryType.getMethod("getNull")).anyTimes();
		EasyMock.expect(methodMatchingInfoGetNull.getSource()).andReturn(libType.getMethod("getNull")).anyTimes();
		EasyMock.expect(methodMatchingInfoGetNull.getArgumentTypeMatchingInfos())
				.andReturn(new HashMap<ParamPosition, MatchingInfo>()).anyTimes();
		methodMatchingInfos.add(methodMatchingInfoGetNull);

		TypeMatchingInfo moduleMatchingInfo = EasyMock.createNiceMock(TypeMatchingInfo.class);
		EasyMock.expect(moduleMatchingInfo.getMethodMatchingInfos()).andReturn(methodMatchingInfos).anyTimes();
		EasyMock.replay(moduleMatchingInfo, methodMatchingInfoGetFalse, methodMatchingInfoGetTrue,
				methodMatchingInfoGetOne, methodMatchingInfoGetNull);

		ConvertableComponent convertableComponent = new ConvertableComponent(convertationObject,
				moduleMatchingInfo.getMethodMatchingInfos());
		InterfaceWrapper converted = converter
				.convert(ConvertableBundle.createBundle(Collections.singletonList(convertableComponent)));
		assertThat(converted.getFalse(), equalTo(convertationObject.getFalse()));
		assertThat(converted.getTrue(), equalTo(convertationObject.getTrue()));
		assertThat(converted.getOne(), equalTo(convertationObject.getOne()));
		assertThat(converted.getNull(), equalTo(convertationObject.getNull()));
		assertThat(converted.getNull(), nullValue());
	}

}
