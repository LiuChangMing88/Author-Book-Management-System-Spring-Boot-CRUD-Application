package com.learning.PGdatabase.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.PGdatabase.domain.AuthorEntity;
import com.learning.PGdatabase.domain.DTO.AuthorDto;
import com.learning.PGdatabase.mappers.impl.AuthorMapper;
import com.learning.PGdatabase.services.AuthorService;
import com.learning.PGdatabase.utility.TestDataUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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
public class AuthorControllerIntegrationTests {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthorMapper authorMapper;
    private final AuthorService authorService;

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, AuthorMapper authorMapper, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.authorMapper = authorMapper;
        this.authorService = authorService;
    }

    @Test
    public void testThatCreateAuthorReturnsStatus201Created() throws Exception {
        // Create test author json
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        AuthorDto testAuthorDto = authorMapper.mapFrom(testAuthorEntity);
        String testAuthorJson = objectMapper.writeValueAsString(testAuthorDto);

        // Test using MockMVC
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testAuthorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorReturnsCreatedAuthor() throws Exception {
        // Create test author json
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();
        AuthorDto testAuthorDto = authorMapper.mapFrom(testAuthorEntity);
        String testAuthorJson = objectMapper.writeValueAsString(testAuthorDto);

        // Test using MockMVC
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testAuthorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testAuthorEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthorEntity.getAge())
        );
    }

    @Test
    public void testThatListAuthorsReturnsHttpStatus200() throws Exception {
        // Create test authors
        AuthorEntity testAuthorEntity1 = TestDataUtil.createTestAuthorA();
        AuthorEntity testAuthorEntity2 = TestDataUtil.createTestAuthorB();

        // Insert them into the database
        authorService.saveAuthor(testAuthorEntity1);
        authorService.saveAuthor(testAuthorEntity2);

        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAuthorsReturnsListOfAuthors() throws Exception {
        // Create test authors
        AuthorEntity testAuthorEntity1 = TestDataUtil.createTestAuthorA();
        AuthorEntity testAuthorEntity2 = TestDataUtil.createTestAuthorB();

        // Insert them into the database
        authorService.saveAuthor(testAuthorEntity1);
        authorService.saveAuthor(testAuthorEntity2);

        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(2))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].name").value(testAuthorEntity1.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].age").value(testAuthorEntity1.getAge())
        );
    }

    @Test
    public void testThatGetAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {
        // Create test author
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();

        // Insert into the database
        AuthorEntity savedAuthor = authorService.saveAuthor(testAuthorEntity);

        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/{id}", savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAuthorReturnsHttpStatus404WhenAuthorNotFound() throws Exception {
        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetAuthorReturnsAuthorWhenAuthorExists() throws Exception {
        // Create test author
        AuthorEntity testAuthorEntity = TestDataUtil.createTestAuthorA();

        // Insert into the database
        AuthorEntity savedAuthor = authorService.saveAuthor(testAuthorEntity);

        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/{id}", savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testAuthorEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthorEntity.getAge())
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus404WhenNoAuthorExists() throws Exception {
        // Create test author json
        AuthorEntity testAuthor = TestDataUtil.createTestAuthorA();
        AuthorDto testAuthorDto = authorMapper.mapFrom(testAuthor);
        String testAuthorJson = objectMapper.writeValueAsString(testAuthorDto);

        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testAuthorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {
        // Create test author entity
        AuthorEntity testAuthor = TestDataUtil.createTestAuthorA();

        // Insert author into database
        AuthorEntity savedAuthor = authorService.saveAuthor(testAuthor);

        // Create test author json
        AuthorDto testAuthorDto = authorMapper.mapFrom(savedAuthor);
        String testAuthorJson = objectMapper.writeValueAsString(testAuthorDto);

        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/{id}", savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testAuthorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateAuthorUpdatesExistingAuthor() throws Exception {
        // Create test author entity
        AuthorEntity testAuthor1 = TestDataUtil.createTestAuthorA();
        AuthorEntity testAuthor2 = TestDataUtil.createTestAuthorB();

        // Insert author 1 into database
        AuthorEntity savedAuthor = authorService.saveAuthor(testAuthor1);

        // Create test author 2 json
        AuthorDto testAuthorDto = authorMapper.mapFrom(testAuthor2);
        String testAuthorJson = objectMapper.writeValueAsString(testAuthorDto);

        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/{id}", savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testAuthorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testAuthor2.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthor2.getAge())
        );
    }

    @Test
    public void testThatPartialUpdateAuthorReturnsHttpStatus404WhenNoAuthorFound() throws Exception {
        // Create test author json
        AuthorEntity testAuthor = TestDataUtil.createTestAuthorA();
        testAuthor.setName("UPDATED");
        testAuthor.setAge(null);
        AuthorDto testAuthorDto = authorMapper.mapFrom(testAuthor);
        String testAuthorJson = objectMapper.writeValueAsString(testAuthorDto);

        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testAuthorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {
        // Create test author entity
        AuthorEntity testAuthor = TestDataUtil.createTestAuthorA();

        // Insert author into database
        AuthorEntity savedAuthor = authorService.saveAuthor(testAuthor);

        // Create test author json
        testAuthor.setName("UPDATED");
        testAuthor.setAge(null);
        AuthorDto testAuthorDto = authorMapper.mapFrom(savedAuthor);
        String testAuthorJson = objectMapper.writeValueAsString(testAuthorDto);

        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/{id}", savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testAuthorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateAuthorUpdatesExistingAuthor() throws Exception {
        // Create test author entity
        AuthorEntity testAuthor = TestDataUtil.createTestAuthorA();

        // Insert author into database
        AuthorEntity savedAuthor = authorService.saveAuthor(testAuthor);

        // Create test author json
        testAuthor.setName("UPDATED");
        testAuthor.setAge(null);
        AuthorDto testAuthorDto = authorMapper.mapFrom(savedAuthor);
        String testAuthorJson = objectMapper.writeValueAsString(testAuthorDto);

        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/{id}", savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testAuthorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testAuthor.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthor.getAge())
        );
    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus404WhenNoAuthorExists() throws Exception {
        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/1")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204WhenAuthorExists() throws Exception {
        // Create test author entity
        AuthorEntity testAuthor = TestDataUtil.createTestAuthorA();

        // Insert author into database
        AuthorEntity savedAuthor = authorService.saveAuthor(testAuthor);

        // Test using MockMvc
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/{id}", savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}