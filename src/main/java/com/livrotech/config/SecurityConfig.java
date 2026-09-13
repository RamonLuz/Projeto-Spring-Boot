package com.livrotech.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            @Value("${app.security.enabled:false}") boolean securityEnabled) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/health", "/swagger-ui/**", "/v3/api-docs/**").permitAll();
            if (securityEnabled) {
                auth.anyRequest().authenticated();
            } else {
                auth.anyRequest().permitAll();
            }
        });

        if (securityEnabled) {
            http.httpBasic(Customizer.withDefaults());
            http.formLogin(AbstractHttpConfigurer::disable);
        } else {
            http.httpBasic(AbstractHttpConfigurer::disable);
            http.formLogin(AbstractHttpConfigurer::disable);
        }

        return http.build();
    }

    @Bean
    InMemoryUserDetailsManager userDetailsManager(
            @Value("${APP_SECURITY_USERNAME:admin}") String username,
            @Value("${APP_SECURITY_PASSWORD:admin123}") String password) {
        UserDetails user = User.withUsername(username)
                .password("{noop}" + password)
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
