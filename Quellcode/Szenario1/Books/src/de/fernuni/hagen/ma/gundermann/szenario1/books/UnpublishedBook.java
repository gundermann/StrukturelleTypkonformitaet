package de.fernuni.hagen.ma.gundermann.szenario1.books;

import de.fernuni.hagen.ma.gundermann.szenario1.dokumente.AbstractDocument;

public class UnpublishedBook extends AbstractDocument {

	private String author;
	
	private int year;

	public UnpublishedBook(String title, int year) {
		super(title);
		this.year = year;
	}

	public String getAuthor() {
		return author;
	}

	public int getYear() {
		return year;
	}

	

}
