package com.learning.PGdatabase.controllers;

import com.learning.PGdatabase.domain.AuthorEntity;
import com.learning.PGdatabase.domain.DTO.AuthorDto;
import com.learning.PGdatabase.mappers.impl.AuthorMapper;
import com.learning.PGdatabase.services.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    public AuthorController(AuthorMapper authorMapper, AuthorService authorService) {
        this.authorMapper = authorMapper;
        this.authorService = authorService;
    }

    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto authorDto) {
        // Turn Dto into Entity
        AuthorEntity authorEntity = authorMapper.mapTo(authorDto);

        // Save author to database
        AuthorEntity savedAuthorEntity = authorService.saveAuthor(authorEntity);

        // Turn back to dto and then return
        return new ResponseEntity<>(authorMapper.mapFrom(savedAuthorEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/authors")
    public ResponseEntity<Page<AuthorDto>> getAllAuthors(Pageable pageable) {
        // Get author page from database
        Page<AuthorEntity> authorEntityPage = authorService.getAllAuthors(pageable);
        return new ResponseEntity<>(authorEntityPage.map(authorMapper::mapFrom), HttpStatus.OK);
    }

    @GetMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable Long id) {
        // Retrieve author
        Optional<AuthorEntity> authorEntityOptional = authorService.getAuthor(id);

        // Handle the optional entity:
        return authorEntityOptional.map(authorEntity -> {
            AuthorDto authorDto = authorMapper.mapFrom(authorEntity);
            return new ResponseEntity<>(authorDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(
            @PathVariable Long id,
            @RequestBody AuthorDto authorDto) {
        // Check if doesn't exist
        if (!authorService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Turn into entity and perform service action
        authorDto.setId(id);
        AuthorEntity authorEntity = authorMapper.mapTo(authorDto);
        AuthorEntity updatedAuthor = authorService.saveAuthor(authorEntity);
        return new ResponseEntity<>(authorMapper.mapFrom(updatedAuthor), HttpStatus.OK);
    }

    @PatchMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> partialUpdateAuthor(
            @PathVariable Long id,
            @RequestBody AuthorDto authorDto) {
        if (!authorService.exists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        AuthorEntity authorEntity = authorMapper.mapTo(authorDto);
        AuthorEntity updatedAuthor = authorService.partialUpdateAuthor(id, authorEntity);
        return new ResponseEntity<>(authorMapper.mapFrom(updatedAuthor), HttpStatus.OK);
    }

    @DeleteMapping(path = "/authors/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        if (!authorService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        authorService.deleteAuthor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
