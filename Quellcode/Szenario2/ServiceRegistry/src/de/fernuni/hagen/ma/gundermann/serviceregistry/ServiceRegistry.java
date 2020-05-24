package de.fernuni.hagen.ma.gundermann.serviceregistry;

public class ServiceRegistry {

	
	public static <S> S findFirst(Class<S> serviceType) {
		return null;
	}
	
	public static <S> S findFirstStructurallyMatching(Class<S> structurallServiceType) {
		return null;
	}
	
	public static <S> void register(Class<S> serviceType, S service) {
		
	}
}
