package de.fernuni.hagen.ma.gundermann.typkonverter;

public interface TypConverter<T> {
	boolean matchesStructural(Object sourceObject);

	T convertStructural(Object sourceObject);
}
