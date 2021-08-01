package de.fernuni.hagen.ma.gundermann.ejb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public enum EJBContainer {
	CONTAINER;

	Map<Class<?>, Object> containerMap = new HashMap<>();

	private EJBContainer() {
		try {
			init(Files.readAllLines(Paths.get("src/de/fernuni/hagen/ma/gundermann/ejb/pool.txt")));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void reset() {
		containerMap.clear();
	}

	private void init(List<String> beans) {
		for (String bean : beans) {
			try {
				Class<?> loadedInterface = ClassLoader.getSystemClassLoader().loadClass(bean);
				registerBean(loadedInterface, null);
			} catch (ClassNotFoundException cnfe) {
				// Logger.info( String.format( "class not found %s", bean ) );
			}
		}
		// Logger.info( String.format( "registeres bean interfaces: %d",
		// containerMap.keySet().size() ) );
	}

	public void reInit() {
		containerMap.clear();
		try {
			init(Files.readAllLines(Paths.get("src/de/fernuni/hagen/ma/gundermann/ejb/pool.txt")));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public <BI> void registerBean(Class<BI> beanInterface, BI bean) {
		containerMap.put(beanInterface, bean);
	}

	public Optional<?> getOptBean(Class<?> componentClass) {
		return Optional.ofNullable(containerMap.get(componentClass));
	}

	public Class<?>[] getRegisteredBeanInterfaces() {
		return containerMap.keySet().toArray(new Class[] {});
	}

}
