package com.learning.PGdatabase.utility;


import com.learning.PGdatabase.domain.AuthorEntity;
import com.learning.PGdatabase.domain.BookEntity;

public class TestDataUtil {
    private TestDataUtil () {}

    public static AuthorEntity createTestAuthorA() {
        return AuthorEntity.builder()
                .name("John Doe")
                .age(30)
                .build();
    }

    public static AuthorEntity createTestAuthorB() {
        return AuthorEntity.builder()
                .name("Jane Doe")
                .age(40)
                .build();
    }

    public static AuthorEntity createTestAuthorC() {
        return AuthorEntity.builder()
                .name("Kane Doe")
                .age(50)
                .build();
    }

    public static BookEntity createTestBookA(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-1-56619-909-4")
                .title("Harry Potter")
                .author(authorEntity)
                .build();
    }

    public static BookEntity createTestBookB(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-1-56619-909-5")
                .title("Lord of the Rings")
                .author(authorEntity)
                .build();
    }

    public static BookEntity createTestBookC(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-1-56619-909-6")
                .title("How to win friends and influence people")
                .author(authorEntity)
                .build();
    }
}
