package com.learning.PGdatabase.services.impl;

import com.learning.PGdatabase.domain.BookEntity;
import com.learning.PGdatabase.repository.BookRepository;
import com.learning.PGdatabase.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookEntity saveBook(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);
        return bookRepository.save(bookEntity);
    }

    @Override
    public List<BookEntity> getAllBooks() {
        return StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                .toList();
    }

    @Override
    public Page<BookEntity> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Optional<BookEntity> getBook(String isbn) {
        return bookRepository.findById(isbn);
    }

    @Override
    public Boolean exists(String isbn) {
        return bookRepository.existsById(isbn);
    }

    @Override
    public BookEntity partialUpdateBook(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);
        return bookRepository.findById(isbn).map(currentBook -> {
            Optional.ofNullable(bookEntity.getTitle()).ifPresent(currentBook::setTitle);
            Optional.ofNullable(bookEntity.getAuthor()).ifPresent(currentBook::setAuthor);
            return bookRepository.save(currentBook);
        }).orElseThrow(() -> new RuntimeException("Book with isbn " + isbn + " doesn't exist"));
    }

    @Override
    public void deleteBook(String isbn) {
        bookRepository.deleteById(isbn);
    }
}
