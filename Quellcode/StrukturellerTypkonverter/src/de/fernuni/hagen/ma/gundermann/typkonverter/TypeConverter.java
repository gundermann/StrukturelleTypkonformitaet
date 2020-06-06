package de.fernuni.hagen.ma.gundermann.typkonverter;

public interface TypeConverter<T> {
	boolean matchesStructural(Object sourceObject);

	T convertStructural(Object sourceObject);
}
