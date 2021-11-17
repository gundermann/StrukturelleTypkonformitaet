package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.heuristics;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatcherCombiner;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.ExactTypeMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.GenSpecTypeMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.StructuralTypeMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.TypeMatcher;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.types.ContainerTypeMatcher;

public enum TypeMatcherManager {
	INSTANCE;

	private StructuralTypeMatcher[] typeMatchers;

	private TypeMatcherManager() {
		TypeMatcher exactTM = new ExactTypeMatcher();
		TypeMatcher genSpecTM = new GenSpecTypeMatcher();
		TypeMatcher combinedGenSpecExactTM = MatcherCombiner.combine(genSpecTM, exactTM).get();
		TypeMatcher wrappedTM = new ContainerTypeMatcher(() -> combinedGenSpecExactTM);
		TypeMatcher combinedWrappedGenSpecExact = MatcherCombiner.combine(genSpecTM, exactTM, wrappedTM).get();
		StructuralTypeMatcher structWrappedGenSpecExactTM = new StructuralTypeMatcher(
				() -> combinedWrappedGenSpecExact);
		typeMatchers = new StructuralTypeMatcher[] { structWrappedGenSpecExactTM };
	}

	public static StructuralTypeMatcher[] getMainTypeMatcher() {
		return INSTANCE.getMainMatcherArray();
	}

	private StructuralTypeMatcher[] getMainMatcherArray() {
		return this.typeMatchers;
	}

	public static StructuralTypeMatcher[] createMainMatcher(TypeMatcher[] fullTypeMatcher) {
		INSTANCE.reorgansizeMatchers(fullTypeMatcher);
		return INSTANCE.getMainMatcherArray();
	}

	private void reorgansizeMatchers(TypeMatcher[] fullTypeMatcher) {
		this.typeMatchers = Stream.of(fullTypeMatcher).map(m -> new StructuralTypeMatcher(() -> m))
				.collect(Collectors.toList()).toArray(new StructuralTypeMatcher[] {});
	}

}
