package com.learning.PGdatabase.repository;

import com.learning.PGdatabase.domain.AuthorEntity;
import com.learning.PGdatabase.domain.BookEntity;
import com.learning.PGdatabase.utility.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookEntityRepositoryIntegrationTests {
    private final BookRepository underTest;

    @Autowired
    public BookEntityRepositoryIntegrationTests(BookRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatBookCanBeCreatedAndRead() {
        // Create test objects
        final AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        final BookEntity testBookEntity = TestDataUtil.createTestBookA(testAuthorEntity);

        // Save to database and then retrieve
        underTest.save(testBookEntity);
        Optional<BookEntity> underTestFindOne = underTest.findById(testBookEntity.getIsbn());

        // Assert
        assertThat(underTestFindOne).isPresent();
        assertThat(underTestFindOne.get()).isEqualTo(testBookEntity);
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRead() {
        // Create test books and authors
        final AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        final BookEntity testBookEntity1 = TestDataUtil.createTestBookA(testAuthorEntity);
        final BookEntity testBookEntity2 = TestDataUtil.createTestBookB(testAuthorEntity);
        final BookEntity testBookEntity3 = TestDataUtil.createTestBookC(testAuthorEntity);

        // Insert into books
        underTest.save(testBookEntity1);
        underTest.save(testBookEntity2);
        underTest.save(testBookEntity3);

        Iterable<BookEntity> testBookList = underTest.findAll();
        assertThat(testBookList).containsExactly(
                testBookEntity1,
                testBookEntity2,
                testBookEntity3
        );
    }

    @Test
    public void testThatBookIsCorrectlyUpdated() {
        // Create test author and book
        final AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        final BookEntity testBookEntity = TestDataUtil.createTestBookA(testAuthorEntity);

        // Insert into database
        underTest.save(testBookEntity);

        // Retrieve book and test
        Optional<BookEntity> updatedBook = underTest.findById(testBookEntity.getIsbn());
        assertThat(updatedBook).isPresent();
        assertThat(updatedBook.get()).isEqualTo(testBookEntity);

        // Update book
        testBookEntity.setTitle("Test Title");
        underTest.save(testBookEntity);

        // Retrieve book and test
        updatedBook = underTest.findById(testBookEntity.getIsbn());
        assertThat(updatedBook).isPresent();
        assertThat(updatedBook.get()).isEqualTo(testBookEntity);
    }

    @Test
    public void testThatBookIsCorrectlyDeleted() {
        // Create test author and book
        final AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        final BookEntity testBookEntity = TestDataUtil.createTestBookA(testAuthorEntity);

        // Insert into database
        underTest.save(testBookEntity);

        // Retrieve book and assert
        Optional<BookEntity> retrievedBook = underTest.findById(testBookEntity.getIsbn());
        assertThat(retrievedBook).isPresent();

        // Delete book
        underTest.deleteById(testBookEntity.getIsbn());

        // Retrieve book again
        retrievedBook = underTest.findById(testBookEntity.getIsbn());

        // Assert
        assertThat(retrievedBook).isEmpty();
    }
}
