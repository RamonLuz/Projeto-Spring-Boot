package com.livrotech.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@ConditionalOnProperty(name = "app.security.enabled", havingValue = "true")
@EnableMethodSecurity
public class MethodSecurityConfig {
}
