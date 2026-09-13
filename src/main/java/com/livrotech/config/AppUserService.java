package com.livrotech.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.livrotech.entity.AppUser;
import com.livrotech.repository.AppUserRepository;

@Service
@ConditionalOnProperty(name = "app.security.enabled", havingValue = "true")
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${APP_SECURITY_USERNAME:admin}")
    private String defaultUsername;

    @Value("${APP_SECURITY_PASSWORD:admin123}")
    private String defaultPassword;

    @Value("${APP_SECURITY_ROLE:ADMIN}")
    private String defaultRole;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public void ensureDefaultUserExists() {
        if (appUserRepository.findByUsername(defaultUsername).isEmpty()) {
            AppUser user = new AppUser(defaultUsername, passwordEncoder.encode(defaultPassword), defaultRole.trim());
            appUserRepository.save(user);
        }
    }
}
