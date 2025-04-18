package com.learning.PGdatabase.services;

import com.learning.PGdatabase.domain.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {
    BookEntity saveBook(String isbn, BookEntity bookEntity);
    List<BookEntity> getAllBooks();
    Page<BookEntity> getAllBooks(Pageable pageable);
    Optional<BookEntity> getBook(String isbn);
    Boolean exists(String isbn);

    BookEntity partialUpdateBook(String isbn, BookEntity bookEntity);

    void deleteBook(String isbn);
}
