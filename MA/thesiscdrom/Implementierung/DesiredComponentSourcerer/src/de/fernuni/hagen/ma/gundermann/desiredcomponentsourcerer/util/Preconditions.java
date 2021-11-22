package de.fernuni.hagen.ma.gundermann.desiredcomponentsourcerer.util;

public class Preconditions {

	public static void argNotNull(Object arg, String name) {
		if(arg == null) {
			throw new IllegalArgumentException(name + " is null");
		}
	}

}
