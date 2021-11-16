package matching.types;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.function.Supplier;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatcherCombiner;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.ExactTypeMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.GenSpecTypeMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.StructuralTypeMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.WrappedTypeMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.util.Logger;
import matching.types.testtypes.Class1;
import matching.types.testtypes.Class2;
import matching.types.testtypes.Enum2;
import matching.types.testtypes.EnumNative;
import matching.types.testtypes.Interface1;
import matching.types.testtypes.InterfaceWrapper;

public class StructuralTypeMatcherInfoCalculationTest {

	ExactTypeMatcher exactTypeMatcher = new ExactTypeMatcher();

	GenSpecTypeMatcher genSpecTypeMatcher = new GenSpecTypeMatcher();

	StructuralTypeMatcher matcher = new StructuralTypeMatcher(MatcherCombiner.combine(genSpecTypeMatcher, exactTypeMatcher,
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
		MatchingInfo partlyTypeMatchingInfos = matcher.calculateTypeMatchingInfos(Interface1.class, InterfaceWrapper.class);
		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getTarget(), equalTo(Interface1.class));
		assertThat(partlyTypeMatchingInfos.getMatchedSourceMethods().size(), equalTo(4));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(4));
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
		MatchingInfo partlyTypeMatchingInfos = matcher.calculateTypeMatchingInfos(EnumNative.class,
				InterfaceWrapper.class);

		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getTarget(), equalTo(EnumNative.class));
		assertThat(partlyTypeMatchingInfos.getMatchedSourceMethods().size(), equalTo(4));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(4));
		for (Entry<Method, Supplier<Collection<MethodMatchingInfo>>> entry : partlyTypeMatchingInfos
				.getMethodMatchingInfoSupplier().entrySet()) {
			switch (entry.getKey().getName()) {
			case "getTrue":
			case "getFalse":
				assertThat(entry.getValue().get().size(), equalTo(2));
				break;
			case "getNull":
				assertThat(entry.getValue().get().size(), equalTo(12));
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
		MatchingInfo partlyTypeMatchingInfos = matcher.calculateTypeMatchingInfos(InterfaceWrapper.class, Interface1.class);

		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getTarget(), equalTo(InterfaceWrapper.class));
		assertThat(partlyTypeMatchingInfos.getMatchedSourceMethods().size(), equalTo(7));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(3));
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

		partlyTypeMatchingInfos = matcher.calculateTypeMatchingInfos(Interface1.class, InterfaceWrapper.class);

		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getTarget(), equalTo(Interface1.class));
		assertThat(partlyTypeMatchingInfos.getMatchedSourceMethods().size(), equalTo(4));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(4));
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
		MatchingInfo partlyTypeMatchingInfos = matcher.calculateTypeMatchingInfos(EnumNative.class,
				Interface1.class);
		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getTarget(), equalTo(EnumNative.class));
		assertThat(partlyTypeMatchingInfos.getMatchedSourceMethods().size(), equalTo(7));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(3));
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
		
		partlyTypeMatchingInfos = matcher.calculateTypeMatchingInfos(Enum2.class,
				Interface1.class);

		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getTarget(), equalTo(Enum2.class));
		assertThat(partlyTypeMatchingInfos.getMatchedSourceMethods().size(), equalTo(7));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(7));
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
		
		partlyTypeMatchingInfos = matcher.calculateTypeMatchingInfos(EnumNative.class,
				InterfaceWrapper.class);

		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getTarget(), equalTo(EnumNative.class));
		assertThat(partlyTypeMatchingInfos.getMatchedSourceMethods().size(), equalTo(4));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(4));
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
				assertThat(entry.getValue().get().size(), equalTo(12));
				break;
			default:
				fail("no MatchingMethodInfo for " + entry.getKey().getName());
				break;
			}
		}

		partlyTypeMatchingInfos = matcher.calculateTypeMatchingInfos(Enum2.class,
				InterfaceWrapper.class);
		
		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getTarget(), equalTo(Enum2.class));
		assertThat(partlyTypeMatchingInfos.getMatchedSourceMethods().size(), equalTo(4));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(4));
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
				assertThat(entry.getValue().get().size(), equalTo(13));
				break;
			default:
				fail("no MatchingMethodInfo for " + entry.getKey().getName());
				break;
			}
		}
	}

	@Test
	public void class2interface_partly_match() {
		MatchingInfo partlyTypeMatchingInfos = matcher.calculateTypeMatchingInfos(Class1.class,
				Interface1.class);
		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getTarget(), equalTo(Class1.class));
		assertThat(partlyTypeMatchingInfos.getMatchedSourceMethods().size(), equalTo(7));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(5));
		for (Entry<Method, Supplier<Collection<MethodMatchingInfo>>> entry : partlyTypeMatchingInfos
				.getMethodMatchingInfoSupplier().entrySet()) {
			switch (entry.getKey().getName()) {
			case "getTrue":
			case "getFalse":
				assertThat(entry.getValue().get().size(), equalTo(4));
				break;
			case "getOne":
				assertThat(entry.getValue().get().size(), equalTo(3));
				break;
			case "subPartlyNativeWrapped":
			case "addPartlyNativeWrapped":
				assertThat(entry.getValue().get().size(), equalTo(4));
				break;
			case "subPartlyWrapped":
			case "addPartlyWrapped":
				assertThat(entry.getValue().get().size(), equalTo(20));
				break;
			default:
				fail("no MatchingMethodInfo for " + entry.getKey().getName());
				break;
			}
		}
		
		partlyTypeMatchingInfos = matcher.calculateTypeMatchingInfos(Class2.class,
				Interface1.class);
		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getTarget(), equalTo(Class2.class));
		assertThat(partlyTypeMatchingInfos.getMatchedSourceMethods().size(), equalTo(7));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(5));
		for (Entry<Method, Supplier<Collection<MethodMatchingInfo>>> entry : partlyTypeMatchingInfos
				.getMethodMatchingInfoSupplier().entrySet()) {
			switch (entry.getKey().getName()) {
			case "getTrue":
			case "getFalse":
				assertThat(entry.getValue().get().size(), equalTo(4));
				break;
			case "getOne":
				assertThat(entry.getValue().get().size(), equalTo(3));
				break;
			case "subPartlyNativeWrapped":
			case "addPartlyNativeWrapped":
				assertThat(entry.getValue().get().size(), equalTo(16));
				break;
			case "subPartlyWrapped":
			case "addPartlyWrapped":
				assertThat(entry.getValue().get().size(), equalTo(64));
				break;
			default:
				fail("no MatchingMethodInfo for " + entry.getKey().getName());
				break;
			}
		}
		
		partlyTypeMatchingInfos = matcher.calculateTypeMatchingInfos(Class1.class,
				InterfaceWrapper.class);
		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getTarget(), equalTo(Class1.class));
		assertThat(partlyTypeMatchingInfos.getMatchedSourceMethods().size(), equalTo(4));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(2));
		for (Entry<Method, Supplier<Collection<MethodMatchingInfo>>> entry : partlyTypeMatchingInfos
				.getMethodMatchingInfoSupplier().entrySet()) {
			switch (entry.getKey().getName()) {
			case "getNull":
				assertThat(entry.getValue().get().size(), equalTo(6));
				break;
			case "getOne":
				assertThat(entry.getValue().get().size(), equalTo(1));
				break;
			default:
				fail("no MatchingMethodInfo for " + entry.getKey().getName());
				break;
			}
		}

		partlyTypeMatchingInfos = matcher.calculateTypeMatchingInfos(Class2.class,
				InterfaceWrapper.class);
		
		assertThat(partlyTypeMatchingInfos, notNullValue());
		assertThat(partlyTypeMatchingInfos.getTarget(), equalTo(Class2.class));
		assertThat(partlyTypeMatchingInfos.getMatchedSourceMethods().size(), equalTo(4));
		assertThat(partlyTypeMatchingInfos.getMethodMatchingInfoSupplier().size(), equalTo(2));
		for (Entry<Method, Supplier<Collection<MethodMatchingInfo>>> entry : partlyTypeMatchingInfos
				.getMethodMatchingInfoSupplier().entrySet()) {
			switch (entry.getKey().getName()) {
			case "getNull":
				assertThat(entry.getValue().get().size(), equalTo(6));
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

}
