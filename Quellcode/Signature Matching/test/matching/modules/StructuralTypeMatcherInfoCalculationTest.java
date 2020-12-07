package matching.modules;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.function.Supplier;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import matching.MatcherCombiner;
import matching.methods.MethodMatchingInfo;
import matching.modules.testmodules.Class1;
import matching.modules.testmodules.Class2;
import matching.modules.testmodules.Enum2;
import matching.modules.testmodules.EnumNative;
import matching.modules.testmodules.Interface1;
import matching.modules.testmodules.InterfaceWrapper;
import util.Logger;

public class StructuralTypeMatcherInfoCalculationTest {

	ExactTypeMatcher exactTypeMatcher = new ExactTypeMatcher();

	GenSpecTypeMatcher genSpecTypeMatcher = new GenSpecTypeMatcher();

	PartlyTypeMatcher matcher = new StructuralTypeMatcher(MatcherCombiner.combine(genSpecTypeMatcher, exactTypeMatcher,
			new WrappedTypeMatcher(MatcherCombiner.combine(genSpecTypeMatcher, exactTypeMatcher))));

	@BeforeClass
	public static void setupBefore() {
		Logger.switchOn();
	}

	@AfterClass
	public static void tearDownAfter() {
		Logger.switchOff();
	}

	@Test
	public void interface2interface_full_calculation() {
		Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos(Interface1.class,
				InterfaceWrapper.class);
		assertTrue(!matchingInfos.isEmpty());
		assertTrue(matchingInfos.size() == 12);

		PartlyTypeMatchingInfo partlyTypeMatchingInfos = matcher.calculatePartlyTypeMatchingInfos(Interface1.class,
				InterfaceWrapper.class);
		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getCheckType(), equalTo(Interface1.class));
		assertThat(partlyTypeMatchingInfos.getOriginalMethods().size(), equalTo(4));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(4));
		assertThat(partlyTypeMatchingInfos.getQuantitaiveMatchRating(), equalTo(1.0));
		for (Entry<Method, Supplier<Collection<MethodMatchingInfo>>> entry : partlyTypeMatchingInfos
				.getMethodMatchingInfoSupplier().entrySet()) {
			switch (entry.getKey().getName()) {
			case "getTrue":
			case "getFalse":
				assertThat(entry.getValue().get().size(), equalTo(2));
				break;
			case "getNull":
				assertThat(entry.getValue().get().size(), equalTo(3));
				break;
			case "getOne":
				assertThat(entry.getValue().get().size(), equalTo(1));
				break;
			default:
				fail("no MatchingMethodInfo for " + entry.getKey().getName());
				break;
			}
		}
	}

	@Test
	public void enum2interface_full_match() {
		Collection<ModuleMatchingInfo> matchingInfos = matcher.calculateTypeMatchingInfos(EnumNative.class,
				InterfaceWrapper.class);
		assertTrue(!matchingInfos.isEmpty());
		assertThat(matchingInfos.size(), equalTo(156));
		
		PartlyTypeMatchingInfo partlyTypeMatchingInfos = matcher.calculatePartlyTypeMatchingInfos(EnumNative.class,
				InterfaceWrapper.class);
		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getCheckType(), equalTo(EnumNative.class));
		assertThat(partlyTypeMatchingInfos.getOriginalMethods().size(), equalTo(4));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(4));
		assertThat(partlyTypeMatchingInfos.getQuantitaiveMatchRating(), equalTo(1.0));
		for (Entry<Method, Supplier<Collection<MethodMatchingInfo>>> entry : partlyTypeMatchingInfos
				.getMethodMatchingInfoSupplier().entrySet()) {
			switch (entry.getKey().getName()) {
			case "getTrue":
			case "getFalse":
				assertThat(entry.getValue().get().size(), equalTo(2));
				break;
			case "getNull":
				assertThat(entry.getValue().get().size(), equalTo(13));
				break;
			case "getOne":
				assertThat(entry.getValue().get().size(), equalTo(3));
				break;
			default:
				fail("no MatchingMethodInfo for " + entry.getKey().getName());
				break;
			}
		}
	}

	@Test
	public void interface2interface_partly_match() {
		PartlyTypeMatchingInfo partlyTypeMatchingInfos = matcher.calculatePartlyTypeMatchingInfos(InterfaceWrapper.class,
				Interface1.class);
		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getCheckType(), equalTo(InterfaceWrapper.class));
		assertThat(partlyTypeMatchingInfos.getOriginalMethods().size(), equalTo(7));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(3));
		assertThat(partlyTypeMatchingInfos.getQuantitaiveMatchRating(), equalTo(3d/7d));
		for (Entry<Method, Supplier<Collection<MethodMatchingInfo>>> entry : partlyTypeMatchingInfos
				.getMethodMatchingInfoSupplier().entrySet()) {
			switch (entry.getKey().getName()) {
			case "getTrue":
			case "getFalse":
				assertThat(entry.getValue().get().size(), equalTo(3));
				break;
			case "getOne":
				assertThat(entry.getValue().get().size(), equalTo(2));
				break;
			default:
				fail("no MatchingMethodInfo for " + entry.getKey().getName());
				break;
			}
		}
		
		
		partlyTypeMatchingInfos = matcher.calculatePartlyTypeMatchingInfos(Interface1.class,
				InterfaceWrapper.class);
		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getCheckType(), equalTo(Interface1.class));
		assertThat(partlyTypeMatchingInfos.getOriginalMethods().size(), equalTo(4));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(4));
		assertThat(partlyTypeMatchingInfos.getQuantitaiveMatchRating(), equalTo(1.0));
		for (Entry<Method, Supplier<Collection<MethodMatchingInfo>>> entry : partlyTypeMatchingInfos
				.getMethodMatchingInfoSupplier().entrySet()) {
			switch (entry.getKey().getName()) {
			case "getTrue":
			case "getFalse":
				assertThat(entry.getValue().get().size(), equalTo(2));
				break;
			case "getNull":
				assertThat(entry.getValue().get().size(), equalTo(3));
				break;
			case "getOne":
				assertThat(entry.getValue().get().size(), equalTo(1));
				break;
			default:
				fail("no MatchingMethodInfo for " + entry.getKey().getName());
				break;
			}
		}
		
	}

	@Test
	public void enum2interface_partly_match() {
		PartlyTypeMatchingInfo partlyTypeMatchingInfos = matcher.calculatePartlyTypeMatchingInfos(EnumNative.class,
				Interface1.class);
		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getCheckType(), equalTo(EnumNative.class));
		assertThat(partlyTypeMatchingInfos.getOriginalMethods().size(), equalTo(7));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(3));
		assertThat(partlyTypeMatchingInfos.getQuantitaiveMatchRating(), equalTo(3d/7d));
		for (Entry<Method, Supplier<Collection<MethodMatchingInfo>>> entry : partlyTypeMatchingInfos
				.getMethodMatchingInfoSupplier().entrySet()) {
			switch (entry.getKey().getName()) {
			case "getTrue":
			case "getFalse":
				assertThat(entry.getValue().get().size(), equalTo(2));
				break;
			case "getOne":
				assertThat(entry.getValue().get().size(), equalTo(7));
				break;
			default:
				fail("no MatchingMethodInfo for " + entry.getKey().getName());
				break;
			}
		}
		
		
		partlyTypeMatchingInfos = matcher.calculatePartlyTypeMatchingInfos(Enum2.class,
				Interface1.class);
		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getCheckType(), equalTo(Enum2.class));
		assertThat(partlyTypeMatchingInfos.getOriginalMethods().size(), equalTo(7));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(7));
		assertThat(partlyTypeMatchingInfos.getQuantitaiveMatchRating(), equalTo(7d/7d));
		for (Entry<Method, Supplier<Collection<MethodMatchingInfo>>> entry : partlyTypeMatchingInfos
				.getMethodMatchingInfoSupplier().entrySet()) {
			switch (entry.getKey().getName()) {
			case "getTrue":
			case "getFalse":
				assertThat(entry.getValue().get().size(), equalTo(3));
				break;
			case "getOne":
				assertThat(entry.getValue().get().size(), equalTo(8));
				break;
			case "subPartlyNativeWrapped":
			case "addPartlyNativeWrapped":
				assertThat(entry.getValue().get().size(), equalTo(8));
				break;
			case "subPartlyWrapped":
			case "addPartlyWrapped":
				assertThat(entry.getValue().get().size(), equalTo(32));
				break;
			default:
				fail("no MatchingMethodInfo for " + entry.getKey().getName());
				break;
			}
		}
		
		
		partlyTypeMatchingInfos = matcher.calculatePartlyTypeMatchingInfos(EnumNative.class,
				InterfaceWrapper.class);
		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getCheckType(), equalTo(EnumNative.class));
		assertThat(partlyTypeMatchingInfos.getOriginalMethods().size(), equalTo(4));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(4));
		assertThat(partlyTypeMatchingInfos.getQuantitaiveMatchRating(), equalTo(4d/4d));
		for (Entry<Method, Supplier<Collection<MethodMatchingInfo>>> entry : partlyTypeMatchingInfos
				.getMethodMatchingInfoSupplier().entrySet()) {
			switch (entry.getKey().getName()) {
			case "getTrue":
			case "getFalse":
				assertThat(entry.getValue().get().size(), equalTo(2));
				break;
			case "getOne":
				assertThat(entry.getValue().get().size(), equalTo(3));
				break;
			case "getNull":
				assertThat(entry.getValue().get().size(), equalTo(13));
				break;
			default:
				fail("no MatchingMethodInfo for " + entry.getKey().getName());
				break;
			}
		}
		
		partlyTypeMatchingInfos = matcher.calculatePartlyTypeMatchingInfos(Enum2.class,
				InterfaceWrapper.class);
		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getCheckType(), equalTo(Enum2.class));
		assertThat(partlyTypeMatchingInfos.getOriginalMethods().size(), equalTo(4));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(4));
		assertThat(partlyTypeMatchingInfos.getQuantitaiveMatchRating(), equalTo(4d/4d));
		for (Entry<Method, Supplier<Collection<MethodMatchingInfo>>> entry : partlyTypeMatchingInfos
				.getMethodMatchingInfoSupplier().entrySet()) {
			switch (entry.getKey().getName()) {
			case "getTrue":
			case "getFalse":
				assertThat(entry.getValue().get().size(), equalTo(3));
				break;
			case "getOne":
				assertThat(entry.getValue().get().size(), equalTo(4));
				break;
			case "getNull":
				assertThat(entry.getValue().get().size(), equalTo(14));
				break;
			default:
				fail("no MatchingMethodInfo for " + entry.getKey().getName());
				break;
			}
		}
	}

	@Test
	public void class2interface_partly_match() {
		assertTrue(matcher.matchesTypePartly(Class1.class, Interface1.class));
		assertTrue(matcher.matchesTypePartly(Class2.class, Interface1.class));
		assertTrue(matcher.matchesTypePartly(Class1.class, InterfaceWrapper.class));
		assertTrue(matcher.matchesTypePartly(Class2.class, InterfaceWrapper.class));
	}

}
