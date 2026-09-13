package com.livrotech.controller;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.livrotech.config.AppUserService;
import com.livrotech.dto.UserCreateRequestDTO;
import com.livrotech.dto.UserResponseDTO;

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

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cria usuário", description = "Cria um novo usuário com papel ADMIN ou USER")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateRequestDTO dto) {
        var createdUser = appUserService.createUser(dto.username(), dto.password(), dto.role());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.from(createdUser));
    }
}
