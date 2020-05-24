package de.fernuni.hagen.ma.gundermann.typkonverter.structureBySignatures;

public interface ProxyFactory<T> {

  T createProxy( Object convertationObject );

}
