package com.livrotech.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.livrotech.entity.Health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Saude", description = "Verificacao da aplicacao")
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "Verifica a saude da aplicacao")
    public Health health() {

        return new Health(
                "UP",
                "livrotech"
        );
    }
}