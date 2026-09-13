package com.livrotech.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            ObjectProvider<JwtAuthenticationFilter> jwtAuthenticationFilterProvider,
            ObjectProvider<UserDetailsService> userDetailsServiceProvider,
            PasswordEncoder passwordEncoder,
            @Value("${app.security.enabled:false}") boolean securityEnabled) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        JwtAuthenticationFilter jwtAuthenticationFilter = jwtAuthenticationFilterProvider.getIfAvailable();
        UserDetailsService userDetailsService = userDetailsServiceProvider.getIfAvailable();

        if (securityEnabled) {
            http.authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                    .requestMatchers("/health", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/books", "/customers", "/employees", "/sales").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/books", "/customers", "/employees", "/sales").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/books/**", "/customers/**", "/employees/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/books/**", "/customers/**", "/employees/**", "/sales/**").hasRole("ADMIN")
                    .anyRequest().authenticated());
            if (userDetailsService != null) {
                http.authenticationProvider(authenticationProvider(userDetailsService, passwordEncoder));
            }
            http.httpBasic(AbstractHttpConfigurer::disable)
                    .formLogin(AbstractHttpConfigurer::disable);
            if (jwtAuthenticationFilter != null) {
                http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            }
        } else {
            http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .formLogin(AbstractHttpConfigurer::disable);
        }

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnProperty(name = "app.security.enabled", havingValue = "true")
    AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

}
