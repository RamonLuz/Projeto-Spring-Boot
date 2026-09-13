package com.livrotech;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCheckApplicationAndDatabaseHealth() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.database").value("UP"));
    }

    @Test
    void shouldCreateAndFindBookThroughApi() throws Exception {
        String book = """
                {
                    "title": "Livro de integracao",
                    "author": "Autor de teste",
                    "price": 29.90
                }
                """;

        mockMvc.perform(post("/books")
                        .contentType("application/json")
                        .content(book))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Livro de integracao"))
                .andExpect(jsonPath("$.id").isNumber());

        mockMvc.perform(get("/books")
                        .param("title", "integracao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Livro de integracao"));
    }
}
