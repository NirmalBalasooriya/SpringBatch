package com.nirmal.springbatch.processor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nirmal.springbatch.model.Book;

/**
 * Intermediate processor class for the process books
 * @author User
 *
 */
public class BookProcessor implements org.springframework.batch.item.ItemProcessor<Book,Book>{

    private static final Logger log = LoggerFactory.getLogger(BookProcessor.class);


	@Override
	public Book process(Book book) throws Exception {
		final String name = book.getName().toUpperCase();
        final String auther = book.getAuther().toUpperCase();

        final Book changedBook = new Book(book.getIsbmNumber(),name, book.getDescription(), auther);

        log.info("Converting (" + book + ") into (" + changedBook + ")");
        return changedBook;
	}

}