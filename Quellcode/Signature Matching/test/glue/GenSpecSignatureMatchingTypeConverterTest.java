package glue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

import matching.methods.MethodMatchingInfo;
import matching.modules.ModuleMatchingInfo;
import testcomponents.genspec.DesiredGenInterface;
import testcomponents.genspec.DesiredSpecInterface;
import testcomponents.genspec.General;
import testcomponents.genspec.OfferedSpecClass;
import testcomponents.genspec.Specific;

public class GenSpecSignatureMatchingTypeConverterTest {

	@Test
	public void genSourceSpecTargetMatching() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Class<DesiredGenInterface> source = DesiredGenInterface.class;
		Class<OfferedSpecClass> target = OfferedSpecClass.class;
		OfferedSpecClass convertationObject = new OfferedSpecClass();
		SignatureMatchingTypeConverter<DesiredGenInterface> converter = new SignatureMatchingTypeConverter<>(source);

		Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();

		MethodMatchingInfo mmiConcat = EasyMock.createNiceMock(MethodMatchingInfo.class);
		EasyMock.expect(mmiConcat.getTarget())
				.andReturn(target.getDeclaredMethod("concat", Specific.class, Specific.class)).anyTimes();
		EasyMock.expect(mmiConcat.getSource()).andReturn(source.getMethod("concat", General.class, General.class))
				.anyTimes();
		EasyMock.expect(mmiConcat.getReturnTypeMatchingInfo()).andReturn(createMMI_Str2Str()).anyTimes();
		EasyMock.expect(mmiConcat.getArgumentTypeMatchingInfos())
				.andReturn(createMMIMap(createMMI_G2S(), createMMI_G2S())).anyTimes();

		MethodMatchingInfo mmiAddInt = EasyMock.createNiceMock(MethodMatchingInfo.class);
		EasyMock.expect(mmiAddInt.getTarget()).andReturn(target.getDeclaredMethod("add", Specific.class, Integer.class))
				.anyTimes();
		EasyMock.expect(mmiAddInt.getSource()).andReturn(source.getMethod("add", General.class, Integer.class))
				.anyTimes();
		EasyMock.expect(mmiAddInt.getReturnTypeMatchingInfo()).andReturn(createMMI_S2G()).anyTimes();
		EasyMock.expect(mmiAddInt.getArgumentTypeMatchingInfos())
				.andReturn(createMMIMap(createMMI_G2S(), createMMI_Int2Int())).anyTimes();

		MethodMatchingInfo mmiAddGen = EasyMock.createNiceMock(MethodMatchingInfo.class);
		EasyMock.expect(mmiAddGen.getTarget())
				.andReturn(target.getDeclaredMethod("add", Specific.class, Specific.class)).anyTimes();
		EasyMock.expect(mmiAddGen.getSource()).andReturn(source.getMethod("add", General.class, General.class))
				.anyTimes();
		EasyMock.expect(mmiAddGen.getReturnTypeMatchingInfo()).andReturn(createMMI_S2G()).anyTimes();
		EasyMock.expect(mmiAddGen.getArgumentTypeMatchingInfos())
				.andReturn(createMMIMap(createMMI_G2S(), createMMI_G2S())).anyTimes();

		MethodMatchingInfo mmiGetLong = EasyMock.createNiceMock(MethodMatchingInfo.class);
		EasyMock.expect(mmiGetLong.getTarget()).andReturn(target.getDeclaredMethod("getBoxedLongAttr", Specific.class))
				.anyTimes();
		EasyMock.expect(mmiGetLong.getSource()).andReturn(source.getMethod("getBoxedLongAttr", General.class)).anyTimes();
		EasyMock.expect(mmiGetLong.getReturnTypeMatchingInfo()).andReturn(createMMI_Long2Long()).anyTimes();
		EasyMock.expect(mmiGetLong.getArgumentTypeMatchingInfos()).andReturn(createMMIMap(createMMI_G2S())).anyTimes();

		EasyMock.replay(mmiConcat, mmiAddInt, mmiAddGen, mmiGetLong);

		methodMatchingInfos.add(mmiConcat);
		methodMatchingInfos.add(mmiAddInt);
		methodMatchingInfos.add(mmiAddGen);
		methodMatchingInfos.add(mmiGetLong);

		ModuleMatchingInfo<DesiredGenInterface> moduleMatchingInfo = EasyMock.createNiceMock(ModuleMatchingInfo.class);
		EasyMock.expect(moduleMatchingInfo.getMethodMatchingInfos()).andReturn(methodMatchingInfos).anyTimes();
		EasyMock.replay(moduleMatchingInfo);

		DesiredGenInterface converted = converter.convert(convertationObject, moduleMatchingInfo);
		General hello_1_10_Gen = new General("hello", 1, 10L);
		General world_5_44_Gen = new General("world", 5, 44L);
		Specific hello_2_Spec = new Specific("hello", 2);
		Specific world_99_Spec = new Specific("world", 99);

		assertThat(converted.concat(hello_1_10_Gen, world_5_44_Gen), equalTo("helloworld"));
		assertThat(converted.concat(hello_2_Spec, world_99_Spec), equalTo("helloworld"));

		General result_2 = converted.add(hello_1_10_Gen, 1);
		assertThat(result_2.getIntAttr(), equalTo(2));

		General result_109 = converted.add(world_99_Spec, 10);
		assertThat(result_109.getIntAttr(), equalTo(109));

		General result_6 = converted.add(hello_1_10_Gen, world_5_44_Gen);
		assertThat(result_6.getIntAttr(), equalTo(6));

		General result_101 = converted.add(world_99_Spec, hello_2_Spec);
		assertThat(result_101.getIntAttr(), equalTo(101));

		General result_3 = converted.add(hello_1_10_Gen, hello_2_Spec);
		assertThat(result_3.getIntAttr(), equalTo(3));
		
		assertThat(converted.getBoxedLongAttr(world_5_44_Gen), equalTo(44L));
		assertThat(converted.getBoxedLongAttr(hello_1_10_Gen), equalTo(10L));
		assertThat(converted.getBoxedLongAttr(world_99_Spec), equalTo(99L));
		assertThat(converted.getBoxedLongAttr(hello_2_Spec), equalTo(2L));

		checkInvokationOfAllNonParametrizedMethods(converted);
	}
	
	@Test
	public void specSourceSpecTargetMatching() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Class<DesiredSpecInterface> source = DesiredSpecInterface.class;
		Class<OfferedSpecClass> target = OfferedSpecClass.class;
		OfferedSpecClass convertationObject = new OfferedSpecClass();
		SignatureMatchingTypeConverter<DesiredSpecInterface> converter = new SignatureMatchingTypeConverter<>(source);

		Set<MethodMatchingInfo> methodMatchingInfos = new HashSet<>();

		MethodMatchingInfo mmiConcat = EasyMock.createNiceMock(MethodMatchingInfo.class);
		EasyMock.expect(mmiConcat.getTarget())
				.andReturn(target.getDeclaredMethod("concat", Specific.class, Specific.class)).anyTimes();
		EasyMock.expect(mmiConcat.getSource()).andReturn(source.getMethod("concat", Specific.class, Specific.class))
				.anyTimes();
		EasyMock.expect(mmiConcat.getReturnTypeMatchingInfo()).andReturn(createMMI_Str2Str()).anyTimes();
		EasyMock.expect(mmiConcat.getArgumentTypeMatchingInfos())
				.andReturn(createMMIMap(createMMI_S2S(), createMMI_S2S())).anyTimes();

		MethodMatchingInfo mmiAddInt = EasyMock.createNiceMock(MethodMatchingInfo.class);
		EasyMock.expect(mmiAddInt.getTarget()).andReturn(target.getDeclaredMethod("add", Specific.class, Integer.class))
				.anyTimes();
		EasyMock.expect(mmiAddInt.getSource()).andReturn(source.getMethod("add", Specific.class, Integer.class))
				.anyTimes();
		EasyMock.expect(mmiAddInt.getReturnTypeMatchingInfo()).andReturn(createMMI_S2S()).anyTimes();
		EasyMock.expect(mmiAddInt.getArgumentTypeMatchingInfos())
				.andReturn(createMMIMap(createMMI_S2S(), createMMI_Int2Int())).anyTimes();

		MethodMatchingInfo mmiAddGen = EasyMock.createNiceMock(MethodMatchingInfo.class);
		EasyMock.expect(mmiAddGen.getTarget())
				.andReturn(target.getDeclaredMethod("add", Specific.class, Specific.class)).anyTimes();
		EasyMock.expect(mmiAddGen.getSource()).andReturn(source.getMethod("add", Specific.class, Specific.class))
				.anyTimes();
		EasyMock.expect(mmiAddGen.getReturnTypeMatchingInfo()).andReturn(createMMI_S2S()).anyTimes();
		EasyMock.expect(mmiAddGen.getArgumentTypeMatchingInfos())
				.andReturn(createMMIMap(createMMI_S2S(), createMMI_S2S())).anyTimes();

		MethodMatchingInfo mmiGetLong = EasyMock.createNiceMock(MethodMatchingInfo.class);
		EasyMock.expect(mmiGetLong.getTarget()).andReturn(target.getDeclaredMethod("getBoxedLongAttr", Specific.class))
				.anyTimes();
		EasyMock.expect(mmiGetLong.getSource()).andReturn(source.getMethod("getBoxedLongAttr", Specific.class)).anyTimes();
		EasyMock.expect(mmiGetLong.getReturnTypeMatchingInfo()).andReturn(createMMI_Long2Long()).anyTimes();
		EasyMock.expect(mmiGetLong.getArgumentTypeMatchingInfos()).andReturn(createMMIMap()).anyTimes();

		EasyMock.replay(mmiConcat, mmiAddInt, mmiAddGen, mmiGetLong);

		methodMatchingInfos.add(mmiConcat);
		methodMatchingInfos.add(mmiAddInt);
		methodMatchingInfos.add(mmiAddGen);
		methodMatchingInfos.add(mmiGetLong);

		ModuleMatchingInfo<DesiredSpecInterface> moduleMatchingInfo = EasyMock.createNiceMock(ModuleMatchingInfo.class);
		EasyMock.expect(moduleMatchingInfo.getMethodMatchingInfos()).andReturn(methodMatchingInfos).anyTimes();
		EasyMock.replay(moduleMatchingInfo);

		DesiredSpecInterface converted = converter.convert(convertationObject, moduleMatchingInfo);
		Specific hello_true_Gen = new Specific("hello", true);
		Specific world_true_Gen = new Specific("world", true);
		Specific hello_2_false_Spec = new Specific("hello", 2);
		Specific world_99_false_Spec = new Specific("world", 99);

		assertThat(converted.concat(hello_true_Gen, world_true_Gen), equalTo("helloworld"));
		assertThat(converted.concat(hello_2_false_Spec, world_99_false_Spec), equalTo("helloworld"));

		General result_109 = converted.add(world_99_false_Spec, 10);
		assertThat(result_109.getIntAttr(), equalTo(109));

		General result_101 = converted.add(world_99_false_Spec, hello_2_false_Spec);
		assertThat(result_101.getIntAttr(), equalTo(101));

//		assertThat(converted.getBoxedLongAttr(world_5_44_Gen), equalTo(44L));
//		assertThat(converted.getBoxedLongAttr(hello_true_Gen), equalTo(10L));
//		assertThat(converted.getBoxedLongAttr(world_99_Spec), equalTo(99L));
//		assertThat(converted.getBoxedLongAttr(hello_2_Spec), equalTo(2L));

		checkInvokationOfAllNonParametrizedMethods(converted);
	}

	private Map<Integer, ModuleMatchingInfo<?>> createMMIMap(ModuleMatchingInfo... infos) {
		Map<Integer, ModuleMatchingInfo<?>> map = new HashMap<>();
		for (int i = 0; i < infos.length; i++) {
			map.put(i, infos[i]);
		}
		return map;
	}

	private ModuleMatchingInfo createMMI_Int2Int() {
		ModuleMatchingInfo mmi = EasyMock.createNiceMock(ModuleMatchingInfo.class);
		EasyMock.expect(mmi.getSource()).andReturn(Integer.class).anyTimes();
		EasyMock.expect(mmi.getTarget()).andReturn(Integer.class).anyTimes();
		EasyMock.expect(mmi.getMethodMatchingInfos()).andReturn(new HashSet<>()).anyTimes();
		EasyMock.replay(mmi);
		return mmi;
	}

	private ModuleMatchingInfo createMMI_Str2Str() {
		ModuleMatchingInfo mmi = EasyMock.createNiceMock(ModuleMatchingInfo.class);
		EasyMock.expect(mmi.getSource()).andReturn(String.class).anyTimes();
		EasyMock.expect(mmi.getTarget()).andReturn(String.class).anyTimes();
		EasyMock.expect(mmi.getMethodMatchingInfos()).andReturn(new HashSet<>()).anyTimes();
		EasyMock.replay(mmi);
		return mmi;
	}
	
	private ModuleMatchingInfo createMMI_S2S() {
		ModuleMatchingInfo mmi = EasyMock.createNiceMock(ModuleMatchingInfo.class);
		EasyMock.expect(mmi.getSource()).andReturn(Specific.class).anyTimes();
		EasyMock.expect(mmi.getTarget()).andReturn(Specific.class).anyTimes();
		EasyMock.expect(mmi.getMethodMatchingInfos()).andReturn(new HashSet<>()).anyTimes();
		EasyMock.replay(mmi);
		return mmi;
	}
	
	private ModuleMatchingInfo createMMI_Long2Long() {
		ModuleMatchingInfo mmi = EasyMock.createNiceMock(ModuleMatchingInfo.class);
		EasyMock.expect(mmi.getSource()).andReturn(Long.class).anyTimes();
		EasyMock.expect(mmi.getTarget()).andReturn(Long.class).anyTimes();
		EasyMock.expect(mmi.getMethodMatchingInfos()).andReturn(new HashSet<>()).anyTimes();
		EasyMock.replay(mmi);
		return mmi;
	}

	private ModuleMatchingInfo createMMI_G2S() throws NoSuchMethodException, SecurityException {
		ModuleMatchingInfo mmit = EasyMock.createNiceMock(ModuleMatchingInfo.class);
		EasyMock.expect(mmit.getSource()).andReturn(Specific.class).anyTimes();
		EasyMock.expect(mmit.getTarget()).andReturn(General.class).anyTimes();
		Set methodInfos = new HashSet<>();
		MethodMatchingInfo concatMethod = EasyMock.createNiceMock(MethodMatchingInfo.class);
		EasyMock.expect(concatMethod.getSource()).andReturn(Specific.class.getMethod("getStringAttr")).anyTimes();
		EasyMock.expect(concatMethod.getTarget()).andReturn(General.class.getMethod("getStringAttr")).anyTimes();
		EasyMock.expect(concatMethod.getReturnTypeMatchingInfo()).andReturn(createMMI_Str2Str()).anyTimes();
		EasyMock.expect(concatMethod.getArgumentTypeMatchingInfos()).andReturn(new HashMap<>()).anyTimes();
		EasyMock.replay(concatMethod);
		methodInfos.add(concatMethod);

		MethodMatchingInfo addMethod = EasyMock.createNiceMock(MethodMatchingInfo.class);
		EasyMock.expect(addMethod.getSource()).andReturn(Specific.class.getMethod("getIntAttr")).anyTimes();
		EasyMock.expect(addMethod.getTarget()).andReturn(General.class.getMethod("getIntAttr")).anyTimes();
		EasyMock.expect(addMethod.getReturnTypeMatchingInfo()).andReturn(createMMI_Int2Int()).anyTimes();
		EasyMock.expect(addMethod.getArgumentTypeMatchingInfos()).andReturn(new HashMap<>()).anyTimes();
		EasyMock.replay(addMethod);
		methodInfos.add(addMethod);
		
		MethodMatchingInfo getLongMethod = EasyMock.createNiceMock(MethodMatchingInfo.class);
		EasyMock.expect(getLongMethod.getSource()).andReturn(Specific.class.getMethod("getBoxedLongAttr")).anyTimes();
		EasyMock.expect(getLongMethod.getTarget()).andReturn(General.class.getMethod("getBoxedLongAttr")).anyTimes();
		EasyMock.expect(getLongMethod.getReturnTypeMatchingInfo()).andReturn(createMMI_Long2Long()).anyTimes();
		EasyMock.expect(getLongMethod.getArgumentTypeMatchingInfos()).andReturn(new HashMap<>()).anyTimes();
		EasyMock.replay(getLongMethod);
		methodInfos.add(getLongMethod);

		EasyMock.expect(mmit.getMethodMatchingInfos()).andReturn(methodInfos).anyTimes();
		EasyMock.replay(mmit);
		return mmit;
	}

	private ModuleMatchingInfo createMMI_S2G() throws NoSuchMethodException, SecurityException {
		ModuleMatchingInfo mmit = EasyMock.createNiceMock(ModuleMatchingInfo.class);
		EasyMock.expect(mmit.getSource()).andReturn(General.class).anyTimes();
		EasyMock.expect(mmit.getTarget()).andReturn(Specific.class).anyTimes();
		Set methodInfos = new HashSet<>();
		
		MethodMatchingInfo concatMethod = EasyMock.createNiceMock(MethodMatchingInfo.class);
		EasyMock.expect(concatMethod.getSource()).andReturn(General.class.getMethod("getStringAttr")).anyTimes();
		EasyMock.expect(concatMethod.getTarget()).andReturn(Specific.class.getMethod("getStringAttr")).anyTimes();
		EasyMock.expect(concatMethod.getReturnTypeMatchingInfo()).andReturn(createMMI_Str2Str()).anyTimes();
		EasyMock.expect(concatMethod.getArgumentTypeMatchingInfos()).andReturn(new HashMap<>()).anyTimes();
		EasyMock.replay(concatMethod);
		methodInfos.add(concatMethod);

		MethodMatchingInfo addMethod = EasyMock.createNiceMock(MethodMatchingInfo.class);
		EasyMock.expect(addMethod.getSource()).andReturn(General.class.getMethod("getIntAttr")).anyTimes();
		EasyMock.expect(addMethod.getTarget()).andReturn(Specific.class.getMethod("getIntAttr")).anyTimes();
		EasyMock.expect(addMethod.getReturnTypeMatchingInfo()).andReturn(createMMI_Int2Int()).anyTimes();
		EasyMock.expect(addMethod.getArgumentTypeMatchingInfos()).andReturn(new HashMap<>()).anyTimes();
		EasyMock.replay(addMethod);
		methodInfos.add(addMethod);
		
		MethodMatchingInfo getLongMethod = EasyMock.createNiceMock(MethodMatchingInfo.class);
		EasyMock.expect(getLongMethod.getSource()).andReturn(General.class.getMethod("getBoxedLongAttr")).anyTimes();
		EasyMock.expect(getLongMethod.getTarget()).andReturn(Specific.class.getMethod("getBoxedLongAttr")).anyTimes();
		EasyMock.expect(getLongMethod.getReturnTypeMatchingInfo()).andReturn(createMMI_Long2Long()).anyTimes();
		EasyMock.expect(getLongMethod.getArgumentTypeMatchingInfos()).andReturn(new HashMap<>()).anyTimes();
		EasyMock.replay(getLongMethod);
		methodInfos.add(getLongMethod);
		
		EasyMock.expect(mmit.getMethodMatchingInfos()).andReturn(methodInfos).anyTimes();
		EasyMock.replay(mmit);
		return mmit;
	}


	private void checkInvokationOfAllNonParametrizedMethods(Object converted)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for (Method m : converted.getClass().getMethods()) {
			if (m.getParameterCount() == 0) {
				System.out.println("try to invoke: " + m.getName());
				// TODO mit wait() gibt es Probleme
				// m.invoke( converted );
			}
		}

	}

}
