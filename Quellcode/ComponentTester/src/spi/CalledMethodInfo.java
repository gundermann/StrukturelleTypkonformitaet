package spi;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public interface CalledMethodInfo {

	void addCalledMethod(Method m);

	Collection<Method> getCalledMethods();

	default Method getMethod(String name, Class<?> type) {
		try {
			Optional<Method> findFirst = Stream.of(type.getDeclaredMethods()).filter(m -> m.getName().equals(name)).findFirst();
			return findFirst.orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
