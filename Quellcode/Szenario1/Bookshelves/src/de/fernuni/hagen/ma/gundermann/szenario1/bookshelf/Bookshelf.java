package de.fernuni.hagen.ma.gundermann.szenario1.bookshelf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.fernuni.hagen.ma.gundermann.szenario1.dokumente.AbstractDocument;
import de.fernuni.hagen.ma.gundermann.typkonverter.ConformityCheckingBase;
import de.fernuni.hagen.ma.gundermann.typkonverter.StructureDefinition;
import de.fernuni.hagen.ma.gundermann.typkonverter.TypeConverter;
import de.fernuni.hagen.ma.gundermann.typkonverter.TypeConverterBuilder;

public class Bookshelf {

	private Collection<IBookFromBookshelf> books = new ArrayList<IBookFromBookshelf>();

	private TypeConverter<IBookFromBookshelf> structConverter = TypeConverterBuilder.create(ConformityCheckingBase.NAMES)
			.withStructureDefinition(StructureDefinition.ABSTRACT_METHODS_NECESSARY).build(IBookFromBookshelf.class);

	public void sortByTitle() {

	}

	public void sortByAuthor() {

	}

	public void sortByYear() {

	}

	public void sortByIsbn() {

	}
	
	public void sortBySignature() {

	}

	public boolean isDocumentSuitable(AbstractDocument dokument) {
		return structConverter.matchesStructural(dokument);
	}

	public void addDocument(AbstractDocument dokument) {
		IBookFromBookshelf buchAusBuecherregal = structConverter.convertStructural(dokument);
		books.add(buchAusBuecherregal);
	}

	public int getBookCount() {
		return books.size();
	}

}
