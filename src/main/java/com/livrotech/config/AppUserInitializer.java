package com.livrotech.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppUserInitializer {

    @Bean
    @ConditionalOnProperty(name = "app.security.enabled", havingValue = "true")
    CommandLineRunner initDefaultUser(AppUserService appUserService) {
        return args -> appUserService.ensureDefaultUserExists();
    }
}
