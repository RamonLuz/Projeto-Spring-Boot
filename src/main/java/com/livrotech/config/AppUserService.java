package com.livrotech.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.livrotech.entity.AppUser;
import com.livrotech.entity.AppUserRole;
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

    public List<AppUser> listUsers() {
        return appUserRepository.findAll();
    }

    public AppUser getUserById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    public AppUser createUser(String username, String password, String role) {
        String normalizedUsername = username == null ? "" : username.trim();
        if (normalizedUsername.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (appUserRepository.findByUsername(normalizedUsername).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        AppUserRole appRole = AppUserRole.from(role);
        return appUserRepository.save(new AppUser(normalizedUsername, passwordEncoder.encode(password), appRole));
    }

    public AppUser updateUser(Long id, String username, String role) {
        AppUser existingUser = getUserById(id);
        String normalizedUsername = username == null ? existingUser.getUsername() : username.trim();

        if (normalizedUsername.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (!normalizedUsername.equals(existingUser.getUsername())
                && appUserRepository.findByUsername(normalizedUsername).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        existingUser.setUsername(normalizedUsername);
        existingUser.setRole(AppUserRole.from(role));
        return appUserRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        AppUser existingUser = getUserById(id);
        if (existingUser.getUsername().equalsIgnoreCase(defaultUsername)) {
            throw new IllegalArgumentException("Default admin user cannot be removed");
        }
        appUserRepository.delete(existingUser);
    }

    public void changePassword(String username, String currentPassword, String newPassword) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is invalid");
        }

        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password is required");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        appUserRepository.save(user);
    }

    public void ensureDefaultUserExists() {
        if (appUserRepository.findByUsername(defaultUsername).isEmpty()) {
            AppUserRole role = AppUserRole.from(defaultRole);
            AppUser user = new AppUser(defaultUsername, passwordEncoder.encode(defaultPassword), role);
            appUserRepository.save(user);
        }
    }
}
