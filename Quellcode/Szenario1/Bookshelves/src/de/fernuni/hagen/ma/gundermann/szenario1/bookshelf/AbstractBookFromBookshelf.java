package de.fernuni.hagen.ma.gundermann.szenario1.bookshelf;

public abstract class AbstractBookFromBookshelf implements IBookFromBookshelf {

	// Unterschied zum Interface: In der abstrakten Klasse gibt es einen Zustand,
	// der über die Instanzvariablen definiert werden.
	private String signatur;

	public AbstractBookFromBookshelf() {
		this.signatur = getAuthor() + getYear();
	}

	public void setSignatur(String signatur) {
		this.signatur = signatur;
	}

	@Override
	public String getSignature() {
		return signatur;
	}

}
