package de.fernuni.hagen.ma.gundermann.szenario1.initialisierer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.fernuni.hagen.ma.gundermann.szenario1.books.Book;
import de.fernuni.hagen.ma.gundermann.szenario1.books.UnpublishedBook;
import de.fernuni.hagen.ma.gundermann.szenario1.bookshelf.Bookshelf;

public class Tests {

	@Test
	public void test() {
		Bookshelf bookshelf = new Bookshelf();
		Book book1 = new Book("A", "Autor1", 2015);
		Book book2 = new Book("B", "Autor2", 2015);
		Book book3 = new Book("C", "Autor3", 2016);
		UnpublishedBook unpublished1 = new UnpublishedBook("A", 2015);

		assertTrue(bookshelf.isDocumentSuitable(book1));
		assertTrue(bookshelf.isDocumentSuitable(book2));
		assertTrue(bookshelf.isDocumentSuitable(book3));
		assertFalse(bookshelf.isDocumentSuitable(unpublished1));

		bookshelf.addDocument(book1);
		bookshelf.addDocument(book2);
		bookshelf.addDocument(book3);
		assertTrue(bookshelf.getBookCount() == 3);
	}

}
