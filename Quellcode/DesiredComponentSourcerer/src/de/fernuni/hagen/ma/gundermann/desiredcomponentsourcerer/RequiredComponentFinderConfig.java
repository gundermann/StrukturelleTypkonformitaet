package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import java.util.Optional;
import java.util.function.Function;

import de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util.Preconditions;

public class RequiredComponentFinderConfig {

	private final Class<?>[] providedInterfaces;
	private final Function<Class<?>, Optional<?>> providedImplementationGetters;
	private boolean useLMF;
	private boolean usePTTF;
	private boolean useBL_NMC;


	private RequiredComponentFinderConfig(Class<?>[] providedInterfaces, Function<Class<?>, Optional<?>> providedImplementationGetters) {
		this.providedInterfaces = providedInterfaces;
		this.providedImplementationGetters = providedImplementationGetters;
	}
	
	public Class<?>[] getProvidedInterfaces() {
		return providedInterfaces;
	}
	

	public Function<Class<?>, Optional<?>> getProvidedImplementationGetter() {
		return providedImplementationGetters;
	}
	
	public boolean useHeuristicLMF() {
		return useLMF;
	}

	public boolean useHeuristicPTTF() {
		return usePTTF;
	}

	public boolean useHeuristicBL_NMC() {
		return useBL_NMC;
	}
	
	private void useHeuristc(Heuristic heuristic) {
		switch (heuristic) {
		case LMF:
			this.useLMF = true;
			break;
		case PTTF:
			this.usePTTF = true;
			break;
		case BL_NMC:
			this.useBL_NMC = true;
			break;
		}
	}
	
	public static class Builder {

		private final RequiredComponentFinderConfig config;

		public Builder(Class<?>[] providedInterfaces, Function<Class<?>, Optional<?>> providedImplementationGetters) {
			Preconditions.argNotNull(providedInterfaces, "providedInterfaces");
			Preconditions.argNotNull(providedImplementationGetters, "providedImplementationGetters");
			config = new RequiredComponentFinderConfig(providedInterfaces, providedImplementationGetters);
		}

		public RequiredComponentFinderConfig build() {
			return config;
		}

		public Builder useHeuristicLMF() {
			config.useHeuristc(Heuristic.LMF);
			return this;
		}

		public Builder useHeuristicPTTF() {
			config.useHeuristc(Heuristic.PTTF);
			return this;
		}

		public Builder useHeuristicBL_NMC() {
			config.useHeuristc(Heuristic.BL_NMC);
			return this;
		}

	}

}
