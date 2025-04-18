package com.learning.PGdatabase.controllers;

import com.learning.PGdatabase.domain.AuthorEntity;
import com.learning.PGdatabase.domain.BookEntity;
import com.learning.PGdatabase.domain.DTO.BookDto;
import com.learning.PGdatabase.mappers.impl.AuthorMapper;
import com.learning.PGdatabase.mappers.impl.BookMapper;
import com.learning.PGdatabase.services.AuthorService;
import com.learning.PGdatabase.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;
    private final BookService bookService;
    private final AuthorService authorService;

    public BookController(BookMapper bookMapper, AuthorMapper authorMapper, BookService bookService, AuthorService authorService) {
        this.bookMapper = bookMapper;
        this.authorMapper = authorMapper;
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> createBook(
            @PathVariable String isbn,
            @RequestBody BookDto bookDto
    ) {
        // Turn Dto into Entity
        BookEntity bookEntity = bookMapper.mapTo(bookDto);

        // Check if book exists in the database
        boolean condition = bookService.exists(isbn);

        // Check if author exists in the database
        if (authorService.exists(bookEntity.getAuthor().getId())) {
            Optional<AuthorEntity> existingAuthor = authorService.getAuthor(bookEntity.getAuthor().getId());
            bookEntity.setAuthor(existingAuthor.get());
        }

        // Save book
        BookEntity savedBookEntity = bookService.saveBook(isbn, bookEntity);

        if (condition) {
            return new ResponseEntity<>(bookMapper.mapFrom(savedBookEntity), HttpStatus.OK);
        }
        return new ResponseEntity<>(bookMapper.mapFrom(savedBookEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/books")
    public ResponseEntity<Page<BookDto>> getAllBooks(Pageable pageable) {
        // Get book page from database
        Page<BookEntity> bookEntityPage = bookService.getAllBooks(pageable);

        // Return book page dto
        return new ResponseEntity<>(bookEntityPage.map(bookMapper::mapFrom), HttpStatus.OK);
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> getBook (@PathVariable String isbn) {
        // Retrieve book from database
        Optional<BookEntity> retrievedBook = bookService.getBook(isbn);
        return retrievedBook.map(bookEntity -> {
            BookDto bookDto = bookMapper.mapFrom(bookEntity);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateBook (
            @PathVariable String isbn,
            @RequestBody BookDto bookDto
    ) {
        if (!bookService.exists(isbn))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        BookEntity bookEntity = bookMapper.mapTo(bookDto);
        BookEntity updatedBook = bookService.partialUpdateBook(isbn, bookEntity);
        return new ResponseEntity<>(bookMapper.mapFrom(updatedBook), HttpStatus.OK);
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable String isbn) {
        if (!bookService.exists(isbn)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookService.deleteBook(isbn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
