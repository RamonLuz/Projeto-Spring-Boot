package com.livrotech.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.livrotech.entity.Health;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Health health() {

        return new Health(
                "UP",
                "livrotech"
        );
    }
}