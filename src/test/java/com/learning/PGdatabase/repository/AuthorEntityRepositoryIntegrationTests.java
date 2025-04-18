package com.learning.PGdatabase.repository;

import com.learning.PGdatabase.domain.AuthorEntity;
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
public class AuthorEntityRepositoryIntegrationTests {
    private final AuthorRepository underTest;

    @Autowired
    public AuthorEntityRepositoryIntegrationTests(AuthorRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatAuthorCanBeCreatedAndRead() {
        final AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        underTest.save(testAuthorEntity);
        Optional<AuthorEntity> underTestById = underTest.findById(testAuthorEntity.getId());
        assertThat(underTestById).isPresent();
        assertThat(underTestById.get()).isEqualTo(testAuthorEntity);
    }

    @Test
    public void testThatMultipleAuthorsCanBeCreatedAndRead() {
        final AuthorEntity testAuthorEntity1 = TestDataUtil.createTestAuthorA();
        final AuthorEntity testAuthorEntity2 = TestDataUtil.createTestAuthorB();
        final AuthorEntity testAuthorEntity3 = TestDataUtil.createTestAuthorC();
        // Insert test authors into the database
        underTest.save(testAuthorEntity1);
        underTest.save(testAuthorEntity2);
        underTest.save(testAuthorEntity3);

        // Retrieve those 3 test authors and check if it's correctly retrieved
        Iterable<AuthorEntity> all = underTest.findAll();
        assertThat(all).containsExactly(
                testAuthorEntity1,
                testAuthorEntity2,
                testAuthorEntity3
        );
    }

    @Test
    public void testThatAuthorIsCorrectlyUpdated() {
        // Insert testAuthor into database
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        underTest.save(testAuthorEntity);

        // Update test author
        testAuthorEntity.setName("Test Name");
        underTest.save(testAuthorEntity);

        // Retrieve
        Optional<AuthorEntity> updatedAuthor = underTest.findById(testAuthorEntity.getId());
        assertThat(updatedAuthor).isPresent();
        assertThat(updatedAuthor.get()).isEqualTo(testAuthorEntity);
    }

    @Test
    public void testThatAuthorIsCorrectlyDeleted() {
        // Insert testAuthor into database
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        underTest.save(testAuthorEntity);

        // Retrieve test author
        Optional<AuthorEntity> retrievedAuthor = underTest.findById(testAuthorEntity.getId());
        assertThat(retrievedAuthor).isPresent();

        // Delete test author
        underTest.deleteById(testAuthorEntity.getId());

        // Retrieve again
        retrievedAuthor = underTest.findById(testAuthorEntity.getId());
        assertThat(retrievedAuthor).isEmpty();
    }

    @Test
    public void testOfCustomQuery() {
        // Create test author
        AuthorEntity testAuthorEntity1 = TestDataUtil.createTestAuthorA();
        AuthorEntity testAuthorEntity2 = TestDataUtil.createTestAuthorB();
        AuthorEntity testAuthorEntity3 = TestDataUtil.createTestAuthorC();

        // Insert into database
        underTest.save(testAuthorEntity1);
        underTest.save(testAuthorEntity2);
        underTest.save(testAuthorEntity3);

        // Custom query
        Iterable<AuthorEntity> testList = underTest.findByAgeLessThan(40);

        // Assert
        assertThat(testList).containsExactly(
                testAuthorEntity1
        );
    }

    @Test
    public void testOfHqlQuery() {
        // Create test author
        AuthorEntity testAuthorEntity1 = TestDataUtil.createTestAuthorA();
        AuthorEntity testAuthorEntity2 = TestDataUtil.createTestAuthorB();
        AuthorEntity testAuthorEntity3 = TestDataUtil.createTestAuthorC();

        // Insert into database
        underTest.save(testAuthorEntity1);
        underTest.save(testAuthorEntity2);
        underTest.save(testAuthorEntity3);

        // Custom query
        Iterable<AuthorEntity> testList = underTest.findAuthorsWithAgeGreaterThan(40);

        // Assert
        assertThat(testList).containsExactly(
                testAuthorEntity3
        );
    }
}
