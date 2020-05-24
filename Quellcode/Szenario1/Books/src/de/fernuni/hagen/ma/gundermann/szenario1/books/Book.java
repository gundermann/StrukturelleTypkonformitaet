package de.fernuni.hagen.ma.gundermann.szenario1.books;

import de.fernuni.hagen.ma.gundermann.szenario1.dokumente.AbstractDocument;

public class Book extends AbstractDocument {

	private String author;
	
	private int year;
	
	private String isbn;

	public Book(String title, String author, int year) {
		super(title);
		this.author = author;
		this.year = year;
	}


	public String getAuthor() {
		return author;
	}

	public int getYear() {
		return year;
	}

	public String getIsbn() {
		return isbn;
	}
	
	
	
	
}
