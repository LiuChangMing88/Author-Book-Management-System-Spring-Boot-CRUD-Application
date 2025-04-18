package com.learning.PGdatabase.services.impl;

import com.learning.PGdatabase.domain.AuthorEntity;
import com.learning.PGdatabase.repository.AuthorRepository;
import com.learning.PGdatabase.services.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {
    AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorEntity saveAuthor(AuthorEntity authorEntity) {
        return authorRepository.save(authorEntity);
    }

    @Override
    public List<AuthorEntity> getAllAuthors() {
        return StreamSupport.stream(authorRepository.findAll().spliterator(), false)
                .toList();
    }

    @Override
    public Page<AuthorEntity> getAllAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    @Override
    public Optional<AuthorEntity> getAuthor(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public Boolean exists(Long id) {
        return authorRepository.existsById(id);
    }

    @Override
    public AuthorEntity partialUpdateAuthor(Long id, AuthorEntity authorEntity) {
        authorEntity.setId(id);
        return authorRepository.findById(id).map(currentAuthor -> {
            Optional.ofNullable(authorEntity.getName()).ifPresent(currentAuthor::setName);
            Optional.ofNullable(authorEntity.getAge()).ifPresent(currentAuthor::setAge);
            return authorRepository.save(currentAuthor);
        }).orElseThrow(() -> new RuntimeException("Author with id " + id + " doesn't exist"));
    }

    @Override
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}
