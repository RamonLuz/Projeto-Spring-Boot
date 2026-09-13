package com.livrotech.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.livrotech.config.AppUserService;
import com.livrotech.dto.UserCreateRequestDTO;
import com.livrotech.dto.UserResponseDTO;
import com.livrotech.entity.AppUser;
import com.livrotech.entity.AppUserRole;

class UserControllerTest {

    @Test
    void shouldCreateUserForAdmin() {
        AppUserService appUserService = mock(AppUserService.class);
        UserController controller = new UserController(appUserService);

        AppUser createdUser = new AppUser("new-user", "encoded-password", AppUserRole.USER);
        createdUser.setId(10L);

        when(appUserService.createUser("new-user", "secret123", "USER")).thenReturn(createdUser);

        ResponseEntity<UserResponseDTO> response = controller.createUser(new UserCreateRequestDTO("new-user", "secret123", "USER"));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(10L, response.getBody().id());
        assertEquals("new-user", response.getBody().username());
        assertEquals("USER", response.getBody().role());
    }
}
