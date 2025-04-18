package com.learning.PGdatabase.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.PGdatabase.domain.AuthorEntity;
import com.learning.PGdatabase.domain.BookEntity;
import com.learning.PGdatabase.domain.DTO.BookDto;
import com.learning.PGdatabase.mappers.impl.AuthorMapper;
import com.learning.PGdatabase.mappers.impl.BookMapper;
import com.learning.PGdatabase.services.BookService;
import com.learning.PGdatabase.utility.TestDataUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTests {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;
    private final BookService bookService;

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, BookMapper bookMapper, AuthorMapper authorMapper, BookService bookService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.bookMapper = bookMapper;
        this.authorMapper = authorMapper;
        this.bookService = bookService;
    }

    @Test
    public void testThatCreateBookReturnsStatus201Created() throws Exception {
        // Create test book json
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        BookEntity testBookEntity = TestDataUtil.createTestBookA(testAuthorEntity);
        BookDto testBookDto = bookMapper.mapFrom(testBookEntity);
        String testBookJson = objectMapper.writeValueAsString(testBookDto);

        // Test using MockMVC
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/{isbn}", testBookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testBookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateBookReturnsCreatedBook() throws Exception {
        // Create test book json
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        BookEntity testBookEntity = TestDataUtil.createTestBookA(testAuthorEntity);
        BookDto testBookDto = bookMapper.mapFrom(testBookEntity);
        String testBookJson = objectMapper.writeValueAsString(testBookDto);

        // Test using MockMVC
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/{isbn}", testBookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testBookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookEntity.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.author.id").isNumber()
        );
    }

    @Test
    public void testThatListBooksReturnsHttpStatus200() throws Exception {
        // Create test book
        AuthorEntity testAuthorEntity1 = TestDataUtil.createTestAuthorA();
        AuthorEntity testAuthorEntity2 = TestDataUtil.createTestAuthorB();
        BookEntity testBookEntity1 = TestDataUtil.createTestBookA(testAuthorEntity1);
        BookEntity testBookEntity2 = TestDataUtil.createTestBookA(testAuthorEntity2);

        // Add book to database
        bookService.saveBook(testBookEntity1.getIsbn(), testBookEntity1);
        bookService.saveBook(testBookEntity2.getIsbn(), testBookEntity2);

        // Test using MockMVC
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListBooksReturnsListOfBooks() throws Exception {
        // Create test book
        AuthorEntity testAuthorEntity1 = TestDataUtil.createTestAuthorA();
        AuthorEntity testAuthorEntity2 = TestDataUtil.createTestAuthorB();
        BookEntity testBookEntity1 = TestDataUtil.createTestBookA(testAuthorEntity1);
        BookEntity testBookEntity2 = TestDataUtil.createTestBookB(testAuthorEntity2);

        // Add book to database
        bookService.saveBook(testBookEntity1.getIsbn(), testBookEntity1);
        bookService.saveBook(testBookEntity2.getIsbn(), testBookEntity2);

        // Test using MockMVC
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(2))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].isbn").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].title").value(testBookEntity1.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].author.id").isNumber()
        );
    }

    @Test
    public void testThatGetBookReturnsHttpStatus200WhenBookExists() throws Exception {
        // Create test book
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        BookEntity testBookEntity = TestDataUtil.createTestBookA(testAuthorEntity);

        // Add book to database
        bookService.saveBook(testBookEntity.getIsbn(), testBookEntity);

        // Test using MockMVC
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/{isbn}", testBookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetBookReturnsHttpStatus404WhenBookNotFound() throws Exception {
        // Create test book
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        BookEntity testBookEntity = TestDataUtil.createTestBookA(testAuthorEntity);

        // Test using MockMVC
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/{isbn}", testBookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetBookReturnsBookWhenBookExists() throws Exception {
        // Create test book
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        BookEntity testBookEntity = TestDataUtil.createTestBookA(testAuthorEntity);

        // Add book to database
        bookService.saveBook(testBookEntity.getIsbn(), testBookEntity);

        // Test using MockMVC
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/{isbn}", testBookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookEntity.getTitle())
        );
    }

    @Test
    public void testThatFullUpdateBookReturnsHttpStatus200WhenBookExists() throws Exception {
        // Create test book
        AuthorEntity testAuthorEntity1 = TestDataUtil.createTestAuthorA();
        BookEntity testBookEntity1 = TestDataUtil.createTestBookA(testAuthorEntity1);

        // Insert book into database
        bookService.saveBook(testBookEntity1.getIsbn(), testBookEntity1);

        // Create new test book and turn it into json
        AuthorEntity testAuthorEntity2 = TestDataUtil.createTestAuthorB();
        BookEntity testBookEntity2 = TestDataUtil.createTestBookB(testAuthorEntity2);
        BookDto testBookDto = bookMapper.mapFrom(testBookEntity2);
        String testBookJson = objectMapper.writeValueAsString(testBookDto);

        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/{isbn}", testBookEntity1.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testBookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateBookUpdatesExistingBook() throws Exception {
        // Create test book
        AuthorEntity testAuthorEntity1 = TestDataUtil.createTestAuthorA();
        BookEntity testBookEntity1 = TestDataUtil.createTestBookA(testAuthorEntity1);

        // Insert book into database
        bookService.saveBook(testBookEntity1.getIsbn(), testBookEntity1);

        // Create new test book and turn it into json
        AuthorEntity testAuthorEntity2 = TestDataUtil.createTestAuthorB();
        BookEntity testBookEntity2 = TestDataUtil.createTestBookB(testAuthorEntity2);
        BookDto testBookDto = bookMapper.mapFrom(testBookEntity2);
        String testBookJson = objectMapper.writeValueAsString(testBookDto);

        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/{isbn}", testBookEntity1.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testBookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookEntity1.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookEntity2.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.author.name").value(testBookEntity2.getAuthor().getName())
        );
    }

    @Test
    public void testThatPartialUpdateBookReturnsHttpStatus200WhenBookExists() throws Exception {
        // Create test book
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        BookEntity testBookEntity = TestDataUtil.createTestBookA(testAuthorEntity);

        // Insert book into database
        bookService.saveBook(testBookEntity.getIsbn(), testBookEntity);

        // Turn updated book into json
        testBookEntity.setTitle("UPDATED TITLE");
        testAuthorEntity.setName("UPDATED AUTHOR NAME");
        testBookEntity.setAuthor(testAuthorEntity);
        BookDto testBookDto = bookMapper.mapFrom(testBookEntity);
        String testBookJson = objectMapper.writeValueAsString(testBookDto);

        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/{isbn}", testBookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testBookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateBookReturnsHttpStatus404WhenNoBookExists() throws Exception {
        // Create test book
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        BookEntity testBookEntity = TestDataUtil.createTestBookA(testAuthorEntity);

        // Turn updated book into json
        testBookEntity.setTitle("UPDATED TITLE");
        testAuthorEntity.setName("UPDATED AUTHOR NAME");
        testBookEntity.setAuthor(testAuthorEntity);
        BookDto testBookDto = bookMapper.mapFrom(testBookEntity);
        String testBookJson = objectMapper.writeValueAsString(testBookDto);

        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/{isbn}", testBookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testBookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateBookUpdatesExistingBook() throws Exception {
        // Create test book
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        BookEntity testBookEntity = TestDataUtil.createTestBookA(testAuthorEntity);

        // Insert book into database
        bookService.saveBook(testBookEntity.getIsbn(), testBookEntity);

        // Turn updated book into json
        testBookEntity.setTitle("UPDATED TITLE");
        testAuthorEntity.setName("UPDATED AUTHOR NAME");
        testBookEntity.setAuthor(testAuthorEntity);
        BookDto testBookDto = bookMapper.mapFrom(testBookEntity);
        String testBookJson = objectMapper.writeValueAsString(testBookDto);

        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/{isbn}", testBookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testBookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookEntity.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.author.name").value(testBookEntity.getAuthor().getName())
        );
    }

    @Test
    public void testThatDeleteBookReturnsHttpStatus204WhenBookExists() throws Exception {
        // Create test book
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        BookEntity testBookEntity = TestDataUtil.createTestBookA(testAuthorEntity);

        // Insert book into database
        bookService.saveBook(testBookEntity.getIsbn(), testBookEntity);

        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/{isbn}", testBookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteBookReturnsHttpStatus404WhenNoBookExists() throws Exception {
        // Create test book
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        BookEntity testBookEntity = TestDataUtil.createTestBookA(testAuthorEntity);

        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/{isbn}", testBookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }
}
