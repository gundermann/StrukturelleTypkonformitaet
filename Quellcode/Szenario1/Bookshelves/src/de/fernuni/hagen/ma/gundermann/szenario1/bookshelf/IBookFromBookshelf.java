package de.fernuni.hagen.ma.gundermann.szenario1.bookshelf;

public interface IBookFromBookshelf {

	String getTitle();
	
	int getYear();
	
	String getAuthor();
	
	String getIsbn();
	
	default String getSignature() {
		return getAuthor() + getYear();
	}
}
