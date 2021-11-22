package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

import org.junit.Test;

public class RequiredComponentFinderConfigBuilderTest {
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenProvidedInterfacesIsNull() {
		new DesiredComponentFinderConfig.Builder(null, a-> Optional.empty());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenOptProvidedImplementationGettersIsNull() {
		new DesiredComponentFinderConfig.Builder(new Class<?>[] {}, null);
	}
	
	@Test
	public void shouldCreateWithGivenParams() {
		Class<?>[] providedInterfaces = new Class<?>[] {String.class, Integer.class};
		Function<Class<?>, Optional<?>> providedImplementationGetter = c -> Optional.empty();
		DesiredComponentFinderConfig config = new DesiredComponentFinderConfig.Builder(providedInterfaces, providedImplementationGetter).build();
		assertNotNull(config.getProvidedInterfaces());
		assertThat(config.getProvidedInterfaces().length, equalTo(2));
		assertThat(Arrays.asList(config.getProvidedInterfaces()), hasItems(String.class, Integer.class));
		
		assertNotNull(config.getProvidedImplementationGetter());
		assertThat(config.getProvidedImplementationGetter(), equalTo(providedImplementationGetter));
	}
	
	

	@Test
	public void shouldCreateWithConfiguredHeuristicLMF() {
		Class<?>[] providedInterfaces = new Class<?>[] {String.class, Integer.class};
		Function<Class<?>, Optional<?>> providedImplementationGetter = c -> Optional.empty();
		DesiredComponentFinderConfig config = new DesiredComponentFinderConfig.Builder(providedInterfaces, providedImplementationGetter)
				.useHeuristicLMF()
				.build();
		
		assertNotNull(config.getProvidedInterfaces());
		assertThat(config.getProvidedInterfaces().length, equalTo(2));
		assertThat(Arrays.asList(config.getProvidedInterfaces()), hasItems(String.class, Integer.class));
		
		assertNotNull(config.getProvidedImplementationGetter());
		assertThat(config.getProvidedImplementationGetter(), equalTo(providedImplementationGetter));
		
		assertTrue(config.useHeuristicLMF());
		assertFalse(config.useHeuristicPTTF());
		assertFalse(config.useHeuristicBL_NMC());
	}
	
	@Test
	public void shouldCreateWithConfiguredHeuristicPTTF() {
		Class<?>[] providedInterfaces = new Class<?>[] {String.class, Integer.class};
		Function<Class<?>, Optional<?>> providedImplementationGetter = c -> Optional.empty();
		DesiredComponentFinderConfig config = new DesiredComponentFinderConfig.Builder(providedInterfaces, providedImplementationGetter)
				.useHeuristicPTTF()
				.build();
		
		assertNotNull(config.getProvidedInterfaces());
		assertThat(config.getProvidedInterfaces().length, equalTo(2));
		assertThat(Arrays.asList(config.getProvidedInterfaces()), hasItems(String.class, Integer.class));
		
		assertNotNull(config.getProvidedImplementationGetter());
		assertThat(config.getProvidedImplementationGetter(), equalTo(providedImplementationGetter));
		
		assertFalse(config.useHeuristicLMF());
		assertTrue(config.useHeuristicPTTF());
		assertFalse(config.useHeuristicBL_NMC());
	}
	
	@Test
	public void shouldCreateWithConfiguredHeuristicBL_NMC() {
		Class<?>[] providedInterfaces = new Class<?>[] {String.class, Integer.class};
		Function<Class<?>, Optional<?>> providedImplementationGetter = c -> Optional.empty();
		DesiredComponentFinderConfig config = new DesiredComponentFinderConfig.Builder(providedInterfaces, providedImplementationGetter)
				.useHeuristicBL_NMC()
				.build();
		
		assertNotNull(config.getProvidedInterfaces());
		assertThat(config.getProvidedInterfaces().length, equalTo(2));
		assertThat(Arrays.asList(config.getProvidedInterfaces()), hasItems(String.class, Integer.class));
		
		assertNotNull(config.getProvidedImplementationGetter());
		assertThat(config.getProvidedImplementationGetter(), equalTo(providedImplementationGetter));
		
		assertFalse(config.useHeuristicLMF());
		assertFalse(config.useHeuristicPTTF());
		assertTrue(config.useHeuristicBL_NMC());
	}
	
	@Test
	public void shouldCreateWithConfiguredHeuristic_All() {
		Class<?>[] providedInterfaces = new Class<?>[] {String.class, Integer.class};
		Function<Class<?>, Optional<?>> providedImplementationGetter = c -> Optional.empty();
		DesiredComponentFinderConfig config = new DesiredComponentFinderConfig.Builder(providedInterfaces, providedImplementationGetter)
				.useHeuristicBL_NMC()
				.useHeuristicLMF()
				.useHeuristicPTTF()
				.build();
		
		assertNotNull(config.getProvidedInterfaces());
		assertThat(config.getProvidedInterfaces().length, equalTo(2));
		assertThat(Arrays.asList(config.getProvidedInterfaces()), hasItems(String.class, Integer.class));
		
		assertNotNull(config.getProvidedImplementationGetter());
		assertThat(config.getProvidedImplementationGetter(), equalTo(providedImplementationGetter));
		
		assertTrue(config.useHeuristicLMF());
		assertTrue(config.useHeuristicPTTF());
		assertTrue(config.useHeuristicBL_NMC());
	}
	
}
