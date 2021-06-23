package spi;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public interface TriedMethodCallsInfo {

	void addTriedMethodCall(Method m);

	Collection<Method> getTriedMethodCalls();

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
