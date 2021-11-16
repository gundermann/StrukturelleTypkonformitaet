package spi;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Interface fuer Testklasse, die waehrend der Testdurchfuehrung weitere
 * Informationen sammeln sollen. Wird benoetigt fuer die Heuristiken
 * <code>PTTF</code> und <code>BL_NMC</code>.
 * 
 * @author Niels Gundermann
 *
 */

public interface TriedMethodCallsInfo {

	void addTriedMethodCall(Method m);

	Collection<Method> getTriedMethodCalls();

	/**
	 * Ermittelt die Methode mit dem uebergebenen <code>name</code>, welche im
	 * uebergebenen <code>type</code> deklariert wurde.
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	default Method getMethod(String name, Class<?> type) {
		try {
			Optional<Method> findFirst = Stream.of(type.getDeclaredMethods()).filter(m -> m.getName().equals(name))
					.findFirst();
			return findFirst.orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
