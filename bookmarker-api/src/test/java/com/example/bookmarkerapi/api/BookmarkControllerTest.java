package com.example.bookmarkerapi.api;

import com.example.bookmarkerapi.domain.Bookmark;
import com.example.bookmarkerapi.domain.BookmarkRepository;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:latest:///demo"
})
class BookmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookmarkRepository repository;

    private List<Bookmark> bookmarks;

    @BeforeEach
    void setUp() {
        repository.deleteAllInBatch();

        bookmarks = new ArrayList<>();
        bookmarks.add(new Bookmark(null, "A", "https://www.google.com", Instant.now()));
        bookmarks.add(new Bookmark(null, "B", "https://www.google.com", Instant.now()));
        bookmarks.add(new Bookmark(null, "C", "https://www.google.com", Instant.now()));
        bookmarks.add(new Bookmark(null, "D", "https://www.google.com", Instant.now()));
        bookmarks.add(new Bookmark(null, "E", "https://www.google.com", Instant.now()));
        bookmarks.add(new Bookmark(null, "F", "https://www.google.com", Instant.now()));
        bookmarks.add(new Bookmark(null, "G", "https://www.google.com", Instant.now()));
        bookmarks.add(new Bookmark(null, "H", "https://www.google.com", Instant.now()));
        bookmarks.add(new Bookmark(null, "I", "https://www.google.com", Instant.now()));
        bookmarks.add(new Bookmark(null, "J", "https://www.google.com", Instant.now()));
        bookmarks.add(new Bookmark(null, "K", "https://www.google.com", Instant.now()));
        bookmarks.add(new Bookmark(null, "L", "https://www.google.com", Instant.now()));
        bookmarks.add(new Bookmark(null, "M", "https://www.google.com", Instant.now()));
        bookmarks.add(new Bookmark(null, "N", "https://www.google.com", Instant.now()));

        repository.saveAll(bookmarks);
    }

    @ParameterizedTest
    @CsvSource({
            "1,14,2,1,true,false,true,false",
            "2,14,2,2,false,true,false,true",

    })
    @SneakyThrows
    void shouldGetBookMarks(int pageNo, int totalElements, int totalPages, int currentPage, boolean isFirst,
                            boolean isLast, boolean hasNext, boolean hasPrevious) {
        mockMvc.perform(get("/api/bookmarks?page=" + pageNo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", CoreMatchers.equalTo(totalElements)))
                .andExpect(jsonPath("$.totalPages", CoreMatchers.equalTo(totalPages)))
                .andExpect(jsonPath("$.currentPage", CoreMatchers.equalTo(currentPage)))
                .andExpect(jsonPath("$.isFirst", CoreMatchers.equalTo(isFirst)))
                .andExpect(jsonPath("$.isLast", CoreMatchers.equalTo(isLast)))
                .andExpect(jsonPath("$.hasNext", CoreMatchers.equalTo(hasNext)))
                .andExpect(jsonPath("$.hasPrevious", CoreMatchers.equalTo(hasPrevious)))
        ;
    }

    @Test
    @SneakyThrows
    void shouldCreateBookmarkSuccessfully() {
        this.mockMvc.perform(
                        post("/api/bookmarks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "Spring Security",
                                            "url": "https://spring.io"
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Spring Security")))
                .andExpect(jsonPath("$.url", is("https://spring.io")));
    }

    @Test
    void shouldFailToCreateBookmarkWhenUrlIsNotPresent() throws Exception {
        this.mockMvc.perform(
                        post("/api/bookmarks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "Spring Security"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", is("application/problem+json")))
                .andExpect(jsonPath("$.type", is("https://zalando.github.io/problem/constraint-violation")))
                .andExpect(jsonPath("$.title", is("Constraint Violation")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field", is("url")))
                .andExpect(jsonPath("$.violations[0].message", is("Url should not be empty")))
                .andReturn();
    }


}