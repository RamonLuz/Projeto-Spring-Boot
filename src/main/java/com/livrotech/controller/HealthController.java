package com.livrotech.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;

import com.livrotech.entity.Health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Saude", description = "Verificacao da aplicacao")
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/health")
    @Operation(summary = "Verifica a saude da aplicacao")
    public Health health() {

        String databaseStatus = "UP";
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        } catch (RuntimeException exception) {
            databaseStatus = "DOWN";
        }

        return new Health("UP", "livrotech", databaseStatus);
    }
}