package de.fernuni.hagen.ma.gundermann.signaturematching.matching.methods;

import java.lang.reflect.Method;
import java.util.Collection;

import de.fernuni.hagen.ma.gundermann.signaturematching.MethodMatchingInfo;
import de.fernuni.hagen.ma.gundermann.signaturematching.matching.MatcherRate;

public interface MethodMatcher {

  boolean matches( Method checkMethod, Method queryMethod );

  Collection<MethodMatchingInfo> calculateMatchingInfos( Method checkMethod, Method queryMethod );

  MatcherRate matchesWithRating( Method checkMethod, Method queryMethod );
}