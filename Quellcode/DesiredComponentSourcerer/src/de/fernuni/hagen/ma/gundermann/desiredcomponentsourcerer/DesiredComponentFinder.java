package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.BestMatchingComponentCombinationFinder;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.combination.CombinationInfo;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics.DefaultTypeMatcherHeuristic;
import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Logger;
import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.glue.ConvertableBundle;
import de.fernuni.hagen.ma.gundermann.signaturematching.glue.ConvertableComponent;
import de.fernuni.hagen.ma.gundermann.signaturematching.glue.TypeConverter;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.StructuralTypeMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.TypeMatcher;
import tester.ComponentTester;
import tester.TestResult;
import tester.TestResult.Cause;

public class DesiredComponentFinder {

	private StructuralTypeMatcher[] mainTypeMatcher = DefaultTypeMatcherHeuristic.getMainTypeMatcher();

	private final Class<?>[] registeredComponentInterfaces;

	private final Function<Class<?>, Optional<?>> optComponentGetter;

	private int testedComponentVariations = 0;

	private Collection<Heuristic> usedHeuristics = new ArrayList<Heuristic>();

	public DesiredComponentFinder(DesiredComponentFinderConfig config) {
		this.registeredComponentInterfaces = config.getProvidedInterfaces();
		this.optComponentGetter = config.getProvidedImplementationGetter();
		if (config.useHeuristicLMF()) {
			usedHeuristics.add(Heuristic.LMF);
		}
		if (config.useHeuristicPTTF()) {
			usedHeuristics.add(Heuristic.PTTF);
		}
		if (config.useHeuristicBL_NMC()) {
			usedHeuristics.add(Heuristic.BL_NMC);
		}
	}

	@Deprecated
	public DesiredComponentFinder(Class<?>[] registeredComponentInterfaces,
			Function<Class<?>, Optional<?>> optComponentGetter) {
		this.registeredComponentInterfaces = Stream.of(registeredComponentInterfaces).distinct()
				.collect(Collectors.toList()).toArray(new Class[] {});
		this.optComponentGetter = optComponentGetter;

	}

	public void setFullTypeMatcher(TypeMatcher[] fullTypeMatcher) {
		this.mainTypeMatcher = DefaultTypeMatcherHeuristic.createMainMatcher(fullTypeMatcher);
	}

	private Optional<?> getComponent(Class<?> componentClass) {
		return optComponentGetter.apply(componentClass);
	}

	public <DesiredInterface> DesiredInterface getDesiredComponent(Class<DesiredInterface> desiredInterface) {
		Logger.info("search component by partly match: " + desiredInterface.getName());
		for (int i = 0; i < mainTypeMatcher.length; i++) {
			Optional<DesiredInterface> optDesiredBean = findDesiredComponentByPartlyMatcher(desiredInterface,
					mainTypeMatcher[i]);
			if (optDesiredBean.isPresent()) {
				Logger.info("component found");
				Logger.infoF("Tested Components variations: %d", testedComponentVariations);
				return optDesiredBean.get();
			}
			Logger.info("component not found");
		}

		Logger.infoF("Tested Components variations: %d", testedComponentVariations);
		return null;
	}

	private <DesiredInterface> Optional<DesiredInterface> findDesiredComponentByPartlyMatcher(
			Class<DesiredInterface> desiredInterface, StructuralTypeMatcher typeMatcher) {
		Logger.infoF("start search with matcher: %s", typeMatcher.getClass().getSimpleName());

		Map<Class<?>, MatchingInfo> componentInterface2PartlyMatchingInfos = findPartlyMatchingComponentInterfaces(
				desiredInterface, typeMatcher);

		// INFO OUTPUT
		componentInterface2PartlyMatchingInfos.values().forEach(i -> {
			Logger.toFile("%f;%s;%b;%s;", i.getQualitativeMatchRating().getMatcherRating(),
					i.getQualitativeMatchRating().toString(), i.isFullMatching(), i.getTarget().getSimpleName());
		});

		Optional<DesiredInterface> result = Optional
				.ofNullable(getCombinedMatchingComponent(desiredInterface, componentInterface2PartlyMatchingInfos));
		Logger.infoF("finish search with matcher: %s", typeMatcher.getClass().getSimpleName());
		return result;
	}

	private <DesiredInterface> Map<Class<?>, MatchingInfo> findPartlyMatchingComponentInterfaces(
			Class<DesiredInterface> desiredInterface, StructuralTypeMatcher typeMatcher) {
		Map<Class<?>, MatchingInfo> matchedBeans = new HashMap<>();
		for (Class<?> beanInterface : getRegisteredComponentInterfaces()) {
			boolean matchesPartly = typeMatcher.matchesType(beanInterface, desiredInterface);
			if (!matchesPartly) {
				continue;
			}
			matchedBeans.put(beanInterface, typeMatcher.calculateTypeMatchingInfos(beanInterface, desiredInterface));
		}
		return matchedBeans;
	}

	private <DesiredInterface> DesiredInterface getCombinedMatchingComponent(Class<DesiredInterface> desiredInterface,
			Map<Class<?>, MatchingInfo> componentInterface2PartlyMatchingInfos) {
		Logger.info("create ComponentInfos");
		BestMatchingComponentCombinationFinder combinationFinder = new BestMatchingComponentCombinationFinder(
				componentInterface2PartlyMatchingInfos, this.usedHeuristics);

		while (combinationFinder.hasNextCombination()) {
			CombinationInfo combinationInfos = combinationFinder.getNextCombination();
			try {
				TestedComponent<DesiredInterface> testedComponent = getPartlyMatchedTestedComponent(combinationInfos,
						desiredInterface);
				if (testedComponent != null) {
					if (testedComponent.allTestsPassed()) {
						return testedComponent.getComponent();
					}
					if (usedHeuristics.contains(Heuristic.PTTF)
							&& testedComponent.anyTestPassed()) {
						// H: combinate passed tests components first
						combinationFinder.optimizeForCurrentCombination();
					}
					if (testedComponent.foundBlacklistedMMICombi()) {
						// H: blacklist by pivot test calls
						// H: blacklist failed single methods tested
						combinationFinder.optimizeMatchingInfoBlacklist(testedComponent.getBlacklistedMMICombis());
					}
				}
			} catch (NoComponentImplementationFoundException e) {
				// H: blacklist if no implementation available
				combinationFinder.optimizeCheckTypeBlacklist(e.getComponentInterface());
			}
		}
		return null;
	}

	private <DesiredInterface> TestedComponent<DesiredInterface> getPartlyMatchedTestedComponent(
			CombinationInfo combinationInfos, Class<DesiredInterface> desiredInterface)
			throws NoComponentImplementationFoundException {
		Logger.infoF("find components for combination: %s", combinationInfos.getComponentClasses().stream()
				.map(c -> c.toString()).collect(Collectors.joining(" + ")));

		testedComponentVariations++;
		TypeConverter<DesiredInterface> converter = new TypeConverter<>(desiredInterface);
		ComponentTester<DesiredInterface> componentTester = new ComponentTester<>(desiredInterface);

		Map<Object, Collection<MethodMatchingInfo>> components2MatchingInfo = new HashMap<>();

		for (Class<?> componentClass : combinationInfos.getComponentClasses()) {
			Optional<?> optComponent = getComponent(componentClass);
			if (!optComponent.isPresent()) {
				throw new NoComponentImplementationFoundException(componentClass);
			}
			components2MatchingInfo.put(optComponent.get(), combinationInfos.getModuleMatchingInfo(componentClass));
		}

		List<ConvertableComponent> convertableComponents = components2MatchingInfo.entrySet().stream()
				.map(e -> new ConvertableComponent(e.getKey(), e.getValue())).collect(Collectors.toList());
		if (!ConvertableBundle.canCreateBundle(convertableComponents)) {
			Logger.infoF("Cannot create convertible bundle");
			return null;
		}
		Logger.infoF("test component: %s",
				combinationInfos.getComponentClasses().stream().map(c -> c.getName()).collect(Collectors.joining(",")));

		DesiredInterface convertedComponent = converter.convert(ConvertableBundle.createBundle(convertableComponents));
		TestResult testResult = componentTester.testComponent(convertedComponent);
		logTestResult(testResult);
		TestedComponent<DesiredInterface> testedComponent = new TestedComponent<>(convertedComponent, testResult);

		// H: blacklist failed method calls
		if (usedHeuristics.contains(Heuristic.BL_NMC)
//				HeuristicSetting.BLACKLIST_FAILED_TRIED_METHOD_CALLS
		) {
			Collection<Method> failedMethodCombi = new ArrayList<Method>(testResult.getTriedMethodCalls());
			Logger.infoF("blacklist by failed method combination: %s",
					failedMethodCombi.stream().map(m -> m.getName()).collect(Collectors.joining(",")));

			List<MethodMatchingInfo> failedMMICombi = components2MatchingInfo.values().stream()
					.flatMap(Collection::stream).filter(mmi -> failedMethodCombi.contains(mmi.getSource()))
					.collect(Collectors.toList());
			Logger.infoF("blacklisting mmi found: %d", failedMMICombi.size());
			testedComponent.addBlacklistedMMICombi(failedMMICombi);
		}

		return testedComponent;
	}

	private void logTestResult(TestResult testResult) {
		Logger.infoF("passed tests: %d/%d", testResult.getPassedTests(), testResult.getTestCount());
		switch (testResult.getResult()) {
		case FAILED:
			Logger.infoF("called Methods: %s",
					testResult.getTriedMethodCalls().stream().map(Method::getName).collect(Collectors.joining(",")));
//			Logger.infoF("test interpretation: %s",
//					testResult.getTriedMethodCalls().size() == 1 ? "Single-Method-Test" : "Multi-Method-Test");
			break;
		case CANCELED:
			Logger.infoF("test canceled: caused by %s\n%s\n%s", testResult.getCause(),
					testResult.getException().getMessage(), Stream.of(testResult.getException().getStackTrace())
							.map(StackTraceElement::toString).map(s -> "\t\t\t" + s).collect(Collectors.joining("\n")));
			if (testResult.getCause() == Cause.FAILED_DELEGATION) {
				Logger.infoF("failed call of method: %s", testResult.getFailedMethodCall().getName());
			}
			break;

		default:
			break;
		}
	}

	private Class<?>[] getRegisteredComponentInterfaces() {
		return this.registeredComponentInterfaces;
	}

}
