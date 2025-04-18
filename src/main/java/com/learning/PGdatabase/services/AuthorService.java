package com.learning.PGdatabase.services;

import com.learning.PGdatabase.domain.AuthorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    AuthorEntity saveAuthor(AuthorEntity authorEntity);
    List<AuthorEntity> getAllAuthors();
    Page<AuthorEntity> getAllAuthors(Pageable pageable);
    Optional<AuthorEntity> getAuthor(Long id);
    Boolean exists(Long id);

    AuthorEntity partialUpdateAuthor(Long id, AuthorEntity authorEntity);

    void deleteAuthor(Long id);
}
