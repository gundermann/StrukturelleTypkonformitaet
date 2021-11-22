package matching.methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import de.fernuni.hagen.ma.gundermann.signaturematching.SingleMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo.ParamPosition;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatcherRate;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.Setting;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods.MethodDelegationRatingFunctions;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods.MethodMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods.MethodMatchingInfoFactory;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods.MethodStructure;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.TypeMatcher;

class CommonMethodMatcher implements MethodMatcher {

	private final Supplier<TypeMatcher> typeMatcherSupplier;

	public CommonMethodMatcher(Supplier<TypeMatcher> typeMatcherSupplier) {
		this.typeMatcherSupplier = typeMatcherSupplier;
	}

	@Override
	public boolean matches(Method checkMethod, Method queryMethod) {
		MethodStructure cms1 = MethodStructure.createFromDeclaredMethod(checkMethod);
		MethodStructure qms2 = MethodStructure.createFromDeclaredMethod(queryMethod);
		return matches(cms1, qms2);
	}

	@Override
	public Collection<MethodMatchingInfo> calculateMatchingInfos(Method checkMethod, Method queryMethod) {
		if (!matches(checkMethod, queryMethod)) {
			return new ArrayList<>();
		}
		MethodMatchingInfoFactory factory = new MethodMatchingInfoFactory(checkMethod, queryMethod);
		MethodStructure checkStruct = MethodStructure.createFromDeclaredMethod(checkMethod);
		MethodStructure queryStruct = MethodStructure.createFromDeclaredMethod(queryMethod);
		Collection<SingleMatchingInfo> returnTypeMatchingInfos = typeMatcherSupplier.get()
				.calculateTypeMatchingInfos(queryStruct.getReturnType(), checkStruct.getReturnType());
		Map<ParamPosition, Collection<SingleMatchingInfo>> argumentTypesMatchingInfos = calculateArgumentTypesMatchingInfos(
				checkStruct.getSortedArgumentTypes(), queryStruct.getSortedArgumentTypes());
		return factory.createFromTypeMatchingInfos(returnTypeMatchingInfos,
				Collections.singletonList(argumentTypesMatchingInfos));
	}

	private boolean matches(MethodStructure ms1, MethodStructure ms2) {
		if (ms1.getSortedArgumentTypes().length != ms2.getSortedArgumentTypes().length) {
			return false;
		}
		if (!typeMatcherSupplier.get().matchesType(ms1.getReturnType(), ms2.getReturnType())) {
			return false;
		}
		for (int i = 0; i < ms1.getSortedArgumentTypes().length; i++) {
			if (!typeMatcherSupplier.get().matchesType(ms1.getSortedArgumentTypes()[i],
					ms2.getSortedArgumentTypes()[i])) {
				return false;
			}
		}
		return true;
	}

	private Map<ParamPosition, Collection<SingleMatchingInfo>> calculateArgumentTypesMatchingInfos(Class<?>[] checkATs,
			Class<?>[] queryATs) {
		Map<ParamPosition, Collection<SingleMatchingInfo>> matchingMap = new HashMap<>();
		for (int i = 0; i < checkATs.length; i++) {
			Class<?> checkAT = checkATs[i];
			Class<?> queryAT = queryATs[i];
			Collection<SingleMatchingInfo> infos = typeMatcherSupplier.get().calculateTypeMatchingInfos(checkAT,
					queryAT);
			matchingMap.put(new ParamPosition(i, i), infos);
		}

		return matchingMap;
	}

	@Override
	public MatcherRate matchesWithRating(Method checkMethod, Method queryMethod) {
		MethodStructure ms1 = MethodStructure.createFromDeclaredMethod(checkMethod);
		MethodStructure ms2 = MethodStructure.createFromDeclaredMethod(queryMethod);
		return getMatchRating(ms1, ms2);
	}

	/**
	 * Ermittlung der MatcherRate fuer das Matching der uebergebenen Methoden
	 * Hinweis: Hier wird die Funktion mdRating umgesetzt
	 * 
	 * @param ms1
	 * @param ms2
	 * @return
	 */
	private MatcherRate getMatchRating(MethodStructure ms1, MethodStructure ms2) {
		if (ms1.getSortedArgumentTypes().length != ms2.getSortedArgumentTypes().length) {
			return null;
		}

		Collection<MatcherRate> rates = new ArrayList<>();
		MatcherRate returnRate = typeMatcherSupplier.get().matchesWithRating(ms1.getReturnType(), ms2.getReturnType());
		if (returnRate == null) {
			return null;
		}
		rates.add(returnRate);

		for (int i = 0; i < ms1.getSortedArgumentTypes().length; i++) {
			MatcherRate argRating = typeMatcherSupplier.get().matchesWithRating(ms1.getSortedArgumentTypes()[i],
					ms2.getSortedArgumentTypes()[i]);
			if (argRating == null) {
				return null;
			}
			rates.add(argRating);
		}
		return new MatcherRate("COMMON", MethodDelegationRatingFunctions.mRating(rates));
	}
}
