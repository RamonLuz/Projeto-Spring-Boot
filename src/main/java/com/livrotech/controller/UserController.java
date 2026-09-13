package com.livrotech.controller;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.livrotech.config.AppUserService;
import com.livrotech.dto.PasswordChangeRequestDTO;
import com.livrotech.dto.UserCreateRequestDTO;
import com.livrotech.dto.UserResponseDTO;
import com.livrotech.dto.UserUpdateRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/users")
@ConditionalOnProperty(name = "app.security.enabled", havingValue = "true")
@Tag(name = "Usuários", description = "Gestão de usuários e perfis")
public class UserController {

    private final AppUserService appUserService;

    public UserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Lista usuários", description = "Lista todos os usuários cadastrados")
    public ResponseEntity<List<UserResponseDTO>> listUsers() {
        return ResponseEntity.ok(appUserService.listUsers().stream()
                .map(UserResponseDTO::from)
                .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Busca usuário", description = "Busca um usuário por id")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(UserResponseDTO.from(appUserService.getUserById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cria usuário", description = "Cria um novo usuário com papel ADMIN ou USER")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateRequestDTO dto) {
        var createdUser = appUserService.createUser(dto.username(), dto.password(), dto.role());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.from(createdUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualiza usuário", description = "Atualiza username e role de um usuário")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDTO dto) {
        var updatedUser = appUserService.updateUser(id, dto.username(), dto.role());
        return ResponseEntity.ok(UserResponseDTO.from(updatedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove usuário", description = "Remove um usuário do sistema")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        appUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Altera senha", description = "Permite ao usuário autenticado alterar sua própria senha")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody PasswordChangeRequestDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext() == null
                ? null
                : SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new IllegalStateException("Authentication required");
        }

        appUserService.changePassword(authentication.getName(), dto.currentPassword(), dto.newPassword());
        return ResponseEntity.noContent().build();
    }
}
