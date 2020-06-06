package de.fernuni.hagen.ma.gundermann.szenario1.bookshelf;

public interface IBookFromBookshelf {

	String getTitle();
	
	int getYear();
	
	String getAuthor();
	
	default String getIsbn() {
		return "";
	}
	
}
